package com.sb.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sb.demo.pub.repo.BaseJpaRepositoryImpl;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseJpaRepositoryImpl.class)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
