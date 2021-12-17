package openair.service;

import openair.repository.MoneyRepository;
import openair.service.interfaces.IMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MoneyService implements IMoneyService {

    @Value("${exchangeratesapi.token}")
    private String API_KEY;

    private MoneyRepository moneyRepository;
    private RestTemplate restTemplate;

    @Autowired
    public MoneyService(MoneyRepository moneyRepository, RestTemplate restTemplate) {
        this.moneyRepository = moneyRepository;
        this.restTemplate = restTemplate;
    }

    //DATE in format YYYY-MM-dd
    @Override
    public double convert(String date, String fromCurrency, String toCurrency, double quantity) {
//      Promena base valute nije moguca na besplatnoj pretplati
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://api.exchangeratesapi.io/v1/" + date + "?access_key=" + API_KEY + "&base=" + fromCurrency + "&symbols=" + toCurrency, String.class);

        //otkomentarisati prve dve linije i zakomenatriasti trecu. API omogucava samo 1000 zahteva po mesecu.

//        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://api.exchangeratesapi.io/v1/" + date + "?access_key=" + API_KEY + "&symbols=RSD", String.class);
//        String response = responseEntity.getBody();
        String response = "{\"success\":true,\"timestamp\":1607558399,\"historical\":true,\"base\":\"EUR\",\"date\":\"2020-12-09\",\"rates\":{\"RSD\":117.579248}}";

        double rate = extractRateAndConvertToDouble(response);

        if(fromCurrency.equals("EUR")) return quantity * rate;
        else return  quantity / rate;
    }

    //prima responseEntity.getBody() i iz njega izvlaci vrednost valute u koju konvertujemo
     private double extractRateAndConvertToDouble(String response) {
        String[] split = response.split(":");

        String value = split[split.length-1].replace("}", "");

        double rate = Double.parseDouble(value);

        return rate;
    }
}
