package com.gliesereum.license;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void contextLoadsAndMigrationsMatchTheEntityMappings() {
    }
}
