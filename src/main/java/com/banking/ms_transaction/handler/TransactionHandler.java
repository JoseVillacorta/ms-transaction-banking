package com.banking.ms_transaction.handler;


import com.banking.ms_transaction.dto.ApiResponse;
import com.banking.ms_transaction.dto.TransferRequest;
import com.banking.ms_transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionHandler {

    private final TransactionService transactionService;

    // Para ver los movimientos de retiro/depósito/transferencia de una persona
    public Mono<ServerResponse> getStatement(ServerRequest request) {
        Long accountId = Long.valueOf(request.pathVariable("accountId"));
        return transactionService.getStatementByAccount(accountId)
                .collectList()
                .flatMap(txs -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.success(txs, "Historial obtenido", HttpStatus.OK.value())));
    }

    // El endpoint principal para recibir las órdenes de transferencia
    public Mono<ServerResponse> makeTransfer(ServerRequest request) {
        return request.bodyToMono(TransferRequest.class)
                .flatMap(req -> transactionService.processTransfer(req.getFromAccountId(), req.getToAccountId(), req.getAmount()))
                .flatMap(tx -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.success(tx, "Trámite de transferencia cerrado con estado: " + tx.getStatus(), HttpStatus.CREATED.value())))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value())));
    }

}
