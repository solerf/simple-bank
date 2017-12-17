package app.service;

import app.domain.Client;
import app.domain.ClientToken;
import app.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
@Service
public class ClientService {

	@Autowired private Map<String, ClientToken> clientTokenStore;
	@Autowired private ClientRepository clientRepository;

	public ClientToken login(String email, String password) {
		Client client = clientRepository.findByEmailAndPassword(email, password);
		if (client == null) {
			throw new RuntimeException("Sorry, email or password incorrect");
		}
		return generateToken(client);
	}

	public ClientToken generateToken(Client client) {
		Optional<ClientToken> optToken = getTokenByClientId(client.getId());

		if(!optToken.isPresent()){
			Set<Integer> accountsId = client.getAccounts().stream().map(a -> a.getAccountNumber()).collect(Collectors.toSet());
			ClientToken token = new ClientToken(client, accountsId);
			clientTokenStore.put(token.getAccessToken(), token);
			return token;
		}
		return optToken.get();
	}

	private Optional<ClientToken> getTokenByClientId(final Integer id) {
		List<Map.Entry<String, ClientToken>> filterResult = clientTokenStore.entrySet().stream()
				.filter((entry) -> id.equals(entry.getValue().getClientId()))
				.collect(Collectors.toList());
		return filterResult.isEmpty() ? Optional.empty() : Optional.of(filterResult.get(0).getValue());
	}

	public boolean isValid(String token) {
		ClientToken clientToken = clientTokenStore.get(token);
		return clientToken != null;
	}

	public ClientToken getToken(String token) {
		return clientTokenStore.get(token);
	}
}
