package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerManager {
    private static final CustomerManager INSTANCE = new CustomerManager();
    private final List<Customer> customers = new ArrayList<>();

    private CustomerManager(){}

    public static CustomerManager getInstance(){ return INSTANCE; }

    public Customer registerCustomer(String email, String name, String passwordHash){
        String id = UUID.randomUUID().toString();
        Customer c = new Customer(id, email, name, passwordHash);
        customers.add(c);
        return c;
    }

    public void topUpCustomer(String email, double amount) {
        Customer customer = findByEmail(email);
        customer.topUp(amount);
    }

    public Customer login(String email, String passwordHash) {
        return customers.stream()
                .filter(c -> c.getEmail().equals(email)
                        && c.getPasswordHash().equals(passwordHash))
                .findFirst()
                .orElse(null); // oder Exception werfen
    }

    public Customer findByEmail(String email){
        return customers.stream().filter(c -> c.getEmail().equals(email)).findFirst().orElse(null);
    }
}

