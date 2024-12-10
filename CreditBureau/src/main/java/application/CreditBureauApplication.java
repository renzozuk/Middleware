package application;

import dev.renzozukeram.winter.broker.WinterApplication;
import entities.CreditBureau;

public class CreditBureauApplication {
    public static void main(String[] args) {
        WinterApplication winterApplication = new WinterApplication(CreditBureau.class);
    }
}
