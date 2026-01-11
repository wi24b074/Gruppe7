package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CustomerManager {
    private static final CustomerManager INSTANCE = new CustomerManager();
    private final List<Customer> customers = new ArrayList<>();

    private CustomerManager(){}

    public static CustomerManager getInstance(){ return INSTANCE; }

    public Customer registerCustomer(String email, String name, String password){
        String id = UUID.randomUUID().toString();
        Customer c = new Customer(id, email, name, password);
        customers.add(c);
        return c;
    }

    public void topUpCustomer(String email, double amount) {
        Customer customer = findByEmail(email);
        customer.topUp(amount);
    }

    public Customer login(String email, String passwordHash) {
        return customers.stream()
                .filter(c -> Objects.equals(c.getEmail(), email))
                .filter(c -> Objects.equals(c.getPasswordHash(), passwordHash))
                .findFirst()
                .orElse(null);
    }

    public Customer findByEmail(String email){
        return customers.stream()
                .filter(c -> Objects.equals(c.getEmail(), email))
                .findFirst()
                .orElse(null);
    }

    public void clear() {
        customers.clear();
    }
}

