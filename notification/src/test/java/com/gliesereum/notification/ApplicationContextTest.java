package com.gliesereum.notification;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Boots the whole service against a real Postgres.
 *
 * This is deliberately not a "does nothing" smoke test: the run applies every
 * Flyway migration in the module and then Hibernate's `ddl-auto: validate`
 * compares each entity mapping against the resulting schema. A missing column,
 * a renamed table, a type the dialect no longer maps the same way, or a bean
 * that stops being constructible all fail here.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApplicationContextTest {

    private static final String CREDENTIAL_PATH_PROPERTY = "firebase.test.credential-path";

    private static Path credentials;

    /**
     * FirebaseAdminConfiguration builds a FirebaseApp during startup, and
     * GoogleCredentials insists on parsing a real PKCS#8 key out of the service
     * account file, so the context cannot come up without one.
     *
     * The file is generated here rather than committed: a checked-in
     * service-account JSON is indistinguishable from a leaked one, both to a
     * reader and to GitHub's push protection. This key is made on the spot, is
     * attached to no Google project, and goes away with the JVM.
     */
    @BeforeClass
    public static void generateThrowawayServiceAccount() throws IOException, NoSuchAlgorithmException {
        credentials = Files.createTempFile("gliesereum-firebase-test", ".json");
        credentials.toFile().deleteOnExit();
        Files.write(credentials, serviceAccountJson().getBytes(UTF_8));
        System.setProperty(CREDENTIAL_PATH_PROPERTY, "file:" + credentials.toAbsolutePath());
    }

    @AfterClass
    public static void removeThrowawayServiceAccount() throws IOException {
        System.clearProperty(CREDENTIAL_PATH_PROPERTY);
        Files.deleteIfExists(credentials);
    }

    @Test
    public void contextLoadsAndMigrationsMatchTheEntityMappings() {
    }

    private static String serviceAccountJson() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        String pem = "-----BEGIN PRIVATE KEY-----\n"
                + Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(keyPair.getPrivate().getEncoded())
                + "\n-----END PRIVATE KEY-----\n";

        return "{\n"
                + "  \"type\": \"service_account\",\n"
                + "  \"project_id\": \"gliesereum-test\",\n"
                + "  \"private_key_id\": \"0000000000000000000000000000000000000000\",\n"
                + "  \"private_key\": \"" + pem.replace("\n", "\\n") + "\",\n"
                + "  \"client_email\": \"test@gliesereum-test.iam.gserviceaccount.com\",\n"
                + "  \"client_id\": \"000000000000000000000\",\n"
                + "  \"token_uri\": \"https://oauth2.googleapis.com/token\"\n"
                + "}\n";
    }
}
