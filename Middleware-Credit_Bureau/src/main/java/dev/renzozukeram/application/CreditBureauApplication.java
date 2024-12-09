package dev.renzozukeram.application;

import dev.renzozukeram.winter.broker.WinterApplication;

public class CreditBureauApplication {
    public static void main(String[] args) {
        new WinterApplication(CreditBureau.class);
    }
}
