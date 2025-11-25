package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TopUp {
    private final String topUpId;
    private final LocalDateTime dateTime;
    private final BigDecimal amount;

    public TopUp(String topUpId, BigDecimal amount){
        this.topUpId = topUpId;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
    }

    public String getTopUpId(){ return topUpId; }
    public LocalDateTime getDateTime(){ return dateTime; }
    public BigDecimal getAmount(){ return amount; }
}
