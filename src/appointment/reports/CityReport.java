
package appointment.reports;

import appointment.model.Customer;
import appointment.model.CustomerRecords;
import java.util.List;
import net.sf.dynamicreports.examples.Templates;
import static net.sf.dynamicreports.examples.Templates.groupStyle;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 *
 * @author el
 */
public class CityReport {

    public CityReport() {
        build();
    }
    private void build() {

        TextColumnBuilder<String> nameColumn = col.column("Customer Name", "name", type.stringType());
        TextColumnBuilder<String> addressColumn = col.column("Customer Address", "address", type.stringType());
        TextColumnBuilder<String> phoneColumn = col.column("Customer Phone", "phone", type.stringType());
        TextColumnBuilder<String> cityColumn = col.column("Customer City", "city", type.stringType());

        CustomGroupBuilder cityGroup = grp.group("city", String.class)
		                                  .setStyle(groupStyle)
		                                  .setTitle("City")
		                                  .setTitleStyle(groupStyle)
		                                  .setTitleWidth(60)
		                                  .setHeaderLayout(GroupHeaderLayout.TITLE_AND_VALUE);
        
        try {
            report()
                .setTemplate(Templates.reportTemplate)
                .columns(
                    nameColumn,
                    addressColumn,
                    phoneColumn,
                    cityColumn
                )
                .sortBy(cityColumn)
                .groupBy(cityGroup)  
                .subtotalsAtGroupFooter(cityGroup, sbt.count(cityColumn))
                .title(Templates.createTitleComponent("Customers By City"))
                .pageFooter(Templates.footerComponent)
                .setDataSource(createDataSource())
                .show(false);	
        } catch (DRException e) {
                e.printStackTrace();
        }
    }
	
    private JRDataSource createDataSource() {

        List<Customer> customers = CustomerRecords.getCustomers();
        DRDataSource dataSource = new DRDataSource("name", "address", "phone", "city");
        
        customers.forEach(a -> dataSource.add(
            a.getName(),
            a.getAddress(),
            a.getPhone(),
            a.getCity()
        ));
        return dataSource;
        
    }

    
}
