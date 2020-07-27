package com.trafigura.infra.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by xingxiaoning on 2020/07/26.
 */
@EnableEurekaServer
@SpringCloudApplication
public class MicroServiceApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(MicroServiceApplication.class, args);
    }
}
