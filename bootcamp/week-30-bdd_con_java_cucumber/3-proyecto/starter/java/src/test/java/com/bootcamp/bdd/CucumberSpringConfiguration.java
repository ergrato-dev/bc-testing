package com.bootcamp.bdd;

import com.bootcamp.TestcontainersConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * Un solo contexto para todos los escenarios: la API en un puerto aleatorio, PostgreSQL en un
 * contenedor y un reloj que los escenarios controlan.
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestcontainersConfiguration.class, CucumberSpringConfiguration.ClockConfiguration.class})
@AutoConfigureRestTestClient
public class CucumberSpringConfiguration {

    @TestConfiguration(proxyBeanMethods = false)
    static class ClockConfiguration {

        /** @Primary: los servicios reciben este reloj en lugar del Clock del sistema de ServiceConfig. */
        @Bean
        @Primary
        TestClock testClock() {
            return new TestClock();
        }
    }
}
