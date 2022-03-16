package com.nespresso.sofa.interview.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nespresso.sofa.interview.cart.services.CartStorage;

@Configuration
public class AppConfig {

    @Bean
    public CartStorage cartStorage() {
        return new CartStorage();
    }

}
