package dev.renzozukeram.winter.model.entities;

import dev.renzozukeram.winter.annotations.*;
import dev.renzozukeram.winter.exceptions.CustomerNotFoundException;
import dev.renzozukeram.winter.patterns.VersionedKey;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

public class CreditBureau {
    private final Set<Customer> customers;

    public CreditBureau(Set<Customer> customers) {
        this.customers = customers;
    }

    @Get(router = "get")
    public Customer getCustomer(String ssn) {
        return customers.stream().filter(c -> c.getSsn().equals(ssn)).findFirst().orElseThrow(() -> new CustomerNotFoundException(ssn));
    }

    @Post(router = "create")
    public void addCustomer(String ssn) {
        customers.add(new Customer(ssn));
    }

    @Post(router = "create")
    public void addCustomer(String ssn, LocalDate signupDate) {
        customers.add(new Customer(ssn, signupDate));
    }

    @Put(router = "put")
    public void updateCustomer(String ssn, int paymentUtilizationScore, int creditUtilizationScore, int amountScore, int availableCreditScore) {

        var customer = customers.stream().filter(c -> c.getSsn().equals(ssn)).findFirst().orElseThrow(() -> new CustomerNotFoundException(ssn));

        customer.updateScores(new VersionedKey(Instant.now().toString(), customer.getQuantityOfKeyScores() + 1),
                new Score(paymentUtilizationScore, creditUtilizationScore, amountScore, availableCreditScore));
    }

    @Patch(router = "patch")
    public void patchCustomer(String ssn, String chosenScore, int newScore) {

        var customer = customers.stream().filter(c -> c.getSsn().equals(ssn)).findFirst().orElseThrow(() -> new CustomerNotFoundException(ssn));

        switch (chosenScore.toLowerCase().replace(" ", "").replace("_", "")) {
            case "paymenthistory", "paymenthistoryscore":
                customer.updatePaymentHistoryScore(new VersionedKey(Instant.now().toString(), customer.getQuantityOfKeyScores() + 1), newScore);
            case "creditutilization", "creditutilizationscore":
                customer.updateCreditUtilizationScore(new VersionedKey(Instant.now().toString(), customer.getQuantityOfKeyScores() + 1), newScore);
            case "amount", "amountscore":
                customer.updateAmountScore(new VersionedKey(Instant.now().toString(), customer.getQuantityOfKeyScores() + 1), newScore);
            case "availablecredit", "availablecreditscore":
                customer.updateAvailableCreditScore(new VersionedKey(Instant.now().toString(), customer.getQuantityOfKeyScores() + 1), newScore);
        }
    }

    @Delete(router = "delete")
    public void removeCustomer(String ssn) {
        var customer = customers.stream().filter(c -> c.getSsn().equals(ssn)).findFirst().orElseThrow(() -> new CustomerNotFoundException(ssn));
        customers.remove(customer);
    }
}
