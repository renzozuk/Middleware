package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.CustomerDao;
import model.entities.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoJDBC implements CustomerDao {

    private Connection conn;

    public CustomerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Customer> findAllCustomers() {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM customers ORDER BY id");

            rs = ps.executeQuery();

            List<Customer> customers = new ArrayList<>();

            while (rs.next()) {
                customers.add(instantiateCustomer(rs));
            }

            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer findCustomerById(String id) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM customers WHERE id = ?");
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return instantiateCustomer(rs);
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Customer saveCustomer(Customer customer) {

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("INSERT INTO customers VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, customer.getId());
            ps.setString(2, customer.getSsn());
            ps.setInt(3, customer.getScore());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    String generatedId = rs.getString(1);
                    customer.setId(generatedId);
                }

                DB.closeResultSet(rs);

                return customer;
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public Customer updateCustomer(Customer customer, String id) {

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("UPDATE customers SET ssn = ?, score = ? WHERE id = ?");

            ps.setString(1, customer.getSsn());
            ps.setInt(2, customer.getScore());
            ps.setString(3, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                customer.setId(id);
                return customer;
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void deleteCustomer(String id) {

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("DELETE FROM customers WHERE id = ?");

            ps.setString(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closeStatement(ps);
        }
    }

    private Customer instantiateCustomer(ResultSet rs) throws SQLException {
        return new Customer(rs.getString("id"), rs.getString("ssn"), rs.getInt("score"));
    }
}
