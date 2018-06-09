
package appointment.model;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author el
 */
public class Appointment {

    private IntegerProperty appointmentId;
    private ObjectProperty<LocalDate> date;
    private ObjectProperty<LocalTime> time;
    private IntegerProperty customerId;
    private StringProperty customerUsername;
    private StringProperty appointmentName;
    private IntegerProperty duration;
    private SimpleBooleanProperty completed;
    private IntegerProperty consultantId;

    public Appointment() {
    }
    
    public int getAppointmentId() {
        return appointmentId.get();
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = new SimpleIntegerProperty();
        this.appointmentId.setValue(appointmentId);
    }

    public IntegerProperty getAppointmentIdProperty() {
        return appointmentId;
    }    

    public LocalDate getAppointmentDate() {
        return date.get();
    }

    public void setAppointmentDate(LocalDate date) {
        this.date = new SimpleObjectProperty<>();
        this.date.setValue(date);
    }

    public ObjectProperty getAppointmentDateProperty() {
        return date;
    }
    
    public LocalTime getAppointmentTime() {
        return time.get();
    }

    public void setAppointmentTime(LocalTime time) {
        this.time = new SimpleObjectProperty<>();
        this.time.setValue(time);
    }

    public ObjectProperty getAppointmentTimeProperty() {
        return time;
    }   

    public int getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(int customerId) {
        this.customerId = new SimpleIntegerProperty();
        this.customerId.setValue(customerId);
    }

    public IntegerProperty getCustomerIdProperty() {
        return customerId;
    }  

    public String getCustomerName() {
        return customerUsername.get();
    }

    public void setCustomerName(String name) {
        this.customerUsername = new SimpleStringProperty();
        this.customerUsername.setValue(name);
    }

    public StringProperty getCustomerUsernameProperty() {
        return customerUsername;
    }

    public String getAppointmentName() {
        return appointmentName.get();
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = new SimpleStringProperty();
        this.appointmentName.setValue(appointmentName);
    }

    public StringProperty getAppointmentNameProperty() {
        return appointmentName;
    }

    public int getDuration() {
        return duration.get();
    }

    public void setDuration(int duration) {
        this.duration = new SimpleIntegerProperty();
        this.duration.setValue(duration);
    }

    public IntegerProperty getDurationProperty() {
        return duration;
    }

    public boolean getCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed = new SimpleBooleanProperty(false);
        this.completed.setValue(completed);
    }

    public SimpleBooleanProperty getCompletedProperty() {
        return completed;
    }
    
    public int getConsultantId() {
        return consultantId.get();
    }

    public void setConsultantId(int consultantId) {
        this.consultantId = new SimpleIntegerProperty();
        this.consultantId.setValue(consultantId);
    }

    public IntegerProperty getConsultantIdProperty() {
        return consultantId;
    }  
    
}
