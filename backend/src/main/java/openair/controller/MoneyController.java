package openair.controller;

import openair.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/money", produces = MediaType.APPLICATION_JSON_VALUE)
public class MoneyController {

    private MoneyService moneyService;

    @Autowired
    public MoneyController(MoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @GetMapping("/convert/{date}/{fromCurrency}/{toCurrency}/{quantity}")
    public  ResponseEntity<Double> convert(@PathVariable String date, @PathVariable String fromCurrency, @PathVariable String toCurrency, @PathVariable double quantity) {
        return new ResponseEntity<Double>(moneyService.convert(date, fromCurrency, toCurrency, quantity), HttpStatus.OK);
    }


}
