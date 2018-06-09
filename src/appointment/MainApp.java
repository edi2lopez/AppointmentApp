
package appointment;

import appointment.model.Appointment;
import appointment.model.Customer;
import appointment.view.AppointmentEditDialogController;
import appointment.view.CustomerRecordsController;
import appointment.view.LoginDialogController;
import appointment.view.CustomerEditDialogController;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author el
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws SQLException, ClassNotFoundException {
        
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Appointment App");
        
        if(showLoginDialog()) {
        //if(true){
            initRootLayout();
            showCustomerRecords();            
        }
 
    }
    
    public boolean showLoginDialog() {
        try {
            // Load login form layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LoginDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login to Database");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set user name & password into the controller 
            LoginDialogController controller = loader.getController(); 
            controller.setDialogStage(dialogStage);
            
            // Show the dialog and wait until data is validated
            dialogStage.showAndWait();
            return controller.getUser();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }        
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Shows customer records inside the root layout.
     */
    public void showCustomerRecords() throws SQLException, ClassNotFoundException {
        try {
            // Load customer records from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CustomerRecords.fxml"));
            AnchorPane customerRecords = (AnchorPane) loader.load();

            // Set inventory overview into the left of root layout.
            rootLayout.setLeft(customerRecords);
            
            // Give the controller access to the main app.
            CustomerRecordsController controller = loader.getController();
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    /**
     * Customer Edit dialog
     * @param customer
     * @return 
     */
    public boolean showCustomerEditDialog (Customer customer) throws SQLException, ClassNotFoundException {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CustomerEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Customer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the part into the controller.
            CustomerEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomer(customer);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Appointment edit dialog
     */
    public boolean showAppointmentDialog (Appointment appointment) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AppointmentEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Appointment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the part into the controller.
            AppointmentEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAppointment(appointment);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Returns the main stage.
     * @return
    */
   public Stage getPrimaryStage() {
        return primaryStage;
   }    
    
   /**
     * @param args the command line arguments
    */
   public static void main(String[] args) {
       launch(args);
   }

}