package openair.service.interfaces;

public interface IMoneyService {
    double convert(String date, String fromCurrency, String toCurrency, double quantity);
}
