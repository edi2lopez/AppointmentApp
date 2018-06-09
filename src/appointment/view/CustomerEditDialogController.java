
package appointment.view;

import appointment.model.Customer;
import appointment.DAO.CustomerDAO;
import java.sql.SQLException;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author el
 */
public class CustomerEditDialogController {
    
    @FXML private TextField customerIdField;
    @FXML private TextField customerNameField;
    @FXML private TextField customerPhoneField;
    @FXML private TextField customerAddressField;
    @FXML private TextField customerPostalCodeField;
    @FXML private ComboBox<String> customerCityComboBox;
    @FXML private ComboBox<String> customerCountryComboBox;
    
    private Stage dialogStage;
    private Customer customer;
    private boolean okClicked;

    public CustomerEditDialogController() {
    }

    @FXML private void initialize() {
        
        // Set the customer ID as read only 
        customerIdField.setDisable(true);

    }

    /**
     * 
     * @param dialogStage 
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;   
    }
    
    public void setCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        this.customer = customer;

        customerIdField.setText(Integer.toString(customer.getId()));
        customerNameField.setText(customer.getName());
        customerPhoneField.setText(customer.getPhone());
        customerAddressField.setText(customer.getAddress());
        customerPostalCodeField.setText(customer.getPostalCode());
        
        customerCityComboBox.setItems(CustomerDAO.getCities());
        customerCityComboBox.setValue(customer.getCity());

        customerCountryComboBox.setItems(CustomerDAO.getCountries());
        customerCountryComboBox.setValue(customer.getCountry());
        
        // Set observable value for cities and countries
        defaultCountry();

    }
    
    /**
     * Update Country based on selected city
     */
    private void defaultCountry() {   
        customerCityComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(customerCityComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("London")) {
                    customerCountryComboBox.setValue("England");
                } else if (!customerCityComboBox.getSelectionModel().getSelectedItem().isEmpty()) {
                    customerCountryComboBox.setValue("United States"); 
                }
            }    
        });

        customerCountryComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(customerCountryComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("England")) {
                    customerCityComboBox.setValue("London");
                } else if (customerCountryComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("United States")) {
                    customerCityComboBox.setValue(""); 
                }
            }    
        });        
    }
    
    /**
     * Called when the user clicks cancel.
     */
    @FXML private void handleCancel() {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle( "Cancel Changes" );
        alert.setHeaderText("Cancel Changes");
        alert.setContentText("Are you sure that you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dialogStage.close();
        } 
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }  

    @FXML private void handleOk() {
        
        if(isInputValid()) {
            
            customer.setId(Integer.parseInt(customerIdField.getText()));
            customer.setName(customerNameField.getText());
            customer.setPhone(customerPhoneField.getText());
            customer.setAddress(customerAddressField.getText());
            customer.setCity(customerCityComboBox.getValue());
            customer.setPostalCode(customerPostalCodeField.getText());
            customer.setCountry(customerCountryComboBox.getValue());
         
            okClicked = true;
            dialogStage.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle( "Appointment Information" );

            alert.setHeaderText("Customer Updated");
            alert.setContentText("Customer " + customer.getName() + " Successfully Updated");
            alert.show();
            
        }
    }
    
    private boolean isInputValid() {
        
        String errorMessage = "";
        
        if (customerNameField.getText() == null || customerNameField.getText().length() == 0) {
            errorMessage += "No valid customer name!\n"; 
        }
        
        if (customerPhoneField.getText() == null || customerPhoneField.getText().length() == 0) {
            errorMessage += "No valid Phone number!\n"; 
        }
        
        if (customerAddressField.getText() == null || customerAddressField.getText().length() == 0) {
            errorMessage += "No valid Address!\n"; 
        }        
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
        
    }
    
}
