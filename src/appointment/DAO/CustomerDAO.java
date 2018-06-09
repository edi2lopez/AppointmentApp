
package appointment.DAO;

import appointment.model.Customer;
import appointment.model.CustomerRecords;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author el
 */
public class CustomerDAO {
    
    // private static final LinkedHashSet<Customer> customers = new LinkedHashSet<Customer>();
    private static final List<Customer> customers = new ArrayList<Customer>();
    private static final ObservableList<Customer> customerList = FXCollections.observableArrayList(customers);
    
    //*******************************
    //Get last row from customer table
    //*******************************
    public static int getLastCustomerId() throws SQLException, ClassNotFoundException {
        
        int nextCustomerId;
        String selectStmt = "SELECT customerId FROM customer;";
        
        //Execute SELECT statement
        try {
            ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
            
            // check if resulset is empty, assign 0 as the first primary key in the customer table
            if(rs.next() == false) {
                System.out.println("ResultSet is empty in Java");
                nextCustomerId = 0;
                return nextCustomerId;
            } else {
                rs.last();
                nextCustomerId = rs.getInt("customerId");
                return ++nextCustomerId;
            }
           
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            throw e;
        }        
    }

    //*******************************
    //SELECT Customers
    //*******************************
    public static ObservableList<Customer> getCustomers() throws SQLException, ClassNotFoundException {
        
        //Declare a SELECT statement
        String selectStmt = "SELECT a.customerId, a.customerName, b.address, b.postalCode, b.phone, c.city, d.country \n" +
                            "FROM customer a, address b, city c, country d \n" +
                            "WHERE a.addressId = b.addressId AND b.cityId = c.cityId AND c.countryId = d.countryId;";

        System.out.println("SQL: " + selectStmt);
        
        //Execute SELECT statement
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            return getCustomerList(rsEmps);
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            throw e;
        }
    }
    
    //Select * from customer operation
    private static ObservableList<Customer> getCustomerList(ResultSet rs) throws SQLException, ClassNotFoundException {

        while (rs.next()) {
            Customer customer = new Customer();
            customer.setId(rs.getInt("customerId"));
            customer.setName(rs.getString("customerName"));
            customer.setPhone(rs.getString("phone"));
            customer.setAddress(rs.getString("address"));
            customer.setPostalCode(rs.getString("postalCode"));
            customer.setCity(rs.getString("city"));
            customer.setCountry(rs.getString("country"));
            
            //Add employee to the ObservableList
            customerList.add(customer);
            
            // Different List for Customer Table in the appointment dialog to avoid duplicating the SQL statement
            CustomerRecords.addCustomer(customer);
        }

        //return FXCollections.observableArrayList(customers);
        return customerList;
        
    }

    //*************************************
    //UPDATE Customers
    //*************************************
    public static void updateCustomer (Customer customer) throws SQLException, ClassNotFoundException {
        
        int customerId = customer.getId();
        String customerName = customer.getName();
        String phone = customer.getPhone();
        String address = customer.getAddress();
        
        // Get logged in user name
        String loggedInUser = LoginDAO.getLoggedInUser();
                
        //Declare an UPDATE statement
        String updateStmt = 
                        " UPDATE customer c, address a\n" +
                        " SET\n" +
                        " c.customerName = '" + customerName + "',\n" +
                        " c.lastUpdate = NOW(),\n" +
                        " c.lastUpdateBy = '" + loggedInUser + "',\n" +
                        " a.phone = '" + phone + "',\n" +
                        " a.address = '" + address + "'\n" +
                        " WHERE c.customerId = " + customerId + "\n" +
                        " AND c.addressId=a.addressId; \n";
        
        System.out.println("SQL :" + updateStmt);
        
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);  
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

     
    //*************************************
    //INSERT a customer
    //*************************************
    public static void insertCustomer (Customer customer) throws SQLException, ClassNotFoundException {
            
        int customerId = customer.getId();
        String customerName = customer.getName();
        String phone = customer.getPhone();
        int addressId = getLastAddressId();
        String address = customer.getAddress();
        String postalCode = customer.getPostalCode();

        String city = customer.getCity();
        int cityId = getCityId(city);

        int countryId = 0;
        String country = customer.getCountry();
        if (Stream.of("London", "New York", "Phoenix").anyMatch(c -> city.equalsIgnoreCase(c))) {
            countryId =  1;
        } else if(city.equalsIgnoreCase("London")) {
            countryId = 2;
        }
        
        // Get logged in user name
        String loggedInUser = LoginDAO.getLoggedInUser();
        
        //Declare a INSERT statement
        String updateStmt1 =
                        " INSERT IGNORE INTO customer\n" +
                        " (CustomerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)\n" +
                        " VALUES\n" +
                        " ("+customerId+", '"+customerName+"', "+addressId+", 1, CURDATE(), '"+loggedInUser+"', NOW(), '"+loggedInUser+"');";
                        
        String updateStmt2 =                
                        " INSERT INTO address\n" +
                        " (addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)\n" +
                        " VALUES\n" +
                        " ("+addressId+", '"+address+"','', "+cityId+", '"+postalCode+"', '"+phone+"', CURDATE(), '"+loggedInUser+"', '"+loggedInUser+"');";     
        
        System.out.println("SQL :" + updateStmt1);
        System.out.println("SQL :" + updateStmt2);
        
        //Execute INSERT operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt1);
            DBUtil.dbExecuteUpdate(updateStmt2);
            
            //customers.add(customer);
            customerList.add(customer);
            
            // Different List for Customer Table in the appointment dialog to avoid duplicating the SQL statement
            CustomerRecords.addCustomer(customer);

        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
 
    //*************************************
    //DELETE a customer
    //*************************************
    public static void deleteCustomerById (int customerId) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt =
                        " DELETE FROM customer\n" +
                        " WHERE customerId ="+ customerId +";";
 
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
            customerList.removeIf(customer -> customer.getId() == customerId);
            
            // Different List for Customer Table in the appointment dialog to avoid duplicating the SQL statement
            CustomerRecords.removeCustomer(customerId);
            
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
    
    //*******************************
    //Get last row from address table
    //*******************************
    public static int getLastAddressId() throws SQLException, ClassNotFoundException {
        
        int nextAddressId;
        String selectStmt = "SELECT addressId FROM address;";
        
        //Execute SELECT statement
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            
            // check if resulset is empty, assign 0 as the first primary key in the adddress table
            if(rsEmps.next() == false) {
                System.out.println("ResultSet is empty in Java");
                nextAddressId = 0;
                return nextAddressId;
            } else {
                rsEmps.last();
                nextAddressId = rsEmps.getInt("addressId");
                return ++nextAddressId;
            }
            
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            throw e;
        }
    }
    
    //*******************************
    //SELECT Cities
    //*******************************
    public static ObservableList<String> getCities() throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT city FROM city;";

        //Execute SELECT statement
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            return getCitiesList(rsEmps);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }
    
    //Select * from city operation    
    private static ObservableList<String> getCitiesList(ResultSet rs) throws SQLException, ClassNotFoundException {
 
        LinkedHashSet<String> uniqueStrings = new LinkedHashSet<String>();
        
        while (rs.next()) {
            uniqueStrings.add(rs.getString("city"));
        }

        List<String> asList = new ArrayList<String>(uniqueStrings);
        return FXCollections.observableArrayList(asList);
        
    }

    //*******************************
    //SELECT Countries
    //*******************************
    public static ObservableList<String> getCountries() throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT country FROM country;";

        //Execute SELECT statement
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            return getCountriesList(rsEmps);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }
    
    //Select * from customer operation    
    private static ObservableList<String> getCountriesList(ResultSet rs) throws SQLException, ClassNotFoundException {

        LinkedHashSet<String> uniqueStrings = new LinkedHashSet<>();
        
        while (rs.next()) {
            uniqueStrings.add(rs.getString("country"));
        }
        
        List<String> asList = new ArrayList<>(uniqueStrings);
        return FXCollections.observableArrayList(asList);
        
    }
    
    // Get City Id already set in the database
    static int getCityId(String city) {

        switch (city) {
            case "Phoenix":
                return 1;
            case "New York":
                return 2;
            case "London":
                return 3;
        }
        return 0;
    }
    
    
}
