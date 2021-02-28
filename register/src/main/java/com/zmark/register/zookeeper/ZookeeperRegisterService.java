package com.zmark.register.zookeeper;

import com.zmark.config.ServiceMeta;
import com.zmark.register.api.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;

/**
 * @author zhengguangchen
 */
@Slf4j
public class ZookeeperRegisterService implements RegisterService {
    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;
    public static final String ZK_BASE_PATH = "/zmark_rpc";
    private final ServiceDiscovery<ServiceMeta> serviceDiscovery;

    public ZookeeperRegisterService(String registryAddr) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory
            .newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class).client(client).serializer(serializer)
            .basePath(ZK_BASE_PATH).build();
        this.serviceDiscovery.start();
    }

    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance =
            ServiceInstance.<ServiceMeta>builder().name(serviceMeta.getServiceName())
                .address(serviceMeta.getServiceAddress()).port(serviceMeta.getServicePort()).payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        return null;
    }

    @Override
    public void destroy() throws IOException {
        serviceDiscovery.close();
    }
}
