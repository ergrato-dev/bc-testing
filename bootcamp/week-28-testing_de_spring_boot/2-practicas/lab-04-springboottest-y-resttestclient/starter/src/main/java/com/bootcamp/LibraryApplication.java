package com.bootcamp;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    /** La fecha de hoy. Los tests la sustituyen por un reloj que controlan. */
    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
