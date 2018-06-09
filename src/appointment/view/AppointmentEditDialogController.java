package appointment.view;

import appointment.MainApp;
import appointment.model.Appointment;
import appointment.model.Customer;
import appointment.model.CustomerRecords;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author el
 */
public class AppointmentEditDialogController {

    @FXML private DatePicker appointmentDate;
    @FXML private TextField appointmentTimeField;
    @FXML private TextField appointmentNameField;
    @FXML private TextField appointmentCustomerIdField;
    @FXML private TextField appointmentCustomerNameField;
    @FXML private TextField appointmentDurationField;
    
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> customerIdColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerCityColumn;
    @FXML private Button addCustomerToAppointment;

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mma");
    DateTimeFormatter timeFormatterAlt = DateTimeFormatter.ofPattern(""
        + "[hh:mma]"
        + "[h:mma]"
    );

    private Stage dialogStage;
    private Appointment appointment;
    private boolean okClicked;
    private MainApp mainApp;


    public AppointmentEditDialogController() {
    }

    @FXML private void initialize() throws SQLException, ClassNotFoundException {
        
        // Set Customer Table
        setCustomerTable();
        setCustomers();
        
        // Set the customer as read only 
        appointmentCustomerIdField.setDisable(true);
        appointmentCustomerNameField.setDisable(true);
        
        // add customers to appointment
        addCustomerToAppointment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                appointmentCustomerNameField.setText(selectedCustomer.getName());
                appointmentCustomerIdField.setText(Integer.toString(selectedCustomer.getId()));
            }
        });        

    }

   /**
    * 
    * @param dialogStage 
    */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }  
    
    
    /**
     * Predicate of Unique values for customers
     * 
     * @param <T>
     * @param keyExtractor
     * @return 
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
        Map<Object,String> seen = new ConcurrentHashMap<>();
        return t -> seen.put(keyExtractor.apply(t), "") == null;
    }
    
    /**
     * Get a list of Unique customers to add to appointment
     * 
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    private void setCustomers() throws SQLException, ClassNotFoundException {

        ObservableList<Customer> customers = CustomerRecords.getCustomers();
        
        List<Customer> customerList = new ArrayList<>();
        customerList = customers.stream()
                            .filter(distinctByKey(Customer::getName))
                            .collect(Collectors.toList());

        customerTable.setItems(FXCollections.observableArrayList(customerList));
        
    }

    /**
     * Set appointment data
     * @param appointment 
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;

        appointmentDate.setValue(LocalDate.from(appointment.getAppointmentDate()));
        appointmentTimeField.setText(appointment.getAppointmentTime().format(timeFormatter));
        appointmentNameField.setText(appointment.getAppointmentName());
        appointmentDurationField.setText(Integer.toString(appointment.getDuration()));
        appointmentCustomerIdField.setText(Integer.toString(appointment.getCustomerId()));
        appointmentCustomerNameField.setText(appointment.getCustomerName());

    }

    /**
     * Set customer table
     */
    private void setCustomerTable() {
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        customerCityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
    }

    /**
     * Add customers to Appointment
     */
    @FXML private void addCustomerToAppointment() {
        
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if ( selectedCustomer != null && selectedCustomer.getId() >= 0 ) {
            appointmentCustomerNameField.setText(selectedCustomer.getName());
            appointmentCustomerIdField.setText(Integer.toString(selectedCustomer.getId()));
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No customer Selected");
            alert.setContentText("Please select a customer in the table.");

            alert.showAndWait();
        }

    }
 
    /**
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML private void handleOk() {
        if(isInputValid()) {

            appointment.setAppointmentDate(appointmentDate.getValue());
            appointment.setAppointmentTime(LocalTime.parse(appointmentTimeField.getText(), timeFormatterAlt));
            appointment.setAppointmentName(appointmentNameField.getText());
            appointment.setDuration(Integer.parseInt(appointmentDurationField.getText()));
            appointment.setCustomerId(Integer.parseInt(appointmentCustomerIdField.getText()));
            appointment.setCustomerName(appointmentCustomerNameField.getText());

            okClicked = true;
            dialogStage.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle( "Appointment Information" );

            alert.setHeaderText("Appointment Updated");
            alert.setContentText("Appointment " + appointment.getAppointmentName() + " Successfully Updated");
            alert.show();

        }
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

    /**
     * Validate input fields
     * @return 
     */
    private boolean isInputValid() {

        String errorMessage = "";

        // validate appointment date
        if(appointmentDate.getValue().isBefore(LocalDate.now())) {
            errorMessage += "No valid date. Appointment can not be in the past!\n";
        }

        if (appointmentTimeField.getText() == null || appointmentTimeField.getText().length() == 0) {
            errorMessage += "No valid time!\n";
        } else if(LocalTime.parse(appointmentTimeField.getText(), timeFormatterAlt).isBefore(LocalTime.now()) && appointmentDate.getValue().isBefore(LocalDate.now())) {
            errorMessage += "No valid time. Appointment can not be in the past!\n";
        } else if( !(LocalTime.parse(appointmentTimeField.getText(), timeFormatterAlt).isAfter(LocalTime.of(8, 0)) && LocalTime.parse(appointmentTimeField.getText(), timeFormatterAlt).isBefore(LocalTime.of(17,0)))) {
            errorMessage += "No valid time, please schedule during business hours!\n";
        } else {
            // try to parse the appointment time into time.
            try {
                LocalTime.parse(appointmentTimeField.getText(), timeFormatterAlt);
            } catch (DateTimeException e) {
                errorMessage += "No valid time format (please follow the following format: HH:MM )!\n";
            }
        }

        // validate appointment name
        if (appointmentNameField.getText() == null || appointmentNameField.getText().length() == 0) {
            errorMessage += "No valid appointment name!\n";
        }

        // validate appointment duration
        if (appointmentDurationField.getText() == null || appointmentDurationField.getText().length() == 0) {
            errorMessage += "No valid appointment duration!\n";
        } else {
            // try to parse the duration in minutes into an int.
            try {
                Integer.parseInt(appointmentDurationField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid appointment duration (number of minutes must be an number)!\n";
            }
        }

        // validate appointment customer id
        if (appointmentCustomerNameField.getText() == null || appointmentCustomerNameField.getText().length() == 0) {
            errorMessage += "No valid customer name!\n";
        }
        
        if(appointmentCustomerIdField.getText() == null || appointmentCustomerIdField.getText().length() == 0) {
            errorMessage += "No valid customer id Level!\n"; 
        } else if(Integer.parseInt(appointmentCustomerIdField.getText()) == 0) {
            errorMessage += "Please select a customer from the table!\n"; 
        } else {
            try {
                Integer.parseInt(appointmentCustomerIdField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Inventory Level (must be an number)!\n"; 
            }                
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
