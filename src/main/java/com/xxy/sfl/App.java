package com.xxy.sfl;

import com.xxy.sfl.pub.repo.BaseJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * SpringBoot启动类
 *
 * @author xg
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseJpaRepositoryImpl.class)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
