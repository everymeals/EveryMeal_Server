package everymeal.server.global;


import everymeal.server.ServerApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Rollback
@SpringBootTest(classes = ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTestSupport {}
