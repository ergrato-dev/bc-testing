package com.bootcamp;

// PASO 3
// import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
// PASO 1
// import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
// PASO 4
// import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
// PASO 2
// import java.time.LocalDate;

/** Reglas de arquitectura: fallan si el código de src/main deja de cumplirlas. */
@AnalyzeClasses(packages = "com.bootcamp", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    // PASO 1: el dominio no depende de los servicios ni de la infraestructura
    // @ArchTest
    // static final ArchRule domainDependsOnNothingElse = noClasses()
    //     .that().resideInAPackage("..domain..")
    //     .should().dependOnClassesThat().resideInAnyPackage("..service..", "..infrastructure..");

    // PASO 2: nadie pide la fecha al sistema; la fecha llega por un Clock
    // @ArchTest
    // static final ArchRule todayComesFromAClock = noClasses()
    //     .should().callMethod(LocalDate.class, "now")
    //     .because("la fecha de hoy llega por un Clock para que los tests la controlen");

    // PASO 3: los servicios reciben sus dependencias por el constructor y no las cambian
    // @ArchTest
    // static final ArchRule serviceFieldsAreFinal = fields()
    //     .that().areDeclaredInClassesThat().resideInAPackage("..service..")
    //     .should().beFinal();

    // PASO 4: las capas, en una sola regla
    // @ArchTest
    // static final ArchRule layersAreRespected = layeredArchitecture()
    //     .consideringOnlyDependenciesInLayers()
    //     .layer("Domain").definedBy("..domain..")
    //     .layer("Service").definedBy("..service..")
    //     .layer("Infrastructure").definedBy("..infrastructure..")
    //     .whereLayer("Service").mayNotBeAccessedByAnyLayer()
    //     .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer();
}
