
package appointment.util;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author el
 * 
 */
public class LocaleUtil {
    
    private static final ObjectProperty<Locale> locale = new SimpleObjectProperty<>(Locale.getDefault());
    public static ObjectProperty<Locale> localeProperty() {
        return locale ;
    }
    public static Locale getLocale() {
        return locale.get();
    }
    public static void setLocale(Locale locale) {
        localeProperty().set(locale);
    }

    public static String i18n(String key) {
        return ResourceBundle.getBundle("bundleName", getLocale()).getString(key);
    }
    
}
