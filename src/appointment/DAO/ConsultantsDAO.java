package appointment.DAO;

import appointment.model.Consultant;
import appointment.model.CustomerRecords;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author el
 */
public class ConsultantsDAO {
    
    private static final List<Consultant> consultants = new ArrayList<Consultant>();
    private static final ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants);


    //*******************************
    //SELECT Consultants
    //*******************************
    public static ObservableList<Consultant> getConsultants() throws SQLException, ClassNotFoundException {
    
        //Declare a SELECT statement
        String selectStmt = "SELECT userId, userName FROM user;";
        System.out.println("SQL: " + selectStmt);
        
        //Execute SELECT statement
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            return getConsultantsList(rsEmps);
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            throw e;
        }
    }
    
    //Select * from customer operation
    private static ObservableList<Consultant> getConsultantsList(ResultSet rs) throws SQLException, ClassNotFoundException {

        while (rs.next()) {
            Consultant consultant = new Consultant();
            consultant.setConsultantId(rs.getInt("userId"));
            consultant.setConsultantName(rs.getString("userName"));
            
            //Add employee to the ObservableList
            consultantList.add(consultant);
            
            // Add Consultants to Customer records
            CustomerRecords.addConsultant(consultant);
            
        }
        
        return consultantList;
        
    }    
}
