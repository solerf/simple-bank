package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
public class ClientToken {

	private final Integer clientId;
	private final String clientEmail;
	private final String clientName;
	private final Set<Integer> accounts;
	private final String accessToken;
	private final Date creationTimestamp;

	public ClientToken(final Client client, final Set<Integer> accountsId) {
		this.clientId = client.getId();
		this.clientEmail = client.getEmail();
		this.clientName = client.getName();
		this.accounts = accountsId;
		this.accessToken = UUID.randomUUID().toString();
		this.creationTimestamp = Calendar.getInstance().getTime();
	}

	@JsonIgnore
	public Integer getClientId() {
		return clientId;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public String getClientName() {
		return clientName;
	}

	public Set<Integer> getAccounts() {
		return Collections.unmodifiableSet(accounts);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}
}
