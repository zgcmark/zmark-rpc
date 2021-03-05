package com.zmark.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zhengguangchen
 */

public class TestProxy {
    private Object target;

    public TestProxy(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {

        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
            new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    Object invoke = method.invoke(o, objects);
                    return invoke;
                }
            });
    }
}
