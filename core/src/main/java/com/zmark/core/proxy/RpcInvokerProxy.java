package com.zmark.core.proxy;

import com.zmark.core.RpcFuture;
import com.zmark.core.RpcRequestHolder;
import com.zmark.register.api.RegisterService;
import com.zmark.remoting.consumer.ZmarkRpcConsumer;
import com.zmark.remoting.provider.netty.enums.MsgTypeEnum;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocol;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocolHeader;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocolType;
import com.zmark.remoting.provider.netty.protocol.ZmarkRpcRequest;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

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
        long requestId = RpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setMagic(ZmarkProtocolType.MAGIC);
        header.setRequestId(requestId);
        header.setMsgType(MsgTypeEnum.request.getValue());
        protocol.setProtocolHeader(header);

        //设置请求体数据
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(objects);

        protocol.setBody(request);

        //发起远程
        ZmarkRpcConsumer zmarkRpcConsumer = new ZmarkRpcConsumer();
        RpcFuture future = new RpcFuture(new DefaultPromise(new DefaultEventLoop()), timeout);
        return null;
    }
}
