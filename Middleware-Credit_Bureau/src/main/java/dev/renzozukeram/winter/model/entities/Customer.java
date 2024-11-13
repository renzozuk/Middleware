package dev.renzozukeram.winter.model.entities;

import dev.renzozukeram.winter.model.entities.enums.CustomerStatus;
import dev.renzozukeram.winter.patterns.VersionedKey;

import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

public class Customer {
    private final String id;
    private final String ssn;
    private final LocalDate signupDate;
    private final NavigableMap<VersionedKey, Score> keyScore;

    public Customer(String ssn) {
        if(ssn.length() != 9){
            throw new IllegalArgumentException("Invalid SSN");
        }

        this.id = UUID.nameUUIDFromBytes(ssn.getBytes()).toString();
        this.ssn = ssn;
        this.signupDate = LocalDate.now();
        this.keyScore = new ConcurrentSkipListMap<>();
    }

    public Customer(String ssn, LocalDate signupDate) {
        if(ssn.length() != 9){
            throw new IllegalArgumentException("Invalid SSN");
        }

        this.id = UUID.nameUUIDFromBytes(ssn.getBytes()).toString();
        this.ssn = ssn;
        this.signupDate = signupDate;
        this.keyScore = new ConcurrentSkipListMap<>();
    }

    public String getId() {
        return id;
    }

    public String getSsn() {
        return ssn;
    }

    public LocalDate getSignupDate() {
        return signupDate;
    }

    public int getQuantityOfKeyScores() {
        return keyScore.size();
    }

    public void updatePaymentHistoryScore(VersionedKey versionedKey, int paymentHistoryScore) {

        var lastScore = keyScore.lastEntry().getValue();

        keyScore.put(versionedKey, new Score(paymentHistoryScore,
                lastScore.getCreditUtilizationScore(),
                lastScore.getAmountScore(),
                lastScore.getAvailableCreditScore()));
    }

    public void updateCreditUtilizationScore(VersionedKey versionedKey, int creditUtilizationScore) {

        var lastScore = keyScore.lastEntry().getValue();

        keyScore.put(versionedKey, new Score(lastScore.getPaymentHistoryScore(),
                creditUtilizationScore,
                lastScore.getAmountScore(),
                lastScore.getAvailableCreditScore()));
    }

    public void updateAmountScore(VersionedKey versionedKey, int amountScore) {

        var lastScore = keyScore.lastEntry().getValue();

        keyScore.put(versionedKey, new Score(lastScore.getPaymentHistoryScore(),
                lastScore.getCreditUtilizationScore(),
                amountScore,
                lastScore.getAvailableCreditScore()));
    }

    public void updateAvailableCreditScore(VersionedKey versionedKey, int availableCreditScore) {

        var lastScore = keyScore.lastEntry().getValue();

        keyScore.put(versionedKey, new Score(lastScore.getPaymentHistoryScore(),
                lastScore.getCreditUtilizationScore(),
                lastScore.getAmountScore(),
                availableCreditScore));
    }

    public void updateScores(VersionedKey versionedKey, Score score) {
        keyScore.put(versionedKey, score);
    }

    public void updateScores(NavigableMap<VersionedKey, Score> keyScore) {
        this.keyScore.putAll(keyScore);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("========================================\n");

        result.append("ID: ").append(id).append("\n")
                .append("SSN: ").append(ssn).append("\n")
                .append("Signup date: ").append(signupDate).append("\n");

        keyScore.forEach((key, value) -> result.append(key).append(": ")
                .append("[Score: ").append(value.getFinalScore(this)).append(" (").append(CustomerStatus.fromScore(value.getFinalScore(this)).toString().replace("_", " ")).append(")]\n"));

        result.append("========================================\n");

        return result.toString();
    }

    public String getDetailedReport() {
        StringBuilder result = new StringBuilder("========================================\n");

        result.append("ID: ").append(id).append("\n")
                .append("SSN: ").append(ssn).append("\n")
                .append("Signup date: ").append(signupDate).append("\n");

        keyScore.forEach((key, value) -> result.append(key).append(": ")
                .append("[Payment History Score: ").append(value.getPaymentHistoryScore()).append("] ")
                .append("[Credit Utilization Score: ").append(value.getCreditUtilizationScore()).append("] ")
                .append("[Credit History Length Score: ").append(value.getCreditHistoryLengthScore(this)).append("] ")
                .append("[Amount Score: ").append(value.getAmountScore()).append("] ")
                .append("[Available Credit Score: ").append(value.getAvailableCreditScore()).append("] ")
                .append("[Final Score: ").append(value.getFinalScore(this)).append(" (").append(CustomerStatus.fromScore(value.getFinalScore(this)).toString().replace("_", " ")).append(")]\n"));

        result.append("========================================\n");

        return result.toString();
    }
}
