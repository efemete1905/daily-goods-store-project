package com.example.odev.proje;

import com.example.odev.proje.dao.usersRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.odev.proje")
public class ProjeApplication  {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); // .env dosyasını yükler
		SpringApplication.run(ProjeApplication.class, args);
	}

}
