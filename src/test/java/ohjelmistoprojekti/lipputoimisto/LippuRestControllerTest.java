package ohjelmistoprojekti.lipputoimisto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
public class LippuRestControllerTest {
    private static final String BASE_URL = "/api/liput";

    private static final RequestPostProcessor USER1 =
            httpBasic("user", "user");
    private static final RequestPostProcessor USER2 =
            httpBasic("user2", "");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "admin");

    @Autowired
    private MockMvc mockMvc;

    // TODO: kirjoita testit
    @Test
    void reserveTicketReturnsCreated() throws Exception {

    }

    @Test
    void reserveTicketReturnsNotFound() throws Exception {

    }

    @Test
    void redeemTicketReturnsOk() throws Exception {

    }

    @Test
    void redeemTicketReturnsNotFound() throws Exception {

    }

    @Test
    void redeemTicketReturnsConflict() throws Exception {

    }

    @Test
    void cancelTicketReturnsOk() throws Exception {

    }

    @Test
    void cancelTicketReturnsNotFound() throws Exception {

    }

    @Test
    void cancelTicketReturnsConflict() throws Exception {

    }
}
