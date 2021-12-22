package openair.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import openair.repository.TimeSheetDayRepository;
import openair.service.interfaces.IPdfExporterService;
import openair.utils.PdfTableDataOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PdfExporterService implements IPdfExporterService {

    private StorageService storageService;

    private TimeSheetDayRepository timeSheetDayRepository;

    @Autowired
    public PdfExporterService(StorageService storageService, TimeSheetDayRepository timeSheetDayRepository) {
        this.storageService = storageService;
        this.timeSheetDayRepository = timeSheetDayRepository;
    }

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


    private void writeTableData(PdfPTable table) {
        List<PdfTableDataOutput> pdfTableDataOutputList = timeSheetDayRepository.getDataForPdf();

        for(PdfTableDataOutput pdfTableDataOutput : pdfTableDataOutputList) {
            table.addCell(pdfTableDataOutput.getEmployee());
            table.addCell(pdfTableDataOutput.getProject());
            table.addCell(String.valueOf(pdfTableDataOutput.getWorkTime()));
            table.addCell(pdfTableDataOutput.getProjectType());
        }
    }

    public String export(String username) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);

        Path tempFile = Files.createTempFile(username, ".pdf");

        File file = new File(tempFile.toString());
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

        return tempFile.getFileName().toString();
    }
}
