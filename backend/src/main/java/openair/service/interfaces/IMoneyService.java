package openair.service.interfaces;

import org.springframework.http.ResponseEntity;

public interface IMoneyService {
    double convert(String date, String fromCurrency, String toCurrency, double quantity);
}
