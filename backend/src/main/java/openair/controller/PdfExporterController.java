package openair.controller;

import com.lowagie.text.DocumentException;
import openair.service.PdfExporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(value = "/pdfs", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdfExporterController {

    private PdfExporterService pdfExporterService;

    @Autowired
    public PdfExporterController(PdfExporterService pdfExporterService) {
        this.pdfExporterService = pdfExporterService;
    }

    @GetMapping("/export-pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> exportPDF(Principal loggedAdmin) throws DocumentException, IOException {
        return new ResponseEntity<String>(pdfExporterService.export(loggedAdmin.getName()), HttpStatus.OK);
    }

}
