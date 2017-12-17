package app.repository;

import app.domain.AccountMovement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
public interface AccountMovementRepository extends JpaRepository<AccountMovement, Integer> {

}
