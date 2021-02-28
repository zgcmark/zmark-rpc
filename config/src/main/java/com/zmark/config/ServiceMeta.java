package com.zmark.config;

import java.util.Map;

/**
 * @author zhengguangchen
 */
public class ServiceMeta {
    private String serviceName;
    private String serviceAddress;
    private int servicePort;
    private Map<String, Object> manualData;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public Map<String, Object> getManualData() {
        return manualData;
    }

    public void setManualData(Map<String, Object> manualData) {
        this.manualData = manualData;
    }
}
