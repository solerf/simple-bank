package app.service;

import app.domain.Client;
import app.domain.ClientToken;
import app.repository.ClientRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {

	@Mock private ClientRepository clientRepository;
	@Mock private Map<String, ClientToken> clientTokenStore;
	@Mock private Client mary;
	@Mock private ClientToken maryToken;

	@InjectMocks
	private ClientService clientService = new ClientService();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private String token = UUID.randomUUID().toString();
	private Integer clientId = 1;
	private String clientEmail = "mary@marycompany.com";
	private String clientPassword = "marymary";
	private String clientName = "Mary";

	@Before
	public void before(){
		mary = mockClient();
		when(clientRepository.findByEmailAndPassword(eq(clientEmail), eq(clientPassword))).thenReturn(mary);

		maryToken = mockToken();

		Map<String, ClientToken> mockMap = new HashMap<>();
		mockMap.put(token, maryToken);

		when(maryToken.getClientId()).thenReturn(clientId);
		when(clientTokenStore.get(eq(token))).thenReturn(maryToken);
		when(clientTokenStore.entrySet()).thenReturn(mockMap.entrySet());
	}

	private ClientToken mockToken() {
		maryToken = mock(ClientToken.class);
		when(maryToken.getClientId()).thenReturn(clientId);
		when(maryToken.getClientName()).thenReturn(clientName);
		when(maryToken.getAccessToken()).thenReturn(token);
		return maryToken;
	}

	private Client mockClient() {
		mary = mock(Client.class);
		when(mary.getName()).thenReturn(clientName);
		when(mary.getId()).thenReturn(clientId);
		return mary;
	}

	@Test
	public void test_login_valid_client() throws Exception {
		ClientToken clientToken = clientService.login(clientEmail, clientPassword);

		assertThat(clientToken, notNullValue());
		assertThat(clientToken.getClientName(), is(equalTo(clientName)));
		assertThat(clientToken.getClientId(), is(equalTo(clientId)));
	}

	@Test
	public void test_login_client_already_logged() throws Exception {
		verify(clientTokenStore, never()).put(isA(String.class), isA(ClientToken.class));

		ClientToken clientToken =  clientService.login(clientEmail, clientPassword);

		assertThat(clientToken, notNullValue());
		assertThat(clientToken.getClientName(), is(equalTo(clientName)));
		assertThat(clientToken.getAccessToken(), is(equalTo(token)));
	}

	@Test
	public void test_login_invalid_client() throws Exception {
		expectedException.expect(Exception.class);
		expectedException.expectMessage(is(equalTo("Sorry, email or password incorrect")));

		clientService.login("mary@blablabla.com", clientPassword);
	}

	@Test
	public void test_logged_client_get_token(){
		ClientToken clientToken = clientService.login(clientEmail, clientPassword);
		String tkn = clientToken.getAccessToken();
		ClientToken sameClientToken = clientService.getToken(tkn);

		assertThat(sameClientToken, is(notNullValue()));
		assertThat(sameClientToken, is(clientToken));
	}

	@Test
	public void test_get_client_token_with_invalid_token(){
		ClientToken invalid =  clientService.getToken("");
		assertThat(invalid, nullValue());
	}

}