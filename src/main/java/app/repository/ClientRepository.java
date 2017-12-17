package app.repository;

import app.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {

	Client findByEmailAndPassword(@Param(value = "email") String email, @Param(value = "password") String password);

}
