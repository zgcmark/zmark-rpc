package com.zmark.register.zookeeper;

import com.zmark.config.ServiceMeta;
import com.zmark.register.api.ServiceLoadBalancer;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.TreeMap;

/**
 * @author zhengguangchen
 */

public class ZkHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>> {

    @Override
    public ServiceInstance<ServiceMeta> selectService(List<ServiceInstance<ServiceMeta>> servers, int hashCode) {
        //创建hash环
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = createHashRing(servers);

        //分配服务
        selectByRing(ring, hashCode);

        return null;
    }

    private void selectByRing(TreeMap<Integer, ServiceInstance<ServiceMeta>> ring, int hashCode) {
        //TODO 分配hash环的算法
    }

    /**
     * 构建hash环
     *
     * @param servers
     * @return
     */
    private TreeMap<Integer, ServiceInstance<ServiceMeta>> createHashRing(List<ServiceInstance<ServiceMeta>> servers) {

        return null;
    }
}
