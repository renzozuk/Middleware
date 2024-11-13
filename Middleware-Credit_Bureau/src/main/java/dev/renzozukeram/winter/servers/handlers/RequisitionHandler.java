package dev.renzozukeram.winter.servers.handlers;

import java.time.LocalDate;
import java.util.List;

public class RequisitionHandler {
//    private static final CustomerDao customerDao = DaoFactory.createCustomerDao();
//    private static final ScoreDao scoreDao = DaoFactory.createScoreDao();

    public static String processRequisition(List<String> tokens) {
        switch (tokens.get(0).trim().toLowerCase()) {
//            case "are you alive?":
//                return "Yes, I'm alive!";
//            case "get":
//                try {
//                    Customer customer = customerDao.findBySsn(tokens.get(1).trim());
//                    customer.updateScores(scoreDao.findByCustomerSsn(tokens.get(1).trim()));
//                    return customer.toString();
//                } catch (NullPointerException e) {
//                    return  "It wasn't possible to find a user with the typed SSN.\n";
//                }
//            case "getdetailed", "get_detailed":
//                try {
//                    Customer customer = customerDao.findBySsn(tokens.get(1).trim());
//                    customer.updateScores(scoreDao.findByCustomerSsn(tokens.get(1).trim()));
//                    return customer.getDetailedReport();
//                } catch (NullPointerException e) {
//                    return  "It wasn't possible to find a user with the typed SSN.\n";
//                }
//            case "getlast", "get_last":
//                try {
//                    Customer customer = customerDao.findBySsn(tokens.get(1).trim());
//                    customer.updateScores(scoreDao.findLastScoreByCustomerSsn(tokens.get(1).trim()));
//                    return customer.toString();
//                } catch (NullPointerException e) {
//                    return  "It wasn't possible to find a user with the typed SSN.\n";
//                }
//            case "getdetailedlast", "get_detailed_last":
//                try {
//                    Customer customer = customerDao.findBySsn(tokens.get(1).trim());
//                    customer.updateScores(scoreDao.findLastScoreByCustomerSsn(tokens.get(1).trim()));
//                    return customer.getDetailedReport();
//                } catch (NullPointerException e) {
//                    return  "It wasn't possible to find a user with the typed SSN.\n";
//                }
//            case "create", "post":
//                try {
//                    if (tokens.size() >= 3) {
//                        customerDao.insert(new Customer(tokens.get(1).trim(), LocalDate.parse(tokens.get(2).trim())));
//                    } else {
//                        customerDao.insert(new Customer(tokens.get(1).trim()));
//                    }
//
//                    return String.format("An account [SSN: %s] was created successfully.\n", tokens.get(1));
//                } catch (DbException e) {
//                    return String.format("It wasn't possible to create an account [SSN: %s]. Please, check if there's already an account with that SSN.\n", tokens.get(1));
//                } catch (IllegalArgumentException e) {
//                    return  "The SSN is invalid.\n";
//                }
//            case "updatescore", "update_score":
//                try {
//                    scoreDao.insert(tokens.get(1).trim(), new Score(Integer.parseInt(tokens.get(2).trim()),
//                            Integer.parseInt(tokens.get(3).trim()),
//                            Integer.parseInt(tokens.get(4).trim()),
//                            Integer.parseInt(tokens.get(5).trim())));
//                    return String.format("User score [SSN: %s] updated successfully.\n", tokens.get(1).trim());
//                } catch (NullPointerException e) {
//                    return "It wasn't possible to find a user with the typed SSN.\n";
//                }
//            case "delete":
//                try {
//                    customerDao.deleteBySsn(tokens.get(1).trim());
//                    return String.format("User [SSN: %s] deleted successfully.\n", tokens.get(1).trim());
//                } catch (NullPointerException ignored) {}
            default:
                return "Invalid request.";

        }
    }
}
