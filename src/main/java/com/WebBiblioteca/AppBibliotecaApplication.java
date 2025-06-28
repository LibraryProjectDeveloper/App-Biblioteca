package com.WebBiblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AppBibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppBibliotecaApplication.class, args);
    }

}
