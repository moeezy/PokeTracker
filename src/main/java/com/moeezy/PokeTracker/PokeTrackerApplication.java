package com.moeezy.PokeTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@RestController
public class PokeTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokeTrackerApplication.class, args);
	}

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://pokeapi.co/api/v2")
                .build();
    }
}

