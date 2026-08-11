package com.gliesereum.proxy;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.exchange.service.auth.AuthExchangeService;
import com.gliesereum.share.common.exchange.service.permission.ApplicationExchangeService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Describes what the gateway promises to every caller of the platform, stated
 * in terms of HTTP rather than of whichever proxy implementation is underneath.
 *
 * Everything here is asserted from the outside: a request goes in on a real
 * port, and the assertions are about the status the caller sees and the request
 * the downstream service receives. That is deliberate — these tests were
 * written against the Zuul implementation so they could keep holding it to the
 * same contract after it was replaced.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GatewayContractTest {

    private static final String APPLICATION_ID_HEADER = "Application-Id";
    private static final String SERVICE_AUTHORIZATION_HEADER = "Service-Authorization";

    private static MockWebServer downstream;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @MockBean
    private ApplicationExchangeService applicationExchangeService;

    @MockBean
    private AuthExchangeService authExchangeService;

    @BeforeClass
    public static void startDownstream() throws IOException {
        downstream = new MockWebServer();
        downstream.start();
        // Read by zuul.routes.stub-service.url in application-test.yml. Set here
        // because the port is only known once the server is listening, and the
        // Spring context is not built until the first test instance.
        System.setProperty("downstream.url", "http://localhost:" + downstream.getPort());
    }

    @AfterClass
    public static void stopDownstream() throws IOException {
        downstream.shutdown();
        System.clearProperty("downstream.url");
    }

    @Before
    public void stubApplicationLookup() {
        ApplicationDto application = new ApplicationDto();
        application.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        when(applicationExchangeService.check(any(UUID.class))).thenReturn(application);
    }

    private String url(String path) {
        return "http://localhost:" + port + "/api" + path;
    }

    private ResponseEntity<String> get(String path, HttpHeaders headers) {
        return rest.exchange(url(path), HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    private HttpHeaders withApplicationId() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(APPLICATION_ID_HEADER, "11111111-1111-1111-1111-111111111111");
        return headers;
    }

    @Test
    public void rejectsARequestThatCarriesNoApplicationId() throws InterruptedException {
        ResponseEntity<String> response = get("/stub/v1/thing", new HttpHeaders());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue("expected error code 1310 (ApplicationId required), got: " + response.getBody(),
                response.getBody().contains("\"code\":1310"));
        assertNull("the downstream service must not be reached", downstream.takeRequest(1, SECONDS));
    }

    @Test
    public void rejectsAnApplicationIdThatIsNotAUuid() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(APPLICATION_ID_HEADER, "not-a-uuid");

        ResponseEntity<String> response = get("/stub/v1/thing", headers);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue("expected error code 1313 (Application id type not valid), got: " + response.getBody(),
                response.getBody().contains("\"code\":1313"));
        assertNull(downstream.takeRequest(1, SECONDS));
    }

    @Test
    public void forwardsToTheDownstreamServiceWithTheRoutePrefixStripped() throws InterruptedException {
        downstream.enqueue(new MockResponse().setResponseCode(200).setBody("{\"ok\":true}"));

        ResponseEntity<String> response = get("/stub/v1/thing/42", withApplicationId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"ok\":true}", response.getBody());

        RecordedRequest forwarded = downstream.takeRequest(5, SECONDS);
        assertNotNull("downstream was never called", forwarded);
        assertEquals("/thing/42", forwarded.getPath());
    }

    /**
     * Downstream services trust Service-Authorization, not the caller's bearer
     * token; the gateway is the only thing that mints it. If this stops being
     * attached, every service behind the gateway starts seeing anonymous calls.
     */
    @Test
    public void mintsAServiceAuthorizationHeaderForTheDownstreamService() throws InterruptedException {
        downstream.enqueue(new MockResponse().setResponseCode(200));

        get("/stub/v1/thing", withApplicationId());

        RecordedRequest forwarded = downstream.takeRequest(5, SECONDS);
        assertNotNull(forwarded);
        String serviceAuthorization = forwarded.getHeader(SERVICE_AUTHORIZATION_HEADER);
        assertNotNull("no Service-Authorization was minted", serviceAuthorization);
        assertTrue("expected a JWT-prefixed token, got: " + serviceAuthorization,
                serviceAuthorization.startsWith("JWT "));
    }

    @Test
    public void resolvesTheBearerTokenAndCarriesTheUserDownstream() throws InterruptedException {
        UUID userId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UserDto user = new UserDto();
        user.setId(userId);
        AuthDto auth = new AuthDto();
        auth.setUser(user);
        when(authExchangeService.checkAccessToken("token-abc")).thenReturn(auth);

        downstream.enqueue(new MockResponse().setResponseCode(200));

        HttpHeaders headers = withApplicationId();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer token-abc");
        get("/stub/v1/thing", headers);

        RecordedRequest forwarded = downstream.takeRequest(5, SECONDS);
        assertNotNull(forwarded);
        String jwt = forwarded.getHeader(SERVICE_AUTHORIZATION_HEADER);
        assertNotNull(jwt);
        assertTrue("the minted JWT should carry the resolved user",
                payloadOf(jwt).contains(userId.toString()));
    }

    /** `status` is in notRequiredApplicationIdHosts, so it must stay open. */
    @Test
    public void servesStatusWithoutAnApplicationId() {
        ResponseEntity<String> response = get("/status", new HttpHeaders());

        // 2xx rather than 200: with no Eureka behind it the controller reports an
        // empty service list. What matters is that it is not turned away.
        assertTrue("status must stay open, got " + response.getStatusCode(),
                response.getStatusCode().is2xxSuccessful());
    }

    private String payloadOf(String serviceAuthorizationHeader) {
        String token = serviceAuthorizationHeader.substring("JWT ".length()).trim();
        String payload = token.split("\\.")[1];
        return new String(java.util.Base64.getUrlDecoder().decode(payload));
    }
}
