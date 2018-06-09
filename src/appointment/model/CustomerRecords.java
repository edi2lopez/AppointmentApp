package appointment.model;
 
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
 
/**
 *
 * @author el
 */
public class CustomerRecords {
 
    /**
     * The products as an observable list of Inventory.
     */
    //private static final LinkedHashSet<Customer> customers = new LinkedHashSet<Customer>();
    private static final List<Customer> customers = new ArrayList<Customer>();
    private static final ObservableList<Customer> customerList = FXCollections.observableArrayList(customers);
 
    /**
     * Returns customers as an observable list.
     * @return
     */
    public static ObservableList<Customer> getCustomers() {
        return customerList;
    }
 
    /**
     * Add Customers
     * @param customer
     */
    public static void addCustomer(Customer customer) {
        customerList.add(customer);
    }
 
    /**
     * Remove Customers
     * @param customerId
     * @return
     */
    public static boolean removeCustomer(int customerId) {
        return customerList.removeIf(product -> product.getId() == customerId);
    }
 
    /**
     * Appointments
     */
    private static final ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();
    // List for filtering appoitnment results
    private static final FilteredList<Appointment> filteredCustomers = new FilteredList<>(appointmentData, p -> true);
    private static final SortedList<Appointment> sortedCustomers = new SortedList<>(filteredCustomers);
 
    /**
     * Returns appointments as an observable list.
     * @return
     */
    public static ObservableList<Appointment> getAppointments() {
        return appointmentData;
    }
 
    /**
     * Add Appointments
     * @param appointment
     */
    public static void addAppointment(Appointment appointment) {
        appointmentData.add(appointment);
    }
    
    /**
     * Remove appointment by ID
     * 
     * @param appointmentId
     * @return 
     */
    public static boolean removeAppointment(int appointmentId) {
        return appointmentData.removeIf(appointment -> appointment.getAppointmentId() == appointmentId);
    }
    
    /**
     * Filter Appointments
     * @return 
     */
    public static FilteredList<Appointment> getFilteredAppointments() {
        return filteredCustomers;
    }
    
    /**
     * Filter Appointments
     * @return 
     */
    public static SortedList<Appointment> getSortedAppointments() {
        return sortedCustomers;
    }

    /**
     * Sort Appointments by week
     * @return 
     */
    public static ObservableList<String> getAppointmentsByWeek() {

        List<LocalDate> dates = appointmentData.stream()
                .map(appointment -> appointment.getAppointmentDate())
                .collect(Collectors.toList());  
        
        Map<Integer, List<LocalDate>> map = 
            dates.stream()
                .collect(Collectors.
                 groupingBy(x -> x.get(ChronoField.ALIGNED_WEEK_OF_YEAR)));
        
        List<String> weeksList = map.entrySet().stream()
                .map(x-> "Week: " + x.getKey().toString())
                .collect(Collectors.toList());

        //Collections.sort(weeksList);
        //Collections.reverse(weeksList);
        
        return FXCollections.observableArrayList(weeksList);
             
    }
    
    /**
     * List of all consultants
     */
    private static final List<Consultant> consultants = new ArrayList<Consultant>();
    private static final ObservableList<Consultant> consultantList = FXCollections.observableArrayList(consultants);
    
    /**
     * Returns consultants as an observable list.
     * @return
     */
    public static ObservableList<Consultant> getConsultants() {
        return consultantList;
    }
    
   /**
     * Add Appointments
     * @param consultant
     */
    public static void addConsultant(Consultant consultant) {
        consultantList.add(consultant);
    }
    
    /**
     * Remove consultant by ID
     * 
     * @param consultantId
     * @return 
     */
    public static boolean removeConsultant(int consultantId) {
        return consultantList.removeIf(consultant -> consultant.getConsultantId() == consultantId);
    }

}