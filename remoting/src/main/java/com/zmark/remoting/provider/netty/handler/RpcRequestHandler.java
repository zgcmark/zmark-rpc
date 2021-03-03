package com.zmark.remoting.provider.netty.handler;

import com.zmark.remoting.provider.netty.protocol.ZmarkProtocol;
import com.zmark.remoting.provider.netty.protocol.ZmarkRpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
        //TODO 处理接受到的信息

    }
}
