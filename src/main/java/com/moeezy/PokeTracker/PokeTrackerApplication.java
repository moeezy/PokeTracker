package com.moeezy.PokeTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PokeTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokeTrackerApplication.class, args);
	}

    @GetMapping
    public String helloWorld(){
        return "AYo hello world";
    }
}

