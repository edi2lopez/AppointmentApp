package appointment.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author el
 */
public class User {

    private StringProperty id;
    private StringProperty userName;
    private ObservableList<String> appointmentType;

    public User(String userName, String id) {
        this.userName = new SimpleStringProperty(userName);
        this.id = new SimpleStringProperty(id);
    }

    public User() {
    }

    public ObservableList<String> getAppointmentType(){
        return appointmentType;
    }

    public String getId() {
        return id.get();
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty getUserNameProperty(){
        return userName;
    }

    public StringProperty getIdProperty(){
        return id;
    }

    public void setId(String id){
        this.id.set(id);
    }

    public void setUserName(String userName){
        this.userName.set(userName);
    }

}
