package com.flymatcher.skyscanner.adaptor.bootstrap;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
//@formatter:off
@SpringBootApplication(scanBasePackages = { "com.flymatcher.skyscanner.adaptor" })
//@formatter:on
public class ServiceRunner {
  public static void main(final String[] args) {
    run(ServiceRunner.class, args);
  }
}
