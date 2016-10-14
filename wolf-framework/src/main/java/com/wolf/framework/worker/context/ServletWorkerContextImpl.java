package com.wolf.framework.worker.context;

import com.wolf.framework.servlet.ServiceServlet;
import com.wolf.framework.worker.ServiceWorker;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public class ServletWorkerContextImpl extends AbstractWorkContext {
    
    private final ServiceServlet serviceServlet;

    private String sid;

    public ServletWorkerContextImpl(ServiceServlet serviceServlet, String sid, String act, Map<String, String> parameterMap, ServiceWorker serviceWorker) {
        super(act, parameterMap, serviceWorker);
        this.sid = sid;
        this.serviceServlet = serviceServlet;
    }

    @Override
    public String getSessionId() {
        return this.sid;
    }

    @Override
    public void saveNewSession(String sid) {
        this.sid = sid;
        this.serviceServlet.saveNewSession(sid);
    }

    @Override
    public void removeSession() {
        this.sid = null;
        this.serviceServlet.removeSession(sid);
    }
}
