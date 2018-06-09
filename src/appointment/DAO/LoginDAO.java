
package appointment.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author el
 */
public class LoginDAO {
    
    // private static boolean isValidated;
    private static String loggedInUser;
    
    public static boolean logIn(String inUserName,String inPassword) throws SQLException {
        
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String selectStmt = "SELECT userId, userName, password FROM user WHERE userName = ? AND password = ?";
        
        try {
            dbConnection = DBUtil.connection();
            preparedStatement = dbConnection.prepareStatement(selectStmt);

            int userId = 0;
            String userName = "";
            String password = "";

            preparedStatement.setString (1, inUserName);
            preparedStatement.setString (2, inPassword);

            // execute select SQL stetement
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                userName = rs.getString("userName");
                password = rs.getString("password");
                loggedInUser = userName;
                return true;
            }

            return false;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

            return false;

        }

    }   

    /**
     * Get logged in user name
     * @return 
     */
    public static String getLoggedInUser() {
        return loggedInUser;
    }
    
}
