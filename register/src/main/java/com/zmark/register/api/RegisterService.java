package com.zmark.register.api;

import com.zmark.config.ServiceMeta;

import java.io.IOException;

/**
 * @author zhengguangchen
 */

public interface RegisterService {
    /**
     * 注册
     *
     * @param serviceMeta
     * @throws Exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 注销
     *
     * @param serviceMeta
     * @throws Exception
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务发现
     *
     * @param serviceName
     * @param invokerHashCode
     * @return
     * @throws Exception
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    /**
     * 销毁
     *
     * @throws IOException
     */
    void destroy() throws IOException;

}
