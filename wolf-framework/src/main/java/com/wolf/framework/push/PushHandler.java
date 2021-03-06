package com.wolf.framework.push;

/**
 *
 * @author jianying9
 */
public interface PushHandler {

    public boolean contains(String sid);

    public boolean push(String sid, String route, String message);

    public boolean asyncPush(String sid, String route, String message);
}
