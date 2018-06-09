
package appointment.view;

import appointment.MainApp;
import appointment.model.Appointment;
import appointment.DAO.AppointmentDAO;
import appointment.DAO.ConsultantsDAO;
import appointment.model.Customer;
import appointment.DAO.CustomerDAO;
import appointment.DAO.LoginDAO;
import appointment.model.Consultant;
import appointment.model.CustomerRecords;
import appointment.model.RemindersTask;
import appointment.reports.AppointmentReport;
import appointment.reports.CityReport;
import appointment.reports.ConsultantReport;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.WindowEvent;

/**
 *
 * @author el
 */
public class CustomerRecordsController {
    
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;
    @FXML private Button customerDeleteButton;

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, LocalDate> dateColumn;
    @FXML private TableColumn<Appointment, LocalTime> timeColumn;
    @FXML private TableColumn<Appointment, Integer> durationColumn;
    @FXML private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML private ComboBox<String> monthsComboBox;
    @FXML private ComboBox<String> weeksComboBox;
    
    @FXML private Button appointmentReportbyMonth;
    @FXML private Button appointmentReportbyConsultant;
    @FXML private Button appointmentReportbyCity;

    // Reference to the main application.
    private MainApp mainApp;
    String[] months = new DateFormatSymbols().getMonths();
    Consultant consultant;
    
    /**
     * Constructor
     * The constructor is called before the initialize() method.
     */
    public CustomerRecordsController() {  
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML private void initialize() {
        setCustomerTable();
        customerDeleteButton.setOnAction(this::handleDeleteCustomer);
        setAppointmentTable();
        setReminders();   
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public void setMainApp(MainApp mainApp) throws SQLException, ClassNotFoundException {
        this.mainApp = mainApp;
        
        // test current time
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        System.out.println("Today's date & time: " + today + " " + now);    
        
        // Set customer & appointment data 
        customerTable.setItems(CustomerDAO.getCustomers());
        appointmentTable.setItems(AppointmentDAO.getAppointments()); 

        // Reset appointment filters
        appointmentFilterReset();
        
        // Set appointment by month filter
        String[] months = new DateFormatSymbols().getMonths();
        List<String> monthsList = Arrays.stream(months).collect(Collectors.toList());
        monthsComboBox.setItems(FXCollections.observableArrayList(monthsList));
        appointmentByMonth();
        
        // Set appointment by week filter
        weeksComboBox.setItems(CustomerRecords.getAppointmentsByWeek());
        appointmentByWeek();
        
        // Appointment Report
        appointmentReportbyMonth.setOnAction((event) -> {
            new AppointmentReport();
        });
        
        // Consultants report
        appointmentReportbyConsultant.setOnAction((event) -> {
            new ConsultantReport();
        });
        
        // Cities report
        appointmentReportbyCity.setOnAction((event) -> {
            new CityReport();
        });
        
        // Consultants
        ConsultantsDAO.getConsultants();
        System.out.println("Logged in user: " + LoginDAO.getLoggedInUser());
        
        // Close application when windows closes
        mainApp.getPrimaryStage().setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        });        
        
    }
    
