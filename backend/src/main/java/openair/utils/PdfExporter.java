package openair.utils;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import liquibase.pro.packaged.P;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.Employee;
import openair.model.Task;
import openair.model.Project;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import openair.model.TimeSheetDay;
import openair.service.TimeSheetDayService;

import javax.servlet.http.HttpServletResponse;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PdfExporter {
    private List<Employee> employeeList;
    private TimeSheetDayService timeSheetDayService;

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        cell.setPhrase(new Phrase("Employee"));
        cell.setPadding(5);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Project"));
        cell.setPadding(5);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total work time"));
        cell.setPadding(5);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Project type"));
        cell.setPadding(5);
        table.addCell(cell);
    }

//    table.addCell(employee.getName());
//    table.addCell(project.getName());
//    //  Ovde treba ukupno uneti ukupno sati
//    table.addCell(project.getProjectType().toString());

    private void writeTableData(PdfPTable table) {
       for(Employee employee : employeeList) {
           double workTime = 0;
           List<Project> projectList = employee.getProjects();
           for(Project project : projectList) {
               List<Task> taskList = project.getTasks();
               for(Task task : taskList) {
                   TimeSheetDay timeSheetDay = timeSheetDayService.getByTaskIdEmployeeId(task.getId(), employee.getId()); // ovo se sigurno menja
                   workTime += timeSheetDay.getWorkTime();
               }
               table.addCell(employee.getName());
               table.addCell(project.getName());
               table.addCell(String.valueOf(workTime));
               String strProject = project.getProjectType().toString();
               table.addCell(strProject);
           }

       }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("List of projects, employees and work hours"));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();
    }
}
