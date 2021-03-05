package com.zmark.core;

import com.zmark.core.proxy.RpcInvokerProxy;
import com.zmark.register.api.RegisterService;
import com.zmark.register.zookeeper.ZookeeperRegisterService;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author zhengguangchen
 */

public class ZmarkRpcReferenceBean implements FactoryBean<Object> {
    private Class<?> interfaceClass;
    private long timeout;
    private String registryAddr;
    private Object object;

    public String getRegistryAddr() {
        return registryAddr;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void init() throws Exception {
        //TODO 动态代理生成对象
        RegisterService registryService = new ZookeeperRegisterService(registryAddr);
        object = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass},
            new RpcInvokerProxy(timeout, registryService));
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