    /**
     * FIlter appointments by month
     */
    private void appointmentByMonth() {
        monthsComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
  
                CustomerRecords.getFilteredAppointments().setPredicate(appointment -> { 
                    
                    if (t1 == null || t1.isEmpty()) {
                        return true;
                    }
                    
                    String lowerCaseFilter = t1.toLowerCase();
                    if( appointment.getAppointmentDate().getMonth().toString().toLowerCase().contains(lowerCaseFilter) ) {
                        return true;
                    }
                    return false;
                });

                appointmentTable.setItems(CustomerRecords.getSortedAppointments());

            }    
        });
    }

    /**
     * Filter Appointments by week number of the year
     */
    private void appointmentByWeek() {
        weeksComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
  
                CustomerRecords.getFilteredAppointments().setPredicate(appointment -> { 
                    
                    if (t1 == null || t1.isEmpty()) {
                        return true;
                    }       

                    int weekNo = Integer.parseInt(t1.replace("Week: ", ""));       
                    
                    if( appointment.getAppointmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) == weekNo ) {
                        return true;
                    }
                    return false;
                });

                appointmentTable.setItems(CustomerRecords.getSortedAppointments());

            }    
        });
    }    
    
    /**
     * Observable value between "by month" and "by week" filter
     */
    private void appointmentFilterReset() {   
        weeksComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(!weeksComboBox.getSelectionModel().isEmpty()) {
                    monthsComboBox.getSelectionModel().clearSelection();
                } 
            }    
        });

        monthsComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(!monthsComboBox.getSelectionModel().isEmpty()) {
                    weeksComboBox.getSelectionModel().clearSelection();
                }
            }    
        });        
    }

    /**
     * Set customer table
     */
    private void setCustomerTable() {
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        customerAddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        customerPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
    }
    
    /**
     * Set Appointment table
     */
    private void setAppointmentTable() {

        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentDateProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTimeProperty());
        appointmentTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentNameProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().getDurationProperty().asObject());
        durationColumn.setCellFactory(cellData -> new TableCell<Appointment, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) { setText(null); }
                else if (item > 60 ) {
                    int hours  = item/60;
                    int minutes = item % 60;
                    if(minutes == 0) { setText(String.valueOf(hours) + "hr"); }
                    else { setText(String.valueOf(hours) + "hr " + String.valueOf(minutes) + "min"); }
                }
                else { setText(String.valueOf(item.toString() + "min")); }
            }
        });

    }
    
    /**
     *  set reminders
     */
    public void setReminders() {

        RemindersTask remindersTask = new RemindersTask();
        Timer timer = new Timer();
        long zeroDelay = 0L;
        long period = 60000L; // 60 * 1000 = 1 min
        timer.schedule(remindersTask, zeroDelay, period);

    }
    
    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected customer.
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    @FXML public void handleEditCustomer() throws SQLException, ClassNotFoundException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            boolean okClicked = mainApp.showCustomerEditDialog(selectedCustomer);

            if (okClicked) {
                try {
                    CustomerDAO.updateCustomer(selectedCustomer); 
                } catch(SQLException e) {
                    System.out.println("Problem occurred while updating customer name: " + e);
                }
                customerTable.refresh();
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer in the table.");
            alert.showAndWait();
        }
    }
    
    /**
     * Called when the user clicks the add new customer button. Opens a dialog to add
     * details for the new customer.
     * @throws java.sql.SQLException
     */
    @FXML public void handleNewCustomer() throws SQLException, ClassNotFoundException {
        Customer tempCustomer = new Customer();
        
        int nextCustomerId = CustomerDAO.getLastCustomerId();
        int nextAddressId = CustomerDAO.getLastAddressId();

        // Initialize customer with empty values
        tempCustomer.setId(nextCustomerId);
        tempCustomer.setName("");
        tempCustomer.setPhone("");
        tempCustomer.setAddressId(nextAddressId);
        tempCustomer.setAddress("");
        tempCustomer.setCity("");
        tempCustomer.setPostalCode("");
        tempCustomer.setCountry("");
        
        boolean okClicked = mainApp.showCustomerEditDialog(tempCustomer);
        if (okClicked) {
            try {
                CustomerDAO.insertCustomer(tempCustomer); 
            } catch(SQLException e) {
                System.out.println("Problem occurred while updating customer name: " + e);
            }
            customerTable.refresh();
        }
        
    }
    
    private void handleDeleteCustomer(ActionEvent event) {
        
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        
        if ( selectedCustomer != null && selectedCustomer.getId() >= 0 ) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle( "Delete Customer" );
            alert.setHeaderText("Delete Customer " + selectedCustomer.getName());
            alert.setContentText("Are you sure that you want to delete " + selectedCustomer.getName() + "?");            
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                               .ifPresent(response -> {
                try {
                    CustomerDAO.deleteCustomerById(selectedCustomer.getId());
                    
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(CustomerRecordsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No customer Selected");
            alert.setContentText("Please select a customer in the table.");

            alert.showAndWait();
        }
    }
    
    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected appointment.
     * @throws java.lang.ClassNotFoundException
     */
    @FXML public void handleEditAppointment() throws ClassNotFoundException {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            boolean okClicked = mainApp.showAppointmentDialog(selectedAppointment);

            if (okClicked) {
                try {
                    AppointmentDAO.updateAppointment(selectedAppointment); 
                } catch(SQLException e) {
                    System.out.println("Problem occurred while updating Appointment: " + e);
                }
                appointmentTable.refresh();
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the new appointment button. Opens a dialog to edit
     * details for a new appointment.
     */
    @FXML private void handleNewAppointment() throws ClassNotFoundException {
        Appointment tempAppointment = new Appointment();

        // Initialize appointment with empty/default values
        tempAppointment.setAppointmentDate(LocalDate.now());
        tempAppointment.setAppointmentTime(LocalTime.now());
        tempAppointment.setAppointmentName("");
        tempAppointment.setCustomerId(0);
        tempAppointment.setCustomerName("");
        tempAppointment.setDuration(15);
        tempAppointment.setCompleted(false);

        boolean okClicked = mainApp.showAppointmentDialog(tempAppointment);
        if (okClicked) {
            try {
                AppointmentDAO.insertAppointment(tempAppointment); 
            } catch(SQLException e) {
                System.out.println("Problem occurred while updating customer name: " + e);
            }
            appointmentTable.refresh();
        }
    }
    
    /**
     * Handle delete appointment
     * 
     * @param event 
     */
    @FXML private void handleDeleteAppointment(ActionEvent event) {
        
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        
        if ( selectedAppointment != null && selectedAppointment.getAppointmentId() >= 0 ) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle( "Delete Appointment" );
            alert.setHeaderText("Delete Appointment " + selectedAppointment.getAppointmentName());
            alert.setContentText("Are you sure that you want to delete " + selectedAppointment.getAppointmentName() + "?");            
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                               .ifPresent(response -> {
                try {
                    AppointmentDAO.deleteAppointmentById(selectedAppointment.getAppointmentId());
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerRecordsController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(CustomerRecordsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No appointment Selected");
            alert.setContentText("Please select an appointment in the table.");

            alert.showAndWait();
        }
    }    

    /**
     * Called when the user clicks exit.
     */
    @FXML private void handleExit() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle( "Exit System" );
        alert.setHeaderText("Exit Customer Management System");
        alert.setContentText("Are you sure that you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

}
