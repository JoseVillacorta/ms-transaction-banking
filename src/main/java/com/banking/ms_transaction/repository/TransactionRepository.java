package com.banking.ms_transaction.repository;

import com.banking.ms_transaction.entities.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
    // Buscar historial de una cuenta especifica (sirve para mostrar el "Estado de Cuenta" del cliente)
    Flux<Transaction> findBySourceAccountIdOrDestinationAccountId(Long sourceAccountId, Long destinationAccountId);
}
