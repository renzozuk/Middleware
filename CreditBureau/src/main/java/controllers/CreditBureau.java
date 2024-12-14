package controllers;

import dev.renzozukeram.winter.annotations.*;
import dev.renzozukeram.winter.message.ResponseEntity;
import model.dao.CustomerDao;
import model.dao.DaoFactory;
import model.entities.Customer;

@RequestMapping("/creditbureau")
public class CreditBureau {

    private final CustomerDao customerDao = DaoFactory.createCustomerDao();

    @Get
    public ResponseEntity findAllCustomers() {
        return new ResponseEntity(200, customerDao.findAllCustomers());
    }

    @Get("/id")
    public ResponseEntity findCustomerById(String id) {

        var response = customerDao.findCustomerById(id);

        if (response == null ){
            return new ResponseEntity(404, "Customer not found.");
        } else {
            return new ResponseEntity(200, response);
        }
    }

    @Post
    public ResponseEntity createCustomer(@RequestBody Customer customer) {

        if (customer.getSsn().length() != 9) {
            return new ResponseEntity(400, "The customer ssn must be 9 letters long.");
        }

        customer.setId();
        return new ResponseEntity(201, customerDao.saveCustomer(customer));
    }

    @Put("/id")
    public ResponseEntity updateCustomer(@RequestBody Customer customer, String id) {
        return new ResponseEntity(200, customerDao.updateCustomer(customer, id));
    }

    @Delete("/id")
    public ResponseEntity deleteCustomer(String id) {
        customerDao.deleteCustomer(id);
        return new ResponseEntity(204);
    }

    @Get("/sum")
    public ResponseEntity sum(int a, double b) {
        System.out.println("The sum of " + a + " and " + b + " is " + (a + b));
        return new ResponseEntity(200, (a + b));
    }
}
