package openair;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class OpenairApplication {

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper getModelMapper() { return new ModelMapper(); }

	public static void main(String[] args) {
		SpringApplication.run(OpenairApplication.class, args);
	}

}
