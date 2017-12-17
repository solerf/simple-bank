package app.service;

import app.domain.*;
import app.repository.AccountMovementRepository;
import app.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
@Service
public class AccountService {

	@Autowired private AccountRepository accountRepository;
	@Autowired private AccountMovementRepository accountMovementRepository;

	public OperationResult deposit(Integer fromClientId, Integer intoAccountNumber, BigDecimal amount) {
		validateAmount(amount, OperationType.DEPOSIT);

		Account account = accountRepository.findByAccountNumber(intoAccountNumber);
		AccountMovement at = new AccountMovement(OperationType.DEPOSIT, account, amount, new Client(fromClientId));
		at = accountMovementRepository.save(at);

		return new OperationResult(OperationType.DEPOSIT, at.getAmount());
	}

	private void validateAmount(BigDecimal amount, OperationType operation) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException(String.format("Invalid amount to %s", operation.getValue()));
	}

	public OperationResult details(Integer fromAccountNumber){
		Account account = accountRepository.findByAccountNumber(fromAccountNumber);
		account.calculateCurrentBalance();
		return new OperationResult(OperationType.STATEMENT, account);
	}

	public OperationResult withdraw(Integer fromAccountNumber, BigDecimal amount) {
		validateAmount(amount, OperationType.WITHDRAW);

		Account account = accountRepository.findByAccountNumber(fromAccountNumber);
		validateCurrentBalance(account, amount);

		AccountMovement at = new AccountMovement(OperationType.WITHDRAW, account, amount, account.getClient());
		at = accountMovementRepository.save(at);

		return new OperationResult(OperationType.WITHDRAW, at.getAmount());
	}

	private void validateCurrentBalance(Account account, BigDecimal amount) {
		account.calculateCurrentBalance();
		if (account.getCurrentBalance().compareTo(amount) < 0){
			throw new RuntimeException("Not enough funds to withdraw the desired amount");
		}
	}

	public boolean isValid(Integer acctNumber) {
		Account account = accountRepository.findByAccountNumber(acctNumber);
		return account != null;
	}
}
