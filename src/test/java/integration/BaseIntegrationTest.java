package integration;

import com.zemoga.portfolio.ZemogaPortfolioApplication;
import com.zemoga.portfolio.model.Portfolio;
import com.zemoga.portfolio.repository.PortfolioRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZemogaPortfolioApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    protected String url;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LocalServerPort
    private int port;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Before
    public void prepareData() {
        url = "http://localhost" + ":" + this.port + this.contextPath;
        portfolioRepository.save(new Portfolio("1", "ronaldo", "image-url", "description", "title"));
        portfolioRepository.save(new Portfolio("2", "invalid user", "image2", "description2", "title2"));
    }

    @After
    public void cleanUpData() {
        portfolioRepository.deleteAll();
    }
}
