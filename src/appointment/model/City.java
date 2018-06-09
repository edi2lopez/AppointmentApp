
package appointment.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author el
 */
public abstract class City {

    private IntegerProperty cityId;
    private StringProperty city;
    private StringProperty postalCode;
    private IntegerProperty countryId;
    private StringProperty country;

    public int getCityId() {
        return cityId.get();
    }

    public void setCityId(int cityId) {
        this.cityId = new SimpleIntegerProperty();
        this.cityId.setValue(cityId);
    }
    
    public IntegerProperty cityIdProperty() {
        return cityId;
    }    

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city = new SimpleStringProperty();
        this.city.setValue(city);
    }
    
    public StringProperty cityProperty() {
        return city;
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = new SimpleStringProperty();
        this.postalCode.setValue(postalCode);
    }
    
    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public int getCountryId() {
        return countryId.get();
    }

    public void setCountryId(int countryId) {
        this.countryId = new SimpleIntegerProperty();
        this.countryId.setValue(countryId);
    }
    
    public IntegerProperty countryIdIdProperty() {
        return countryId;
    }        

    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country = new SimpleStringProperty();
        this.country.setValue(country);
    }
    
    public StringProperty countryProperty() {
        return country;
    }
    
}
