package openair.expenseReport;

import openair.dto.ExspenseReportDTO;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Money;
import openair.model.Project;
import openair.model.enums.Currency;
import openair.model.enums.Status;

import java.time.LocalDate;

public class TestData {
    public static ExspenseReportDTO createReportDTO() {
        ExspenseReportDTO expenseReport = new ExspenseReportDTO();

        expenseReport.setName("Troskovi");
        expenseReport.setTrackingNumber("s;kdfjb2");
        expenseReport.setDocument("sfbs");
        expenseReport.setStatus(Status.INPROCESS);
        expenseReport.setDescription("dtherthdg");
        Money refund = new Money();
        refund.setId(1L);
        refund.setDate(LocalDate.now());
        refund.setCurrency(Currency.RSD);
        refund.setQuantity(1000);
        expenseReport.setRefund(refund);
        Project project = openair.project.TestData.createProject();
        expenseReport.setProjectId(project.getId());

        return expenseReport;
    }

    public static ExpenseReport createReport() {
        ExpenseReport expenseReport = new ExpenseReport();

        Employee employee = openair.employee.TestData.createEmployee();

        expenseReport.setId(1L);
        expenseReport.setName("Troskovi");
        expenseReport.setTrackingNumber("s;kdfjb2");
        expenseReport.setDocument("sfbs");
        expenseReport.setStatus(Status.INPROCESS);
        expenseReport.setDescription("dtherthdg");
        Money refund = new Money();
        refund.setId(1L);
        refund.setDate(LocalDate.now());
        refund.setCurrency(Currency.RSD);
        refund.setQuantity(1000);
        expenseReport.setRefund(refund);
        expenseReport.setEmployee(employee);
        expenseReport.setDateOfCreation(LocalDate.parse("2021-12-12"));
        return expenseReport;
    }
}
