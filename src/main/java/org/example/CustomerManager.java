package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerManager {
    private static final CustomerManager INSTANCE = new CustomerManager();
    private final List<Customer> customers = new ArrayList<>();

    private CustomerManager(){}

    public static CustomerManager getInstance(){ return INSTANCE; }

    public Customer registerCustomer(String email, String name){
        String id = UUID.randomUUID().toString();
        Customer c = new Customer(id, email, name, "CUST-"+id.substring(0,8));
        customers.add(c);
        return c;
    }

    public Customer findByEmail(String email){
        return customers.stream().filter(c -> c.getEmail().equals(email)).findFirst().orElse(null);
    }
}

