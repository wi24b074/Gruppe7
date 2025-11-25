package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerAccount {
    private final String accountId;
    private BigDecimal balance = BigDecimal.ZERO;
    private final List<TopUp> topUps = new ArrayList<>();

    public CustomerAccount(String accountId){
        this.accountId = accountId;
    }

    public String getAccountId(){ return accountId; }
    public BigDecimal getBalance(){ return balance; }

    public void credit(java.math.BigDecimal amount){
        balance = balance.add(amount);
        topUps.add(new TopUp("TU-"+System.currentTimeMillis(), amount));
    }

    public void debit(java.math.BigDecimal amount){
        balance = balance.subtract(amount);
    }

    public List<TopUp> getTopUps(){ return List.copyOf(topUps); }
}
