package com.wolf.framework.worker.context;

import com.wolf.framework.session.Session;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public interface WorkerContext {

    public Map<String, String> getParameterMap();

    public String getAct();

    public void sendMessage();

    public void close();

    public void saveNewSession(Session session);

    public void removeSession();
    
    public Session getSession();
    
    public String getResponseMessage();

    public void setResponseMessage(String responseMessage);
}