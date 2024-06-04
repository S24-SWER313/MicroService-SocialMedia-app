package com.search.searchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@EnableFeignClients
@EnableDiscoveryClient
// @EntityScan(basePackages = {"com.mypost.postservice.entity"}) // Adjust this as needed

@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class SearchserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchserviceApplication.class, args);
	}

}
