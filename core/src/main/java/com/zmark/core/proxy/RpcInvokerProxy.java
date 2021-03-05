package com.zmark.core.proxy;

import com.zmark.register.api.RegisterService;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocol;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocolHeader;
import com.zmark.remoting.provider.netty.protocol.ZmarkRpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zhengguangchen
 */

public class RpcInvokerProxy implements InvocationHandler {

    private final RegisterService registerService;

    private final long timeout;

    public RpcInvokerProxy(long timeout, RegisterService registryService) {
        this.registerService = registryService;
        this.timeout = timeout;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        ZmarkProtocol<ZmarkRpcRequest> protocol = new ZmarkProtocol<>();
        ZmarkProtocolHeader header = protocol.getProtocolHeader();
        ZmarkRpcRequest request = protocol.getBody();
        //TODO 动态代理实现
        return null;
    }
}
