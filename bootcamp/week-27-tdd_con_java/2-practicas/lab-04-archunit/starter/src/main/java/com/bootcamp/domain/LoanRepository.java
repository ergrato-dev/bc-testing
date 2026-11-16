package com.bootcamp.domain;

import java.util.Optional;

/** Dónde se guardan los préstamos. El dominio define la interfaz; la infraestructura la implementa. */
public interface LoanRepository {

    Optional<Loan> findById(String id);

    void save(Loan loan);
}
