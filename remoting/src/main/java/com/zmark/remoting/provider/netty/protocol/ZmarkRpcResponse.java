package com.zmark.remoting.provider.netty.protocol;

/**
 * @author zhengguangchen
 */

public class ZmarkRpcResponse {
    private Object bodyObject;

    public Object getBodyObject() {
        return bodyObject;
    }

    public void setBodyObject(Object bodyObject) {
        this.bodyObject = bodyObject;
    }
}
