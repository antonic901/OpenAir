package openair.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
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
import openair.repository.TimeSheetDayRepository;
import openair.service.StorageService;
import openair.service.TimeSheetDayService;
import javax.servlet.http.HttpServletResponse;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PdfExporter {
    private TimeSheetDayRepository timeSheetDayRepository;
    private StorageService storageService;

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

//      table.addCell(employee.getName());
//      table.addCell(project.getName());
//      table.addCell(String.valueOf(workTime));
//      String strProject = project.getProjectType().toString();
//      table.addCell(strProject);

    private void writeTableData(PdfPTable table) {
        List<PdfTableDataOutput> pdfTableDataOutputList = timeSheetDayRepository.getDataForPdf();

        for(PdfTableDataOutput pdfTableDataOutput : pdfTableDataOutputList) {
            table.addCell(pdfTableDataOutput.getEmployee_name());
            table.addCell(pdfTableDataOutput.getProject_name());
            table.addCell(String.valueOf(pdfTableDataOutput.getWork_time()));
            String strProject = pdfTableDataOutput.getProject_type().toString();
            table.addCell(strProject);
        }
    }

    public String export(String username) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);

        File file = new File(LocalDateTime.now() + "-" + username + ".pdf");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        PdfWriter.getInstance(document, fos);

        document.open();
        document.add(new Paragraph("List of projects, employees and work hours"));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();

        storageService.upload(file);

        return file.getName();
    }
}
