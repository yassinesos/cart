package com.nespresso.sofa.interview.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.nespresso.sofa.interview.cart.services.CartStorage;

@Configuration
@ComponentScan("com.nespresso.sofa.interview.cart.services")
public class AppConfig {

    @Bean
    public CartStorage cartStorage() {
        return new CartStorage();
    }

}
