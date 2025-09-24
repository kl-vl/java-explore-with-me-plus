package ru.practicum.stats;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StatsServerApplicationTests {

    @Test
    void mainContextTest() {
        // контекст Spring успешно загружается
        assertDoesNotThrow(() -> StatsServerApplication.main(new String[]{}));
    }
}