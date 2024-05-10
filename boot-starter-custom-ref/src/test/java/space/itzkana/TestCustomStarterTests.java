package space.itzkana;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import space.itzkana.starter.ref.TestCustomStarter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestCustomStarter.class)
class TestCustomStarterTests {

    @Resource
    space.itzkana.starter.service.CustomService customService;

    @Test
    void contextLoads() {
        assertNotNull(customService);
        assertNotNull(customService.name());
        assertFalse(customService.name().isEmpty());
    }

}
