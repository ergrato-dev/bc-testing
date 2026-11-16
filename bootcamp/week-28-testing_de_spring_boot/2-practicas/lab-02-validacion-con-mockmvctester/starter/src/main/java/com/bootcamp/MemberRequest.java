package com.bootcamp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Cuerpo de POST /api/members. Cada restricción lleva su mensaje en inglés: sin él, Hibernate
 * Validator lo traduce al idioma del sistema y los tests dependerían del equipo donde se ejecutan.
 */
public record MemberRequest(
    @NotBlank(message = "is required") String name,
    @NotBlank(message = "is required") @Email(message = "must be a valid email") String email,
    @Min(value = 1900, message = "must be 1900 or later") @Max(value = 2020, message = "must be 2020 or earlier") int birthYear) {
}
