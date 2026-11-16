package com.bootcamp;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Fitness functions del proyecto (teoría 04). Analizan solo src/main: si el código deja de
 * cumplir una regla, mvn test falla igual que con cualquier otro test.
 */
@AnalyzeClasses(packages = "com.bootcamp", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule todayComesFromAClock = noClasses()
        .should().callMethod(LocalDate.class, "now")
        .orShould().callMethod(LocalDateTime.class, "now")
        .orShould().callMethod(Instant.class, "now")
        .because("la fecha llega por un Clock inyectado para que los tests la controlen");

    @ArchTest
    static final ArchRule servicesKeepTheirDependencies = fields()
        .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("Service")
        .should().beFinal()
        .because("las dependencias llegan por el constructor y no cambian");

    @ArchTest
    static final ArchRule productionCodeDoesNotUseTestLibraries = noClasses()
        .should().dependOnClassesThat().resideInAnyPackage("org.mockito..", "org.junit..", "org.assertj..")
        .because("Mockito, JUnit y AssertJ solo existen en el scope test");
}
