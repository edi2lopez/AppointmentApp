
package appointment.util;

import appointment.model.Appointment;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

/**
 *
 * @author el
 */
public class ReminderPredicates {

    private static final Predicate<Appointment> COMPLETED = r ->
            r.getCompleted() == true;

    public static final Predicate<Appointment> ALL = r -> true;

    public static final Predicate<Appointment> TODAY = r ->
            r.getAppointmentDate().isEqual(LocalDate.now());

    private static final Predicate<Appointment> PAST_DAYS = r ->
            r.getAppointmentDate().isBefore(LocalDate.now());

    private static final Predicate<Appointment> PAST_TIME = r ->
            r.getAppointmentTime().isBefore(LocalTime.now());

    private static final Predicate<Appointment> TIME_NOW = r ->
            r.getAppointmentTime().equals(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));

    public static final Predicate<Appointment> DUE_NOW =
            TODAY.and(TIME_NOW);

    private static final Predicate<Appointment> PAST_FIFTEEN = r ->
            r.getAppointmentTime().isBefore(LocalTime.now().plusMinutes(15));

    public static final Predicate<Appointment> DUE_FIFTEEN =
            PAST_DAYS.negate().and(PAST_TIME.negate().and(PAST_FIFTEEN).and(COMPLETED.negate()));

    private static final Predicate<Appointment> WEEKENDS = r ->
            r.getAppointmentDate().getDayOfWeek() == DayOfWeek.SATURDAY || r.getAppointmentDate().getDayOfWeek() == DayOfWeek.SUNDAY;

    private static final Predicate<Appointment> WORK_HOURS = r ->
            r.getAppointmentTime().isAfter(LocalTime.of(8, 0)) && r.getAppointmentTime().isBefore(LocalTime.from(LocalTime.of(17, 0)));

    public static final Predicate<Appointment> BUSINESS_HOURS =
            WORK_HOURS.and(WEEKENDS.negate());

}
