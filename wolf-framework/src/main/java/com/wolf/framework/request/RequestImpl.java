package com.wolf.framework.request;

import com.wolf.framework.comet.CometContext;
import com.wolf.framework.worker.context.WorkerContext;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public class RequestImpl implements WorkerRequest {

    private final WorkerContext workerContext;
    private final Map<String, String> parameterMap;
    private String newSessionId = null;

    public RequestImpl(WorkerContext workerContext) {
        this.workerContext = workerContext;
        this.parameterMap = new HashMap<String, String>(8, 1);
    }

    @Override
    public Map<String, String> getParameterMap() {
        return this.parameterMap;
    }

    @Override
    public String getRoute() {
        return this.workerContext.getRoute();
    }

    @Override
    public String getSessionId() {
        return this.workerContext.getSessionId();
    }

    @Override
    public void removeSession() {
        this.workerContext.removeSession();
    }

    @Override
    public String getParameter(String name) {
        return this.parameterMap.get(name);
    }

    @Override
    public void putParameter(String name, String value) {
        this.parameterMap.put(name, value);
    }

    @Override
    public String getNewSessionId() {
        return this.newSessionId;
    }

    @Override
    public void setNewSessionId(String newSessionId) {
        this.newSessionId = newSessionId;
    }

    @Override
    public boolean push(String sid, String responseMessage) {
        CometContext cometContext = this.workerContext.getApplicationContext().getCometContext();
        return cometContext.push(sid, responseMessage);
    }
}