package application;

import controllers.Calculator;
import dev.renzozukeram.winter.broker.WinterApplication;
import controllers.CreditBureau;

import java.util.Set;

public class CreditBureauApplication {
    public static void main(String[] args) {
//        WinterApplication winterApplication = new WinterApplication(CreditBureau.class);
        WinterApplication winterApplication = new WinterApplication(Calculator.class, "localhost", 8080);
//        WinterApplication winterApplication = new WinterApplication(Set.of(CreditBureau.class, Calculator.class));
        winterApplication.start();
    }
}
