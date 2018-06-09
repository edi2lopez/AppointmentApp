
package appointment.DAO;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author el
 */
public class DBUtil {
    
    private static DBUtil instance;
    
    /**
     * Database login details
     */
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    private static final String db = "WGUTEST";
    private static final String url = "jdbc:mysql://localhost/" + db;
    private static final String user = "wgu_test";
    private static final String pass = "test";       

    //Connect to DB
    public static void dbConnect() throws SQLException, ClassNotFoundException {
        //Setting Oracle JDBC Driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is the Oracle JDBC Driver located?");
            e.printStackTrace();
            throw e;
        }
 
        System.out.println("Oracle JDBC Driver Registered!");
 
        //Establish the Oracle Connection using Connection String
        try {
            conn = DriverManager.getConnection(url,user,pass);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }
    }
    
    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
           throw e;
        }
    }
    
    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
             
            System.out.println("Select statement: " + queryStmt + "\n");
 
            //Create statement
            stmt = conn.createStatement();
 
            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);
 
            //CachedRowSet Implementation
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }
    
    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        
        try {
                        
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            
            // Set automocommit to false
            conn.setAutoCommit(false);

            //Create Statement
            stmt = conn.createStatement();
            
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
            
            // Commit changes
            conn.commit();
            
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }

    /**
     * Connect to database
     * 
     * @throws ClassNotFoundException 
     */
    private DBUtil() throws ClassNotFoundException {
        makeConnection();
    }
    
    /**
     * Login Form
     * 
     * @return
     * @throws ClassNotFoundException 
     */
    public static DBUtil getInstance() throws ClassNotFoundException  {
        if(instance == null) {
            synchronized(DBUtil.class) {
                  if(instance == null) {
                     instance = new DBUtil();
                  }
            }
        }
        return instance;
    }
    
    /**
     * Connect to MySQL Database
     * 
     * @return
     * @throws ClassNotFoundException 
     */
    private static boolean makeConnection() throws ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url,user,pass);
            System.out.println("Connected to database : " + db);
            return true;
        }
        catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
            System.out.println("SQLState: "+e.getSQLState());
            System.out.println("VendorError: "+e.getErrorCode());
            return false;
        }
    }
    
    /**
     * Connect to database
     * 
     * @return 
     */
    public static Connection connection() {
        try {
            if(instance == null)
                makeConnection();
            return conn;
        }
        catch(Exception ex) {
            System.err.println(ex.toString());
            return null;
        }
    }

}
