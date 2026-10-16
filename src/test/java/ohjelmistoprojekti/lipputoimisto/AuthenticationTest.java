package ohjelmistoprojekti.lipputoimisto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {

    private static final RequestPostProcessor USER1 =
            httpBasic("user", "user");
    private static final RequestPostProcessor USER2 =
            httpBasic("user2", "");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "admin");

    // TODO: kirjoita spring securityn testit
    @Autowired
    private MockMvc mockMvc;

}
