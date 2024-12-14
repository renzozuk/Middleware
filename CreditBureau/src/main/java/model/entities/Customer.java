package model.entities;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Customer {

    private String id;
    private String ssn;
    private int score;

    public Customer() {
    }

    public Customer(String id, String ssn, int score) {

        if(ssn.length() != 9){
            throw new IllegalArgumentException("Invalid SSN");
        }

        this.id = id;
        this.ssn = ssn;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
        return "Customer{" +
                "id='" + id + '\'' +
                ", ssn='" + ssn + '\'' +
                ", score=" + score +
                '}';
    }
}
