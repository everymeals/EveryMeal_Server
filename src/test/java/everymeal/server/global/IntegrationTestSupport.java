package everymeal.server.global;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

// @ActiveProfiles("test")
@Rollback
@SpringBootTest
public abstract class IntegrationTestSupport {}
