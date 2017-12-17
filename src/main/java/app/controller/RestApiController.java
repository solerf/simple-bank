package app.controller;

import app.controller.response.ApiResponse;
import app.domain.ClientToken;
import app.domain.OperationResult;
import app.service.AccountService;
import app.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
@RestController
public class RestApiController {

	@Autowired private ClientService clientService;
	@Autowired private AccountService accountService;

	@RequestMapping(path = "/login", method = POST)
	public ResponseEntity<ApiResponse> login(@RequestParam String email, @RequestParam String password) {
		try {
			ClientToken clientToken = clientService.login(email, password);
			return new ResponseEntity<>(ApiResponse.success(clientToken), OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponse.error(e.getMessage()), INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(path = "/account/{acctNumber}/deposit", method = POST)
	public ResponseEntity<ApiResponse> deposit(@RequestParam String token, @PathVariable Integer acctNumber, @RequestParam BigDecimal amount) {
		try {
			verifyAuthentication(token);
			validateAccountNumber(acctNumber);

			ClientToken clientToken = clientService.getToken(token);
			OperationResult result = accountService.deposit(clientToken.getClientId(), acctNumber, amount);
			return new ResponseEntity<>(ApiResponse.success(result), OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponse.error(e.getMessage()), INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(path = "/account/{acctNumber}/withdraw", method = POST)
	public ResponseEntity<ApiResponse> withdraw(@RequestParam String token, @PathVariable Integer acctNumber, @RequestParam BigDecimal amount) {
		try {
			verifyAuthentication(token);
			validateAccountNumber(acctNumber);
			validateClientAccessToAccount(token, acctNumber);

			OperationResult result = accountService.withdraw(acctNumber, amount);
			return new ResponseEntity<>(ApiResponse.success(result), OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponse.error(e.getMessage()), INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(path = "/account/{acctNumber}", method = GET)
	public ResponseEntity<ApiResponse> account(@RequestHeader String token, @PathVariable Integer acctNumber) {
		try{
			verifyAuthentication(token);
			validateAccountNumber(acctNumber);
			validateClientAccessToAccount(token, acctNumber);

			OperationResult result = accountService.details(acctNumber);
			return new ResponseEntity<>(ApiResponse.success(result), OK);
		} catch (Exception e){
			return new ResponseEntity<>(ApiResponse.error(e.getMessage()), INTERNAL_SERVER_ERROR);
		}
	}

	private void validateClientAccessToAccount(String token, Integer acctNumber){
		ClientToken clientToken = clientService.getToken(token);
		if(!clientToken.getAccounts().contains(acctNumber)){
			throw new RuntimeException("Operation forbidden, you don't have access to the account");
		}
	}

	private void validateAccountNumber(Integer acctNumber){
		if(!accountService.isValid(acctNumber)){
			throw new RuntimeException("Invalid account number");
		}
	}

	private void verifyAuthentication(final String token) {
		if(!clientService.isValid(token)) {
			throw new RuntimeException("Operation forbidden, please do login");
		}
	}

}
