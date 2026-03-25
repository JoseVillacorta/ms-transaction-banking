package com.banking.ms_transaction.service.impl;

import com.banking.ms_transaction.dto.TransferRequest;
import com.banking.ms_transaction.entities.Transaction;
import com.banking.ms_transaction.repository.TransactionRepository;
import com.banking.ms_transaction.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WebClient webClient;

    public TransactionServiceImpl(TransactionRepository transactionRepository, WebClient.Builder webClientBuilder) {
        this.transactionRepository = transactionRepository;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Flux<Transaction> getStatementByAccount(Long accountId) {
        return transactionRepository.findBySourceAccountIdOrDestinationAccountId(accountId, accountId);
    }

    @Override
    public Mono<Transaction> processTransfer(Long fromAccountId, Long toAccountId, java.math.BigDecimal amount) {

        // Primero creamos el registro como PENDIENTE
        Transaction transaction = Transaction.builder()
                .sourceAccountId(fromAccountId)
                .destinationAccountId(toAccountId)
                .amount(amount)
                .transactionType("TRANSFER")
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        // Despues preparamos el cuerpo de la petición que ms-account esta esperando
        TransferRequest requestPayload = new TransferRequest();
        requestPayload.setFromAccountId(fromAccountId);
        requestPayload.setToAccountId(toAccountId);
        requestPayload.setAmount(amount);

        // Por ultimo guardamos, llamamos a ms-account y actualizamos segun su respuesta
        return transactionRepository.save(transaction)
                .flatMap(savedTx -> webClient.post()
                        .uri("http://ms-account/api/v1/accounts/transfer")
                        .bodyValue(requestPayload)
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                response -> Mono.error(new RuntimeException("Fondos insuficientes o cuenta destino inválida")))
                        .bodyToMono(Object.class)

                        // CASO ÉXITO: Actualiza a COMPLETED
                        .then(Mono.defer(() -> {
                            savedTx.setStatus("COMPLETED");
                            return transactionRepository.save(savedTx);
                        }))

                        // CASO FALLO: Actualiza a FAILED
                        .onErrorResume(e -> {
                            savedTx.setStatus("FAILED");
                            return transactionRepository.save(savedTx)
                                    .then(Mono.error(new RuntimeException(e.getMessage())));
                        })
                );
    }

}
