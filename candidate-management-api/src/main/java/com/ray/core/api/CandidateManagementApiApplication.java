package com.ray.core.api;

import com.ray.cloud.framework.jetty.JettyFrameWorkServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
//用于启动服务发现功能
@EnableDiscoveryClient
//用于启动Fegin功能
@EnableFeignClients(basePackages = {"com.ray.cloud"})
@SpringBootApplication
public class CandidateManagementApiApplication extends JettyFrameWorkServer{

	public static void main(String[] args) {
		SpringApplication.run(CandidateManagementApiApplication.class, args);
	}
}
