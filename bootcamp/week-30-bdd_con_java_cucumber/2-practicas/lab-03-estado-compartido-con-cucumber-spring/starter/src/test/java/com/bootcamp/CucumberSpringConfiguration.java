package com.bootcamp;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/** La única clase con @CucumberContextConfiguration: de ella sale el contexto de todos los escenarios. */
@CucumberContextConfiguration
@SpringBootTest
public class CucumberSpringConfiguration {
}
