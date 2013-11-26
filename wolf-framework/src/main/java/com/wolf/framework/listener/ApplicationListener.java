package com.wolf.framework.listener;

import com.sun.grizzly.websockets.WebSocketEngine;
import com.wolf.framework.config.FrameworkConfig;
import com.wolf.framework.config.FrameworkLoggerEnum;
import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.context.ApplicationContextBuilder;
import com.wolf.framework.logger.LogFactory;
import com.wolf.framework.websocket.GlobalApplication;
import com.wolf.framework.websocket.ManagementApplication;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;

/**
 * 应用程序全局信息初始化
 *
 * @author aladdin
 */
public class ApplicationListener implements ServletContextListener {

    private static GlobalApplication APP = null;
    private static ManagementApplication MAPP = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Logger logger = LogFactory.getLogger(FrameworkLoggerEnum.FRAMEWORK);
        //1.加载系统配置
        String appFilePath = sce.getServletContext().getRealPath("");
        int index = appFilePath.lastIndexOf(File.separator);
        String appRootPath = appFilePath.substring(index);
        StringBuilder fileBuilder = new StringBuilder(64);
        fileBuilder.append(appFilePath).append(File.separator).append("WEB-INF").append(File.separator).append(FrameworkConfig.CONFIG_FILE);
        String filePath = fileBuilder.toString();
        logger.info("Finding config file:".concat(filePath));
        File file = new File(filePath);
        final Map<String, String> parameterMap = new HashMap<String, String>(2, 1);
        if (file.exists()) {
            //读取配置文件
            logger.info("Reading config file:".concat(filePath));
            JsonNode rootNode;
            ObjectMapper mapper = new ObjectMapper();
            try {
                rootNode = mapper.readValue(file, JsonNode.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //解析保存配置文件
            Entry<String, JsonNode> entry;
            String name;
            String value;
            Iterator<Entry<String, JsonNode>> iterator = rootNode.getFields();
            JsonNode jsonNode;
            while (iterator.hasNext()) {
                entry = iterator.next();
                name = entry.getKey();
                jsonNode = entry.getValue().get("value");
                if (jsonNode == null) {
                    value = "";
                } else {
                    value = jsonNode.getTextValue();
                }
                parameterMap.put(name, value);
            }
        } else {
            throw new RuntimeException("Error when init server. Cause:can not find ".concat(FrameworkConfig.CONFIG_FILE));
        }
        //2.初始化全局信息
        logger.info("Initializing applicationContext...");
        ApplicationContextBuilder applicationContextBuilder = new ApplicationContextBuilder(parameterMap);
        applicationContextBuilder.build();
        logger.info("Start websocket...");
        //开启websocket应用
        ApplicationListener.APP = new GlobalApplication(appRootPath);
        WebSocketEngine.getEngine().register(ApplicationListener.APP);
        //开启开发模式
        String compileModel = parameterMap.get(FrameworkConfig.COMPILE_MODEL);
        if (compileModel != null && compileModel.equals(FrameworkConfig.DEVELOPMENT)) {
            ApplicationListener.MAPP = new ManagementApplication(appRootPath);
            WebSocketEngine.getEngine().register(ApplicationListener.MAPP);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (ApplicationListener.APP != null) {
            WebSocketEngine.getEngine().unregister(ApplicationListener.APP);
            ApplicationListener.APP.shutdown();
        }
        if (ApplicationListener.MAPP != null) {
            WebSocketEngine.getEngine().unregister(ApplicationListener.MAPP);
        }
        ApplicationContext.CONTEXT.contextDestroyed();
    }
}
