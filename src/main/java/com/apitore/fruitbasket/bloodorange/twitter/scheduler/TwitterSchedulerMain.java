package com.apitore.fruitbasket.bloodorange.twitter.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author Keigo Hattori
 */
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@SpringBootApplication
public class TwitterSchedulerMain {

  public static void main(String[] args) {
    SpringApplication.run(TwitterSchedulerMain.class, args);
  }

}
