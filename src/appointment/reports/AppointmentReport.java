
package appointment.reports;

import appointment.model.Appointment;
import appointment.model.CustomerRecords;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.sf.dynamicreports.examples.Templates;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 *
 * @author el
 */
public class AppointmentReport {

    public AppointmentReport() {
        build();
    }
 	
    private void build() {
        
        TextColumnBuilder<Date> yearColumn  = col.column("Appt. year",  "date", type.dateYearType());
        TextColumnBuilder<Date> monthColumn = col.column("Appt. month", "date", type.dateMonthType());
        
        TextColumnBuilder<Date> dateColumn = col.column("Appt. date", "date", type.dateType());
        TextColumnBuilder<String> appointmentColumn = col.column("Appt. Type", "type", type.stringType());
        TextColumnBuilder<Integer> durationColumn = col.column("Duration (min)",    "duration",  type.integerType());
        TextColumnBuilder<Integer> idColumn = col.column("ID",    "id",  type.integerType());
        
        ColumnGroupBuilder yearGroup  = grp.group(yearColumn)
                .groupByDataType();
	ColumnGroupBuilder monthGroup = grp.group(monthColumn)
                .groupByDataType()
                .setHeaderLayout(GroupHeaderLayout.EMPTY)
                .setHideColumn(false);

        try {
            report()
                .setTemplate(Templates.reportTemplate)
                .columns(
                    yearColumn,
                    monthColumn,
                    dateColumn,
                    appointmentColumn,
                    durationColumn, 
                    idColumn
                )
                .sortBy(dateColumn)
                .groupBy(yearGroup, monthGroup)
                .subtotalsAtGroupFooter(monthGroup, sbt.count(idColumn))
                .title(Templates.createTitleComponent("Appointments By Month"))
                .pageFooter(Templates.footerComponent)
                .setDataSource(createDataSource())
                .show(false);	
        } catch (DRException e) {
                e.printStackTrace();
        }
    }
	
    private JRDataSource createDataSource() {

        List<Appointment> appointments = CustomerRecords.getAppointments();
        DRDataSource dataSource = new DRDataSource("date", "type", "duration", "id");
        
        appointments.forEach(a -> dataSource.add(
            (toDate(
                a.getAppointmentDate().getYear(),
                a.getAppointmentDate().getMonthValue(),
                a.getAppointmentDate().getDayOfMonth()
            )),
            a.getAppointmentName(),
            a.getDuration(),
            a.getAppointmentId()
        ));
        return dataSource;
        
    }
	
    private Date toDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

}
