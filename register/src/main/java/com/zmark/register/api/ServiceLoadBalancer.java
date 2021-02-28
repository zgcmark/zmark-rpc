package com.zmark.register.api;

import java.util.List;

/**
 * @author zhengguangchen
 */

public interface ServiceLoadBalancer<T> {

    /**
     * 选择一个service的实例
     *
     * @param servers
     * @param hashCode
     * @return
     */
    T selectService(List<T> servers, int hashCode);
}
