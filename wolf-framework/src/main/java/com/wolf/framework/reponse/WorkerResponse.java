package com.wolf.framework.reponse;

/**
 *
 * @author jianying9
 */
public interface WorkerResponse extends Response {
    
    public void denied();

    public void invalid();

    public void unlogin();
    
    public void setError(String error);

    public String createErrorMessage();
}
