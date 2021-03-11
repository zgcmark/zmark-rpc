package com.zmark.remoting.provider.netty.handler;

import com.zmark.remoting.provider.netty.RequestPool;
import com.zmark.remoting.provider.netty.enums.MsgTypeEnum;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocol;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocolHeader;
import com.zmark.remoting.provider.netty.protocol.ZmarkRpcRequest;
import com.zmark.remoting.provider.netty.protocol.ZmarkRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author zhengguangchen
 */

public class RpcRequestHandler extends SimpleChannelInboundHandler<ZmarkProtocol<ZmarkRpcRequest>> {

    private Map<String, Object> rpcServiceMap;

    public RpcRequestHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ZmarkProtocol<ZmarkRpcRequest> request) throws Exception {
        RequestPool.submit(() -> {
            ZmarkProtocol<ZmarkRpcResponse> protocol = new ZmarkProtocol<>();
            ZmarkRpcResponse response = new ZmarkRpcResponse();
            try {
                Object data = doHandler(request);
                response.setBodyObject(data);
            } catch (Exception e) {
                response.setBodyObject(e);
            }

            ZmarkProtocolHeader responseHeader = new ZmarkProtocolHeader();
            responseHeader.setMsgType(MsgTypeEnum.response.getValue());
            responseHeader.setRequestId(request.getProtocolHeader().getRequestId());
            protocol.setProtocolHeader(responseHeader);

            ctx.writeAndFlush(protocol);
        });

    }

    /**
     * 真正的代理调用
     *
     * @param request
     */
    private Object doHandler(ZmarkProtocol<ZmarkRpcRequest> protocol) throws InvocationTargetException {
        Object serviceBean = rpcServiceMap.get(protocol.getBody().getClassName());
        if (serviceBean == null) {
            throw new IllegalArgumentException("service not exist");
        }
        ZmarkRpcRequest request = protocol.getBody();

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParams();

        FastClass fastClass = FastClass.create(serviceClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);
        return fastClass.invoke(methodIndex, serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        ctx.close();
    }
}
