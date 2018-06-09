
package appointment.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author el
 */
public class UserLocation {
    
    static TimeZone tz = Calendar.getInstance().getTimeZone();
    
    public static String getTimeZoneName() {
        return tz.getDisplayName();
    }
    
    public static String getTimeZone() {
        return tz.getID();
    }
    
}
