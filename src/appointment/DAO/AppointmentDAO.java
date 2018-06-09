
package appointment.DAO;

import appointment.model.Appointment;
import appointment.model.CustomerRecords;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author el
 */
public class AppointmentDAO {

    private static final List<Appointment> appointments = new ArrayList<Appointment>();
    private static final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(appointments);
        
    //*******************************
    //Get last row from appointment table
    //*******************************
    public static int getLastAppointmentId() throws SQLException, ClassNotFoundException {
        
        int nextAppointmentId;
        String selectStmt = "SELECT appointmentId FROM appointment;";
        
        //Execute SELECT statement
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            rsEmps.last();
            nextAppointmentId = rsEmps.getInt("appointmentId");
            return ++nextAppointmentId;
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            throw e;
        }
    }  
    
    //*******************************
    //SELECT an Appointment
    //*******************************
    public static Appointment searchAppointment (String appointmentyId) throws SQLException, ClassNotFoundException {
        
        // Declare a SELECT statement
        String selectStmt = //"SELECT * FROM appointment WHERE appointmentyId="+appointmentyId;
                            "SELECT a.customerId, b.appointmentId, b.title, b.start, b.end \n" +
                            "FROM customer a, appointment b \n" +
                            "WHERE a.customerId = b.customerId";
 
        System.out.println("SQL: " + selectStmt);
        
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsAppointment = DBUtil.dbExecuteQuery(selectStmt);
 
            //Send ResultSet to the getCustomerFromResultSet method and get customer object
            Appointment appointment = getAppointmentFromResultSet(rsAppointment);
 
            //Return customer object
            return appointment;
        } catch (SQLException e) {
            System.out.println("While searching a customer with " + appointmentyId + " id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }
    
    //Use ResultSet from DB as parameter and set Customer Object's attributes and return customer object.
    private static Appointment getAppointmentFromResultSet(ResultSet rs) throws SQLException {
        Appointment appointment = null;
        if (rs.next()) {
            appointment = new Appointment();
            appointment.setAppointmentName(rs.getString("description"));
            //appointment.setDateAndTime(rs.getTime("start").toLocalTime());

        }
        return appointment;
    }
    
        
    //*******************************
    //SELECT Appointments
    //*******************************
    public static ObservableList<Appointment> getAppointments() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM appointment";
 
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
 
            //Send ResultSet to the getEmployeeList method and get employee object
            return getAppointmentList(rsEmps);

        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }
    
    //Select * from customer operation
    private static ObservableList<Appointment> getAppointmentList(ResultSet rs) throws SQLException, ClassNotFoundException {
                 
        while (rs.next()) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(rs.getInt("appointmentId"));
            appointment.setAppointmentName(rs.getString("title"));
            appointment.setAppointmentDate(rs.getDate("start").toLocalDate());
            appointment.setAppointmentTime(rs.getTime("start").toLocalTime());
                        
            LocalTime appointmetStart = rs.getTime("start").toLocalTime();
            LocalTime appointmetEnd = rs.getTime("end").toLocalTime();            
            appointment.setDuration((int) MINUTES.between(appointmetStart, appointmetEnd));
            appointment.setCustomerId(rs.getInt("customerId"));
            appointment.setCustomerName(rs.getString("contact"));
            
            // For Reminders
            appointment.setCompleted(false);
            
            //Add employee to the ObservableList
            appointmentList.add(appointment);
            
            // For Reminders
            CustomerRecords.addAppointment(appointment);
        }
        //return empList (ObservableList of Appointments)
        return appointmentList;
    }
    
    //*************************************
    //UPDATE Appointments
    //*************************************
    public static void updateAppointment (Appointment appointment) throws SQLException, ClassNotFoundException {
        
        int appointmentId = appointment.getAppointmentId();
        String appointmentName = appointment.getAppointmentName();
        
        LocalDate appointmentDate = appointment.getAppointmentDate();
        LocalTime start = appointment.getAppointmentTime();
        int duration = appointment.getDuration();
        LocalTime end = start.plus(Duration.ofMinutes(duration));

        String contact = appointment.getCustomerName();
        int customerId = appointment.getCustomerId();
        
        // Get logged in user name
        String loggedInUser = LoginDAO.getLoggedInUser();

        //Declare an UPDATE statement
        String updateStmt = 
                        " UPDATE appointment\n" +
                        " SET\n" +
                        " title = '"+ appointmentName +"',\n" +
                        " start = '"+ appointmentDate +" " + start + ":00" + "',\n" +
                        " end = '"+ appointmentDate +" "+ end + ":00" + "',\n" +
                        " contact = '"+ contact +"',\n" +
                        " lastUpdate = NOW(),\n" +
                        " customerId = '"+ customerId +"',\n" +
                        " lastUpdateBy = '"+ loggedInUser +"'\n" +
                        " WHERE appointmentId = "+ appointmentId +";";
        
        System.out.println("Update Statement SQL :" + updateStmt);
        
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);  
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }
    
    //*************************************
    //INSERT Appointments
    //*************************************
    public static void insertAppointment (Appointment appointment) throws SQLException, ClassNotFoundException {

        int appointmentId = getLastAppointmentId();
        String appointmentName = appointment.getAppointmentName();
        
        LocalDate appointmentDate = appointment.getAppointmentDate();
        LocalTime start = appointment.getAppointmentTime();
        int duration = appointment.getDuration();
        LocalTime end = start.plus(Duration.ofMinutes(duration));

        String contact = appointment.getCustomerName();
        int customerId = appointment.getCustomerId();
        
        // Get logged in user name
        String loggedInUser = LoginDAO.getLoggedInUser();
        
        // For reminders
        appointment.setCompleted(false);

        //Declare a INSERT statement
        String updateStmt =
                        " INSERT IGNORE INTO appointment\n" +
                        " (appointmentId, customerId, title, contact, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)\n" +
                        " VALUES(\n" +
                        "   " + appointmentId + ",\n" +
                        "   " + customerId + ",\n" +
                        "   '" + appointmentName + "',\n" +
                        "   '"+ contact + "',\n" +
                        "   '" + appointmentDate +" " + start + ":00" + "',\n" +  
                        "   '" + appointmentDate +" "+ end + ":00" + "',\n" +
                        "   CURDATE(),\n" +
                        "   '"+ loggedInUser +"',\n" +
                        "   NOW(),\n" +
                        "   '"+ loggedInUser +"'" + ");";

        System.out.println("Insert Statement SQL :" + updateStmt);
        
        //Execute INSERT operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);            
            appointmentList.add(appointment);
            
            // For Reminders
            CustomerRecords.addAppointment(appointment);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }        
        
    }

    //*************************************
    //DELETE an appointment
    //*************************************
    public static void deleteAppointmentById (int appointmentId) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt =
                        " DELETE FROM appointment\n" +
                        " WHERE appointmentId ="+ appointmentId +";";
 
        System.out.println("Delete Appointment SQL :" + updateStmt);
        
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
            appointmentList.removeIf(appointment -> appointment.getAppointmentId() == appointmentId); 
            
            // For Reminders
            CustomerRecords.removeAppointment(appointmentId);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
    
}
