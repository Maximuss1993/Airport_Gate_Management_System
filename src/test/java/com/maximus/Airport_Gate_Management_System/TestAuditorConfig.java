package com.maximus.Airport_Gate_Management_System;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class TestAuditorConfig {

  @Bean
  public AuditorAware<String> auditorAware() {
    return () -> Optional.of("testUser");
  }
}
