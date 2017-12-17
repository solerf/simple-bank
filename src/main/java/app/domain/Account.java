package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
@Entity
@Table(name = "tb_account")
public class Account {

	private Integer id;
	private Integer accountNumber;
	private Client client;
	private List<AccountMovement> movements;
	private BigDecimal currentBalance;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	@JsonIgnore
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "ACCT_NUMBER")
	public Integer getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	@ManyToOne
	@JoinColumn(name = "CLIENT_ID")
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@OneToMany(mappedBy = "account")
	@JsonIgnore
	public List<AccountMovement> getMovements() {
		return movements;
	}

	@Transient
	@JsonProperty("movements")
	public List<AccountMovement> getOrderedMovements() {
		return this.getMovements().stream()
				.sorted((mov1, mov2) -> mov2.getOperationTimestamp().compareTo(mov1.getOperationTimestamp())).collect(Collectors.toList());
	}

	public void setMovements(List<AccountMovement> accountMovements) {
		this.movements = accountMovements;
	}

	@Transient
	@JsonIgnore
	public BigDecimal getCurrentBalance() {
		return this.currentBalance;
	}

	@Transient
	@JsonProperty("currentBalance")
	public String getFormattedCurrentBalance() {
		return String.format("%.2f", this.currentBalance);
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public void calculateCurrentBalance(){
		BigDecimal totalDeposit = this.getMovements().stream()
				.filter(am -> OperationType.DEPOSIT.equals(am.getType()))
				.map(AccountMovement::getAmount)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);

		BigDecimal totalWithdraw = this.getMovements().stream()
				.filter(am -> OperationType.WITHDRAW.equals(am.getType()))
				.map(AccountMovement::getAmount)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);

		this.setCurrentBalance(totalDeposit.subtract(totalWithdraw));
	}

}
