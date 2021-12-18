package openair.service.interfaces;

import com.lowagie.text.DocumentException;

import java.io.IOException;

public interface IPdfExporterService {
    String export(String username) throws DocumentException, IOException;
}
