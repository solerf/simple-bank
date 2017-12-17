package app.service;

import app.domain.*;
import app.repository.AccountMovementRepository;
import app.repository.AccountRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@Mock private AccountRepository accountRepository;
	@Mock private AccountMovementRepository accountMovementRepository;

	@Mock private Account account;
	@Mock private Client client;
	@Mock private AccountMovement accountMovement;

	@InjectMocks
	private AccountService accountService = new AccountService();

	@Rule public ExpectedException expectedException = ExpectedException.none();

	private Integer clientId = 1;
	private Integer acctNumber = 55900;

	@Before
	public void before(){
		when(client.getId()).thenReturn(clientId);
		when(accountRepository.findByAccountNumber(eq(acctNumber))).thenReturn(account);
	}

	@Test
	public void test_deposit_valid_amount() throws Exception {
		when(accountMovement.getType()).thenReturn(OperationType.DEPOSIT);
		when(accountMovement.getAmount()).thenReturn(new BigDecimal("100"));
		when(accountMovementRepository.save(any(AccountMovement.class))).thenReturn(accountMovement);

		OperationResult result = accountService.deposit(clientId, acctNumber, new BigDecimal("100"));

		assertThat(result, is(notNullValue()));
		assertThat(result.getType(), is(OperationType.DEPOSIT.getValue()));
		assertThat(result.getResult(), is(new BigDecimal("100")));
	}

	@Test
	public void test_deposit_invalid_amount() throws Exception {
		expectedException.expect(Exception.class);
		expectedException.expectMessage("Invalid amount to Deposit");
		accountService.deposit(clientId, acctNumber, new BigDecimal("-50"));

		expectedException.expect(Exception.class);
		expectedException.expectMessage("Invalid amount to Deposit");
		accountService.deposit(clientId, acctNumber, BigDecimal.ZERO);
	}

	@Test
	public void test_withdraw_amount() throws Exception {
		when(account.getCurrentBalance()).thenReturn(new BigDecimal("150"));
		when(accountMovement.getType()).thenReturn(OperationType.WITHDRAW);
		when(accountMovement.getAmount()).thenReturn(new BigDecimal("120"));
		when(accountMovementRepository.save(any(AccountMovement.class))).thenReturn(accountMovement);

		OperationResult result = accountService.withdraw(acctNumber, new BigDecimal("120"));

		assertThat(result, is(notNullValue()));
		assertThat(result.getType(), is(OperationType.WITHDRAW.getValue()));
		assertThat(result.getResult(), is(new BigDecimal("120")));
	}

	@Test
	public void test_withdraw_invalid_amount() throws Exception {
		expectedException.expect(Exception.class);
		expectedException.expectMessage("Invalid amount to Withdraw");
		accountService.withdraw(acctNumber, new BigDecimal("-50"));

		expectedException.expect(Exception.class);
		expectedException.expectMessage("Invalid amount to Withdraw");
		accountService.withdraw( acctNumber, BigDecimal.ZERO);
	}

	@Test
	public void test_withdraw_from_account_with_not_enough_funds() throws Exception {
		when(account.getCurrentBalance()).thenReturn(BigDecimal.ZERO);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("Not enough funds to withdraw the desired amount");

		accountService.withdraw(acctNumber, new BigDecimal("120"));
	}

	@Test
	public void test_get_statement_and_balance(){
		AccountMovement movement_1 = mock(AccountMovement.class);
		when(movement_1.getAmount()).thenReturn(new BigDecimal("100"));
		when(movement_1.getType()).thenReturn(OperationType.DEPOSIT);
		when(movement_1.getAccount()).thenReturn(account);
		when(movement_1.getClient()).thenReturn(client);

		AccountMovement movement_2 = mock(AccountMovement.class);
		when(movement_2.getAmount()).thenReturn(new BigDecimal("80"));
		when(movement_2.getType()).thenReturn(OperationType.WITHDRAW);
		when(movement_2.getAccount()).thenReturn(account);
		when(movement_2.getClient()).thenReturn(client);

		when(account.getCurrentBalance()).thenReturn(new BigDecimal("20"));
		when(account.getMovements()).thenReturn(Arrays.asList(new AccountMovement[]{movement_1, movement_2}));

		OperationResult result = accountService.details(acctNumber);
		Account accountResult = (Account) result.getResult();
		assertThat(result, is(notNullValue()));
		assertThat(result.getType(), is(OperationType.STATEMENT.getValue()));
		assertThat(result.getResult(), is(instanceOf(Account.class)));
		assertThat(accountResult.getMovements().size(), is(2));
		assertThat(accountResult.getCurrentBalance(), is(new BigDecimal("20")));
	}
}