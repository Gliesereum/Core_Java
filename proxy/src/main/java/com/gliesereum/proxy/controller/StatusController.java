package com.gliesereum.proxy.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
public class StatusController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/api/status")
    public Map<String, Object> status() {
        Map<String, Object> result = new HashMap<>();
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances) {
                String serviceId = instance.getServiceId();
                InstanceInfo.InstanceStatus status = InstanceInfo.InstanceStatus.UNKNOWN;
                if (instance instanceof EurekaDiscoveryClient.EurekaServiceInstance) {
                    status = ((EurekaDiscoveryClient.EurekaServiceInstance) instance).getInstanceInfo().getStatus();
                }
                result.put(serviceId, status);
            }
        }
        return result;
    }
}
