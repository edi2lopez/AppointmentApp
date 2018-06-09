
package appointment.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author el
 */
public abstract class Person extends City {
    
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty phone;
    private StringProperty address;
    
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty();
        this.id.setValue(id);
    }
    
    public IntegerProperty idProperty() {
        return id;
    }
    
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty();
        this.name.setValue(name);
    }
    
    public StringProperty nameProperty() {
        return name;
    }
    
    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String name) {
        this.phone = new SimpleStringProperty();
        this.phone.setValue(name);
    }
    
    public StringProperty phoneProperty() {
        return phone;
    }
    
    public int getAddressId() {
        return id.get();
    }

    public void setAddressId(int id) {
        this.id = new SimpleIntegerProperty();
        this.id.setValue(id);
    }
    
    public IntegerProperty addressIdProperty() {
        return id;
    }
    
    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address = new SimpleStringProperty();
        this.address.setValue(address);
    }
    
    public StringProperty addressProperty() {
        return address;
    }

    
}
