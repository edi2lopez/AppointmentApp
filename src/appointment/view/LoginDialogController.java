
package appointment.view;

import appointment.DAO.DBUtil;
import appointment.DAO.LoginDAO;
import appointment.util.LocaleUtil;
import appointment.util.UserLocation;
import java.util.Locale;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/**
 *
 * @author el
 */
public class LoginDialogController {
    
    @FXML private Label connectionLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label titleLabel;
    @FXML private Label userNameLabel;
    @FXML private Label passwordLabel;
    @FXML private TextField userNameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button cancelButton;
    @FXML private ComboBox<Locale> languageComboBox;
    
    private Stage dialogStage;
    private String userName;
    private String password;
    
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public LoginDialogController() {
    }  
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML public void initialize() {

        try {
            if(DBUtil.getInstance()!= null) {
                connectionLabel.setText("Connected to database ...");
            }
            else {
                connectionLabel.setText("Not Connected ...");
            }
            dateTimeLabel.setText(UserLocation.getTimeZoneName() + "\n" + UserLocation.getTimeZone());
      
        } catch(Exception e) {
            System.err.println(e.toString());
        }
        
        // Set language
        setLanguage();
        
        loginButton.setOnAction(this::handleLogin);

    }
    
    /**
     * Set language dropdown
     */
    private void setLanguage() {

        // Locale.setDefault(new Locale("es"));
        Locale currentLocale = Locale.getDefault();
        
        languageComboBox.getItems().addAll(new Locale("en"), new Locale("es"));
        languageComboBox.setValue(currentLocale);
        languageComboBox.setConverter(new StringConverter<Locale>() {
            @Override
            public String toString(Locale l) {

                if(languageComboBox.getSelectionModel().getSelectedItem() != null ) {
                    if(languageComboBox.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("es")) {
                        connectionLabel.setText("Conectado a la base de datos ...");
                        titleLabel.setText("Entrar a la Base de Datos");
                        userNameLabel.setText("Usuario");
                        passwordLabel.setText("Contraseña");
                        loginButton.setText("Entrar");
                        cancelButton.setText("Cancelar");   
                    } else {
                        connectionLabel.setText("Connected to Database ...");
                        titleLabel.setText("Login to Database");
                        userNameLabel.setText("User Name");
                        passwordLabel.setText("Password");
                        loginButton.setText("Login");
                        cancelButton.setText("Cancel");                                 
                    }
                }

                return l.getDisplayLanguage(l);
            }
            @Override
            public Locale fromString(String s) {
                // only really needed if combo box is editable
                return Locale.forLanguageTag(s);
            }
        });

        LocaleUtil.localeProperty().bind(languageComboBox.valueProperty());

    }
    
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;   

        dialogStage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //System.out.println("Window closed ...");
                    }
                });
            }
        });  
    }
    
    /**
     * Handle login
     */
    @FXML private void handleLogin(ActionEvent event) {       

        dialogStage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //System.out.println("Window closed ...");
                    }
                });
            }
        }); 
        
        if (getUser()) {
            dialogStage.close();    
        }
    }

    /**
     * Get User from database
     * @return 
     */
    public boolean getUser() {      
        return isInputValid();
    }
    
    /**
     * Validate user and password inputs
     */
    private boolean isInputValid() {
           
        String errorMessage = "";
        
        userName = userNameField.getText();
        password = passwordField.getText();  
       
        try {
            if(LoginDAO.logIn(userName,password)) {   
               return true;
            } else  {             
               throw new Exception("No user found");
            }
        } catch(Exception e) {
            System.err.println(e.toString());
        }         
        
        if(userNameField.getText() == null || userNameField.getText().length() == 0 ) {
            if(languageComboBox.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("es")) {
                errorMessage += " Por favor entre su mombre de usuario";
            } else {
                errorMessage += " Please enter your user name";
            }
        }
        
        if(passwordField.getText() == null || passwordField.getText().length() == 0) {
            if(languageComboBox.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("es")) {
                errorMessage += " Por favor entre su contraseña";
            } else {
                errorMessage += " Please enter your passwword";
            }            
        }
        
        if(LoginDAO.getLoggedInUser() == null) {
            errorMessage += " Incorrect user name or password";
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
                
            if(languageComboBox.getSelectionModel().getSelectedItem() != null ) {
                if(languageComboBox.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("es")) {
                    alert.setTitle("Incorrecta Información");
                    alert.setHeaderText("Por favor entre información correcta");
                    alert.setContentText(errorMessage);   
                }
            }
            
            alert.showAndWait();
            return false;            
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

        if(languageComboBox.getSelectionModel().getSelectedItem() != null ) {
            if(languageComboBox.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("es")) {
                alert.setTitle( "Salir Del System" );
                alert.setHeaderText("Salir del systema de clientes");
                alert.setContentText("Esta seguro que quiere salir?");  
            }
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }    
    
}
