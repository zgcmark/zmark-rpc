package com.zmark.remoting.provider.netty;

import com.zmark.config.ServiceMeta;
import com.zmark.core.annotation.ZmarkRpcService;
import com.zmark.register.api.RegisterService;
import com.zmark.remoting.provider.AbstractServer;
import com.zmark.remoting.provider.netty.handler.RpcRequestHandler;
import com.zmark.remoting.provider.netty.handler.ZmarkRpcDecoder;
import com.zmark.remoting.provider.netty.handler.ZmarkRpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengguangchen
 */
@Slf4j
public class NettyServer extends AbstractServer implements InitializingBean, BeanPostProcessor {

    private final RegisterService serviceRegistry;

    private final Integer port;

    private final String serverAddress = InetAddress.getLocalHost().getHostAddress();

    private final Map<String, Object> rpcServiceMap = new ConcurrentHashMap<>();

    public NettyServer(RegisterService serviceRegistry, Integer port) throws UnknownHostException {
        this.serviceRegistry = serviceRegistry;
        this.port = port;
    }

    @Override
    public void start() throws Throwable {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ZmarkRpcEncoder()).addLast(new ZmarkRpcDecoder())
                            .addLast(new RpcRequestHandler(rpcServiceMap));
                    }
                }).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(this.serverAddress, this.port).sync();
            log.info("server addr {} started on port {}", this.serverAddress, this.port);
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public void close() throws Throwable {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                start();
            } catch (Throwable throwable) {
                log.error("start rpc server error.", throwable);
            }
        });
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ZmarkRpcService service = bean.getClass().getAnnotation(ZmarkRpcService.class);
        if (service != null) {
            String serviceName = service.name();
            if (StringUtil.isNullOrEmpty(serviceName)) {
                //默认取service的name
                serviceName = beanName;
            }

            try {
                //注册服务
                ServiceMeta serviceMeta = super.register(serviceRegistry, service, serviceName, serverAddress, port);
                rpcServiceMap.putIfAbsent(serviceName, serviceMeta);
            } catch (Throwable throwable) {
                log.error("server register Error :{}", throwable);
            }
        }

        return bean;
    }
}
