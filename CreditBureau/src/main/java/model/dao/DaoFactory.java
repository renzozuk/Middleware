package model.dao;

import db.DB;
import model.dao.impl.CustomerDaoJDBC;

public class DaoFactory {

    public static CustomerDao createCustomerDao() {
        return new CustomerDaoJDBC(DB.getConnection());
    }
}
