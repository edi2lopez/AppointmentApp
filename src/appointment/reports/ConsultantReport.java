
package appointment.reports;

import appointment.model.Appointment;
import appointment.model.CustomerRecords;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.sf.dynamicreports.examples.Templates;
import static net.sf.dynamicreports.examples.Templates.groupStyle;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 *
 * @author el
 */
public class ConsultantReport {

    public ConsultantReport() {
        build();
    }
    


    private void build() {
        /*
        List<Consultant> consultantList = CustomerRecords.getConsultants();
        consultantList.forEach((d) -> {
            System.out.println("Consultant name: " + d.getConsultantName());
        });
        */
        TextColumnBuilder<Date> monthColumn = col.column("Appt. month", "date", type.dateMonthType());
        
        TextColumnBuilder<Date> dateColumn = col.column("Appt. date", "date", type.dateType());
        TextColumnBuilder<String> appointmentColumn = col.column("Appt. Type", "type", type.stringType());
        TextColumnBuilder<String> contactColumn = col.column("Appt. Contact", "contact", type.stringType());
        TextColumnBuilder<Integer> durationColumn = col.column("Duration (min)",    "duration",  type.integerType());

	ColumnGroupBuilder monthGroup = grp.group(monthColumn)
                .groupByDataType()
                .setHeaderLayout(GroupHeaderLayout.EMPTY)
                .setHideColumn(false);
        
        CustomGroupBuilder contactGroup = grp.group("contact", String.class)
		                                  .setStyle(groupStyle)
		                                  .setTitle("Contact")
		                                  .setTitleStyle(groupStyle)
		                                  .setTitleWidth(60)
		                                  .setHeaderLayout(GroupHeaderLayout.TITLE_AND_VALUE);
        
        try {
            report()
                .setTemplate(Templates.reportTemplate)
                .columns(
                    // contactColumn,
                    dateColumn,
                    appointmentColumn,
                    durationColumn
                )
                .groupBy(contactGroup)  
                .title(Templates.createTitleComponent("Appointments By Consultant"))
                .pageFooter(Templates.footerComponent)
                .setDataSource(createDataSource())
                .show(false);	
        } catch (DRException e) {
                e.printStackTrace();
        }
    }
	
    private JRDataSource createDataSource() {

        List<Appointment> appointments = CustomerRecords.getAppointments();
        DRDataSource dataSource = new DRDataSource("contact", "date", "type", "duration");
        
        appointments.forEach(a -> dataSource.add(
            a.getCustomerName(),
            (toDate(
                a.getAppointmentDate().getYear(),
                a.getAppointmentDate().getMonthValue(),
                a.getAppointmentDate().getDayOfMonth()
            )),
            a.getAppointmentName(),
            a.getDuration()
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
