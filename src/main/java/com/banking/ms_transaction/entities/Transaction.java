package com.banking.ms_transaction.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("transactions")
public class Transaction {
    @Id
    private Long id;

    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;
    private String transactionType; // TRANSFER, DEPOSIT, WITHDRAWAL
    private String status;  // PENDING, COMPLETED, FAILED
    private LocalDateTime createdAt;
}
