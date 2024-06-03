package com.mypost.postservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
// @EntityScan(basePackages = {"com.mypost.postservice.entity"}) // Adjust this as needed
@EnableJpaRepositories(basePackages = "com.mypost.postservice.postInfo")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
// @ComponentScan(basePackages = "com.mypost.postservice.postInfo")
public class PostserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostserviceApplication.class, args);
    }

}
