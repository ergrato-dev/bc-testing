package com.bootcamp;

// PASO 3: descomenta la extensión entera (imports incluidos).
//
// import java.time.Clock;
// import java.time.LocalDate;
// import java.time.ZoneOffset;
// import org.junit.jupiter.api.extension.ExtensionContext;
// import org.junit.jupiter.api.extension.ParameterContext;
// import org.junit.jupiter.api.extension.ParameterResolver;
//
// /** Inyecta un Clock fijo en cualquier parámetro de tipo Clock. */
// public final class FixedClockExtension implements ParameterResolver {
//
//     private final LocalDate today;
//
//     public FixedClockExtension() {
//         this(LocalDate.of(2026, 3, 16));
//     }
//
//     private FixedClockExtension(LocalDate today) {
//         this.today = today;
//     }
//
//     public static FixedClockExtension on(LocalDate today) {
//         return new FixedClockExtension(today);
//     }
//
//     @Override
//     public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
//         return parameter.getParameter().getType() == Clock.class;
//     }
//
//     @Override
//     public Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
//         return Clock.fixed(today.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
//     }
// }
