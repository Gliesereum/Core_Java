package com.gliesereum.karma;

import com.gliesereum.karma.model.repository.es.CarWashEsRepository;
import com.gliesereum.karma.model.repository.es.ClientEsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Boots the whole service against a real Postgres.
 *
 * This is deliberately not a "does nothing" smoke test: the run applies all 71
 * Flyway migrations in the module and then Hibernate's `ddl-auto: validate`
 * compares each entity mapping against the resulting schema. A missing column,
 * a renamed table, a type the dialect no longer maps the same way, or a bean
 * that stops being constructible all fail here.
 *
 * The two Elasticsearch repositories are mocked out. Spring Data builds them
 * eagerly and pings the cluster while doing so, and the Elasticsearch 6.4 image
 * this project pins has no arm64 build, so a container would not start
 * everywhere. That leaves the ES layer uncovered here; it is rewritten off the
 * abandoned Jest client separately, and gets its own tests there.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApplicationContextTest {

    @MockBean
    private CarWashEsRepository carWashEsRepository;

    @MockBean
    private ClientEsRepository clientEsRepository;

    @Test
    public void contextLoadsAndMigrationsMatchTheEntityMappings() {
    }
}
