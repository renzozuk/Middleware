package dev.renzozukeram.winter.model.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Score {
    private final int paymentHistoryScore;
    private final int creditUtilizationScore;
    private final int amountScore;
    private final int availableCreditScore;

    public Score(int paymentHistoryScore, int creditUtilizationScore, int amountScore, int availableCreditScore) {
        this.paymentHistoryScore = max(min(paymentHistoryScore, 850), 300);
        this.creditUtilizationScore = max(min(creditUtilizationScore, 850), 300);
        this.amountScore = max(min(amountScore, 850), 300);
        this.availableCreditScore = max(min(availableCreditScore, 850), 300);
    }

    public int getPaymentHistoryScore() {
        return paymentHistoryScore;
    }

    public int getCreditUtilizationScore() {
        return creditUtilizationScore;
    }

    public int getCreditHistoryLengthScore(Customer customer) {
        return (int) max(min(ChronoUnit.DAYS.between(customer.getSignupDate(), LocalDate.now()), 850), 300);
    }

    public int getAmountScore() {
        return amountScore;
    }

    public int getAvailableCreditScore() {
        return availableCreditScore;
    }

    public int getFinalScore(Customer customer) {
        return (int) floor(0.4494 * paymentHistoryScore + 0.2247 * creditUtilizationScore + 0.236 * getCreditHistoryLengthScore(customer) + 0.0562 * amountScore + 0.0337 * availableCreditScore);
    }
}
