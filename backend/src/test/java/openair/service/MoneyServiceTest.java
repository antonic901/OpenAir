package openair.service;


import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MoneyServiceTest {

    @Spy
    @InjectMocks
    private MoneyService moneyService;

    @Test
    void testConvert() {
        double eurToRsd = moneyService.convert("2020-12-11", "EUR", "RSD", 20);
        double rsdToEur = moneyService.convert("2020-12-11", "RSD", "EUR", 12000);

        assertThat(eurToRsd).isCloseTo(2400, Percentage.withPercentage(5));
        assertThat(rsdToEur).isCloseTo(100, Percentage.withPercentage(3));
    }

}
