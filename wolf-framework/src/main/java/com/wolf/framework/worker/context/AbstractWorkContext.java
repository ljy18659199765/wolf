package com.wolf.framework.worker.context;

import com.wolf.framework.config.FrameworkLogger;
import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.logger.LogFactory;
import com.wolf.framework.reponse.ResponseImpl;
import com.wolf.framework.reponse.WorkerResponse;
import com.wolf.framework.request.RequestImpl;
import com.wolf.framework.request.WorkerRequest;
import com.wolf.framework.utils.StringUtils;
import com.wolf.framework.worker.ServiceWorker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.Logger;

/**
 *
 * @author aladdin
 */
public abstract class AbstractWorkContext implements WorkerContext {

    //input
    private Map<String, Object> parameterMap;
    private final String route;
    private final ServiceWorker serviceWorker;
    private final WorkerRequest request;
    private final WorkerResponse response;

    public AbstractWorkContext(String route, ServiceWorker serviceWorker) {
        this.route = route;
        this.serviceWorker = serviceWorker;
        this.request = new RequestImpl(this);
        this.response = new ResponseImpl(this);
    }

    public void initParameter(Map<String, Object> parameterMap) {
        Map<String, Object> tempMap = new HashMap<>(parameterMap.size(), 1);
        Set<String> keySet = parameterMap.keySet();
        Object value;
        for (String key : keySet) {
            value = parameterMap.get(key);
            if(Integer.class.isInstance(value)) {
                long newValue = (int) value;
                value = newValue;
            } else if(Float.class.isInstance(value)) {
                double newValue = (float) value;
                value = newValue;
            }
            tempMap.put(key, value);
        }
        this.parameterMap = tempMap;
    }

    private Object getValue(JsonNode jsonNode) {
        Object value;
        if (jsonNode.isObject()) {
            value = this.initObject(jsonNode);
        } else if (jsonNode.isArray()) {
            value = this.initArray((ArrayNode) jsonNode);
        } else if (jsonNode.isBoolean()) {
            value = jsonNode.getBooleanValue();
        } else if (jsonNode.isDouble()) {
            value = jsonNode.getDoubleValue();
        } else if (jsonNode.isInt()) {
            long num = jsonNode.getIntValue();
            value = num;
        } else if (jsonNode.isLong()) {
            value = jsonNode.getLongValue();
        } else {
            value = StringUtils.trim(jsonNode.getTextValue());
        }
        return value;
    }

    private Map<String, Object> initObject(JsonNode paramNode) {
        Map<String, Object> paramMap = new HashMap<>(8, 1);
        Iterator<Map.Entry<String, JsonNode>> iterator = paramNode.getFields();
        Map.Entry<String, JsonNode> entry;
        String name;
        Object value;
        JsonNode jsonNode;
        while (iterator.hasNext()) {
            entry = iterator.next();
            name = entry.getKey();
            jsonNode = entry.getValue();
            if (jsonNode.isNull() == false) {
                value = this.getValue(jsonNode);
                paramMap.put(name, value);
            }
        }
        return paramMap;
    }

    private List<Object> initArray(ArrayNode paramNode) {
        List<Object> paramList = new ArrayList<>();
        Iterator<JsonNode> iterator = paramNode.getElements();
        JsonNode jsonNode;
        Object value;
        while (iterator.hasNext()) {
            jsonNode = iterator.next();
            if (jsonNode.isNull() == false) {
                value = this.getValue(jsonNode);
                paramList.add(value);
            }
        }
        return paramList;
    }

    public void initParameter(String json) {
        if (json.isEmpty() == false) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = null;
            try {
                rootNode = mapper.readValue(json, JsonNode.class);
            } catch (IOException e) {
                Logger logger = LogFactory.getLogger(FrameworkLogger.FRAMEWORK);
                logger.error("error json message:{}", json);
                logger.error("parse json error:", e);
            }
            if (rootNode != null) {
                //读数据
                JsonNode paramNode = rootNode.get("param");
                if (paramNode.isNull() == false) {
                    this.parameterMap = new HashMap<>(4, 1);
                    Map.Entry<String, JsonNode> entry;
                    String name;
                    Object value;
                    JsonNode jsonNode;
                    Iterator<Map.Entry<String, JsonNode>> iterator = paramNode.getFields();
                    while (iterator.hasNext()) {
                        entry = iterator.next();
                        name = entry.getKey();
                        jsonNode = entry.getValue();
                        if (jsonNode.isNull() == false) {
                            value = this.getValue(jsonNode);
                            this.parameterMap.put(name, value);
                        }
                    }
                } else {
                    this.parameterMap = Collections.emptyMap();
                }
            } else {
                this.parameterMap = Collections.emptyMap();
            }
        } else {
            this.parameterMap = Collections.emptyMap();
        }
    }

    @Override
    public ServiceWorker getServiceWorker() {
        return this.serviceWorker;
    }

    @Override
    public final String getRoute() {
        return route;
    }

    @Override
    public final Map<String, Object> getParameterMap() {
        return Collections.unmodifiableMap(this.parameterMap);
    }

    @Override
    public final Object getParameter(String name) {
        return this.parameterMap.get(name);
    }

    @Override
    public final ApplicationContext getApplicationContext() {
        return ApplicationContext.CONTEXT;
    }

    @Override
    public WorkerRequest getWorkerRequest() {
        return this.request;
    }

    @Override
    public WorkerResponse getWorkerResponse() {
        return this.response;
    }
}
