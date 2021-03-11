package com.zmark.remoting.provider.netty.handler;

import com.zmark.remoting.provider.netty.protocol.ZmarkProtocol;
import com.zmark.remoting.provider.netty.protocol.ZmarkRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author zhengguangchen
 */

public class RpcResponseHandler extends SimpleChannelInboundHandler<ZmarkProtocol<ZmarkRpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ZmarkProtocol<ZmarkRpcResponse> response) throws Exception {

    }
}
