package com.zmark.remoting.provider;

import com.zmark.config.ServiceMeta;
import com.zmark.core.annotation.ZmarkRpcService;
import com.zmark.register.api.RegisterService;

/**
 * @author zhengguangchen
 */

public abstract class AbstractServer {

    protected abstract void start() throws Throwable;

    protected abstract void close() throws Throwable;

    protected ServiceMeta register(RegisterService registerService, ZmarkRpcService rpcService, String serviceName,
        String serverAddress, Integer serverPort) throws Throwable {
        ServiceMeta serviceMeta = new ServiceMeta();
        serviceMeta.setServiceAddress(serverAddress);
        serviceMeta.setServicePort(serverPort);
        serviceMeta.setServiceName(serviceName);
        //TODO 注册服务
        return serviceMeta;
    }

}
