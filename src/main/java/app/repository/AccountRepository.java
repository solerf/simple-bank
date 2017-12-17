package app.repository;

import app.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {

	Account findByAccountNumber(@Param(value = "acct_number") Integer toAccountNumber);
}
