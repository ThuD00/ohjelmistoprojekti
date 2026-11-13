package ohjelmistoprojekti.lipputoimisto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthenticationTest {

    private static final RequestPostProcessor MYYJA =
            httpBasic("myyja", "password");
    private static final RequestPostProcessor JARJESTAJA =
            httpBasic("jarjestaja", "password");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "password");

    @Autowired
    private MockMvc mockMvc;

    // lippujen haku

    @Test
    void getTicketWithoutAuthenticationReturns401() throws Exception {
        mockMvc.perform(get("/api/liput")
                        .param("code", "ABCDEF-123456"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getTicketAsJarjestajaReturns403() throws Exception {
        mockMvc.perform(get("/api/liput")
                        .with(JARJESTAJA)
                        .param("code", "ABCDEF-123456"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTicketAsMyyjaReturns200() throws Exception {
        mockMvc.perform(get("/api/liput")
                        .with(MYYJA)
                        .param("code", "ABCDEF-123456"))
                .andExpect(status().isOk());
    }

    // admin endpointit

    @Test
    void getAllUsersWithoutAuthenticationReturns401() throws Exception {
        mockMvc.perform(get("/api/kayttajat"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllUsersAsMyyjaReturns403() throws Exception {
        mockMvc.perform(get("/api/kayttajat")
                        .with(MYYJA))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsersAsJarjestajaReturns403() throws Exception {
        mockMvc.perform(get("/api/kayttajat")
                        .with(JARJESTAJA))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsersAsAdminReturns200() throws Exception {
        mockMvc.perform(get("/api/kayttajat")
                        .with(ADMIN))
                .andExpect(status().isOk());
    }

    // tapahtumat

    @Test
    void getMyEventsWithoutAuthenticationReturns401() throws Exception {
        mockMvc.perform(get("/api/tapahtumat/minun"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyEventsWithAuthenticationReturns200() throws Exception {
        mockMvc.perform(get("/api/tapahtumat/minun")
                        .with(JARJESTAJA))
                .andExpect(status().isOk());
    }

}

