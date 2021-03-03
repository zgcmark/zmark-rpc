package com.zmark.remoting.provider.netty.protocol;

/**
 * @author zhengguangchen
 */

public class ZmarkProtocol<T> {
    private ZmarkProtocolHeader protocolHeader;
    private T body;

    public ZmarkProtocolHeader getProtocolHeader() {
        return protocolHeader;
    }

    public void setProtocolHeader(ZmarkProtocolHeader protocolHeader) {
        this.protocolHeader = protocolHeader;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
