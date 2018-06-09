package appointment.model;

import appointment.util.ReminderPredicates;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author el
 */
public class RemindersTask extends TimerTask {

    private Predicate<Appointment> predicate = ReminderPredicates.ALL;

    public RemindersTask() {
    }

    @Override
    public void run() {
        List<Appointment> dueRems = getDueRems();
        showNotifications(dueRems);
    }

    private List<Appointment> getDueRems() {

        ObservableList<Appointment> rems = CustomerRecords.getAppointments();
        predicate = ReminderPredicates.DUE_FIFTEEN;
        return rems.stream()
                .filter(predicate)
                .collect(Collectors.toList());

    }

    /*
     * Show notifications;
     * The alerts are displayed at 3 second interval.
     */
    private void showNotifications(List<Appointment> remsFifteen) {
        for (Appointment r : remsFifteen) {
            Platform.runLater(() -> showReminderAlert(r));
            try {
                Thread.sleep(3000); // 3 seconds
            }
            catch (InterruptedException e) {
            }
        }
    }

    private void showReminderAlert(Appointment reminder) {

        Alert alert = new Alert(AlertType.NONE); // a modal alert

        // Format alert content
        String name = reminder.getAppointmentName();
        alert.setTitle("Reminder - " + name);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);

        String content = name + " " +
                (true ? "Appointment" : "") + "\n" +
                reminder.getAppointmentDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) +
                "  " +
                reminder.getAppointmentTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            reminder.setCompleted(true);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
        // Display alert
        //alert.show();

    }

}
