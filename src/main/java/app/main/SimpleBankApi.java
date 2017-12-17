package app.main;

import app.domain.ClientToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */

@SpringBootApplication
@ComponentScan(basePackages = "app")
public class SimpleBankApi {

	public static void main(String[] args) {
		SpringApplication.run(SimpleBankApi.class, args);
	}

	@Bean
	public Map<String, ClientToken> clientTokenStore(){
		return new HashMap<>();
	}
}