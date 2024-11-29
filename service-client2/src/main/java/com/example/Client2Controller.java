
package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@RestController
public class Client2Controller {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/call-client1")
    public String callClient1() {
        return restTemplate.getForObject("http://service-client1/hello", String.class);
    }
}

