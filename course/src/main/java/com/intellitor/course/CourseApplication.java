package com.intellitor.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.intellitor.common.mappers", "com.intellitor.common.config", "com.intellitor.course.services", "com.intellitor.course.controllers", "com.intellitor.course.utils"})
@EntityScan(basePackages = "com.intellitor.common.entities")
@EnableJpaRepositories(basePackages = "com.intellitor.course.repos")
public class CourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseApplication.class, args);
	}

}
