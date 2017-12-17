package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
@Entity
@Table(name = "tb_acct_movements")
public class AccountMovement implements Serializable {

	private Integer id;
	private Account account;
	private OperationType type;
	private Client client;
	private BigDecimal amount;
	private Date operationTimestamp;

	public AccountMovement(){

	}

	public AccountMovement(OperationType type, Account intoAccount, BigDecimal amount, Client fromClient) {
		this.account = intoAccount;
		this.type = type;
		this.client = fromClient;
		this.amount = amount;
	}

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

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID")
	@JsonIgnore
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE")
	@JsonIgnore
	public OperationType getType() {
		return type;
	}

	@Transient
	public String getOperation(){
		return type.getValue();
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	@OneToOne
	@JoinColumn(name =  "CLIENT_ID")
	@JsonIgnore
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Transient
	public String getFromClient(){
		return String.format("%s, %s", this.client.getName(), this.client.getEmail());
	}

	@Column(precision = 10, scale = 2)
	@JoinColumn(name =  "AMOUNT")
	@JsonIgnore
	public BigDecimal getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	@Transient
	public String getFormattedAmount(){
		String sign = (OperationType.WITHDRAW.equals(this.type) ? "-" : "+");
		return String.format("%s%.2f", sign, this.amount);
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(insertable = false, updatable = false, name = "OPERATION_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getOperationTimestamp() {
		return operationTimestamp;
	}

	public void setOperationTimestamp(Date operationTimestamp) {
		this.operationTimestamp = operationTimestamp;
	}
}
