package com.example.jariBean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoAuditing
@EnableMongoRepositories
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class JariBeanApplication {

	public static void main(String[] args) {
		SpringApplication.run(JariBeanApplication.class, args);
	}

}
