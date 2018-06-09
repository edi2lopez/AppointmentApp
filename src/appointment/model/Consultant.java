
package appointment.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author el
 */
public class Consultant {
    
    private IntegerProperty consultantId;
    private StringProperty consultantUsername;
    
    public Consultant() {
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
    public String getConsultantName() {
        return consultantUsername.get();
    }

    public void setConsultantName(String consultantUsername) {
        this.consultantUsername = new SimpleStringProperty();
        this.consultantUsername.setValue(consultantUsername);
    }

    public StringProperty getConsultantUsernameProperty() {
        return consultantUsername;
    }

    
}
