package model.dao;

import model.entities.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> findAllCustomers();
    Customer findCustomerById(String id);
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(Customer customer, String id);
    void deleteCustomer(String id);
}
