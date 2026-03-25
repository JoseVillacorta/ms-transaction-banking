package com.banking.ms_transaction.router;

import com.banking.ms_transaction.handler.TransactionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TransactionRouter {

    @Bean
    public RouterFunction<ServerResponse> transactionRoutes(TransactionHandler handler) {
        return route(POST("/api/v1/transactions"), handler::makeTransfer)
                .andRoute(GET("/api/v1/transactions/statement/{accountId}"), handler::getStatement);
    }
}
