package com.wolf.framework.service.request;

import com.wolf.framework.request.Request;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public class ServiceRequestImpl implements ServiceRequest {

    private final Request request;

    public ServiceRequestImpl(Request request) {
        this.request = request;
    }

    @Override
    public final Map<String, String> getParameterMap() {
        return this.request.getParameterMap();
    }

    @Override
    public final String getRoute() {
        return this.request.getRoute();
    }

    @Override
    public final String getSessionId() {
        return this.request.getSessionId();
    }

    @Override
    public final String getParameter(String name) {
        return this.request.getParameter(name);
    }

    @Override
    public String getNewSessionId() {
        return this.request.getNewSessionId();
    }

    @Override
    public final void setNewSessionId(String newSessionId) {
        this.request.setNewSessionId(newSessionId);
    }

    @Override
    public final boolean push(String sid, String responseMessage) {
        return this.request.push(sid, responseMessage);
    }
}
