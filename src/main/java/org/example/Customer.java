package org.example;

public class Customer extends User {
    private final String customerNumber;
    private final CustomerAccount account;

    public Customer(String userId, String email, String name, String customerNumber) {
        super(userId, email, name);
        this.customerNumber = customerNumber;
        this.account = new CustomerAccount("ACC-"+userId);
    }

    public String getCustomerNumber() { return customerNumber; }
    public CustomerAccount getAccount() { return account; }
    public String getCustomerId() {
        return getUserId();
    }
}
