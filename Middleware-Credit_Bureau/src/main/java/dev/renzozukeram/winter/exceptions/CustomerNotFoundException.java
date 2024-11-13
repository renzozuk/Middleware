package dev.renzozukeram.winter.exceptions;

import dev.renzozukeram.winter.model.entities.Customer;

import java.io.Serial;

public class CustomerNotFoundException extends ObjectNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomerNotFoundException(String customerSsn) {
        super(Customer.class, "It wasn't possible to find any customer with the given SSN.");
    }

    public CustomerNotFoundException(String customerSsn, Throwable cause) {
        super(Customer.class, "It wasn't possible to find any customer with the given SSN.", cause);
    }
}
