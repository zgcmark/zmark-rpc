package com.zmark.remoting.consumer;

import com.zmark.config.ServiceMeta;
import com.zmark.register.api.RegisterService;
import com.zmark.remoting.provider.netty.codec.ZmarkRpcDecoder;
import com.zmark.remoting.provider.netty.codec.ZmarkRpcEncoder;
import com.zmark.remoting.provider.netty.handler.RpcResponseHandler;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocol;
import com.zmark.remoting.provider.netty.protocol.ZmarkRpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;

/**
 * @author zhengguangchen
 */
@Slf4j
public class ZmarkRpcConsumer {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public ZmarkRpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(2);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ZmarkRpcDecoder()).addLast(new ZmarkRpcEncoder())
                        .addLast(new RpcResponseHandler());
                }
            });
    }

    public void request(ZmarkProtocol<ZmarkRpcRequest> protocol, RegisterService registerService) throws Exception {
        ZmarkRpcRequest request = protocol.getBody();
        // 这个地方的实现很糙
        int randomHash = RandomUtils.nextInt(1000);
        ServiceMeta serviceMeta = registerService.discovery(request.getClassName(), randomHash);
        if (serviceMeta == null) {
            log.error("discovery not find any consumer :{}", request.getClassName());
            return;
        }
        ChannelFuture future = bootstrap.connect(serviceMeta.getServiceAddress(), serviceMeta.getServicePort()).sync();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    log.info("connect success ");
                } else {
                    eventLoopGroup.shutdownGracefully();
                }
            }
        });
        future.channel().writeAndFlush(protocol);
    }
}
