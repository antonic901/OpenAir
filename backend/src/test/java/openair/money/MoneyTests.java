package openair.money;


import openair.repository.MoneyRepository;
import openair.service.MoneyService;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MoneyTests {

    @Mock
    private MoneyRepository moneyRepository;

    @Mock
    private RestTemplate restTemplate;

    @Spy
    @InjectMocks
    private MoneyService moneyService;

    @Test
    public void testConvert() {
        double eurToRsd = moneyService.convert("2020-12-11", "EUR", "RSD", 20);
        double rsdToEur = moneyService.convert("2020-12-11", "RSD", "EUR", 12000);

        assertThat(eurToRsd).isCloseTo(2400, Percentage.withPercentage(5));
        assertThat(rsdToEur).isCloseTo(100, Percentage.withPercentage(3));
    }

}
