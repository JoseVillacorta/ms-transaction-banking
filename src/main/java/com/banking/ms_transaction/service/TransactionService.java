package com.banking.ms_transaction.service;

import com.banking.ms_transaction.entities.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

public interface TransactionService {
    // Para ver el historial de movimientos de una cuenta (Estado de Cuenta)
    Flux<Transaction> getStatementByAccount(Long accountId);

    // Para orquestar la transferencia completa
    Mono<Transaction> processTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
