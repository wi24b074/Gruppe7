package org.example;

public class Customer extends User {
    private final String customerNumber;
    private  CustomerAccount account;
    private double balance;

    public Customer(String userId, String email, String name, String customerNumber) {
        super(userId, email, name);
        this.customerNumber = customerNumber;
        this.account = new CustomerAccount("ACC-"+userId);
    }

    public void topUp(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getCustomerNumber() { return customerNumber; }
    public CustomerAccount getAccount() { return account; }
    public String getCustomerId() {
        return getUserId();
    }

    public void setAccount(CustomerAccount account) { this.account = account; }
}
