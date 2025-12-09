package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountEntry {
    private final LocalDateTime timestamp;
    private final BigDecimal amount;
    private final String type;
    private final String description;
    private final BigDecimal balanceAfter;

    public AccountEntry(LocalDateTime timestamp,
                        BigDecimal amount,
                        String type,
                        String description,
                        BigDecimal balanceAfter) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.balanceAfter = balanceAfter;
    }


}

