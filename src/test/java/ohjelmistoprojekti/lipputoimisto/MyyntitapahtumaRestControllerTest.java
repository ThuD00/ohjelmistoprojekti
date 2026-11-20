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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MyyntitapahtumaRestControllerTest {

        private static final String BASE_URL = "/api/myyntitapahtumat";

        private static final RequestPostProcessor MYYJA = httpBasic("myyja", "password");

        private static final RequestPostProcessor JARJESTAJA = httpBasic("jarjestaja", "password");

        private static final RequestPostProcessor ADMIN = httpBasic("admin", "password");

        @Autowired
        private MockMvc mockMvc;

        // GET /api/myyntitapahtumat

        @Test
        void getMyyntitapahtumatReturnsOk() throws Exception {
                mockMvc.perform(get(BASE_URL).with(ADMIN))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray());
        }

        @Test
        void getMyyntitapahtumatReturnsForbidden() throws Exception {
                mockMvc.perform(get(BASE_URL).with(MYYJA))
                                .andExpect(status().isForbidden());
        }

        @Test
        void getMyyntitapahtumatReturnsUnauthorized() throws Exception {
                mockMvc.perform(get(BASE_URL))
                                .andExpect(status().isUnauthorized());
        }

        // GET /api/myyntitapahtumat/{id}

        @Test
        void getMyyntitapahtumaByIdReturnsOk() throws Exception {
                mockMvc.perform(get(BASE_URL + "/1").with(MYYJA))
                                .andExpect(status().isOk());
        }

        @Test
        void getMyyntitapahtumaByIdReturnsNotFound() throws Exception {
                mockMvc.perform(get(BASE_URL + "/99999").with(MYYJA))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getMyyntitapahtumaByIdReturnsOkAsAdmin() throws Exception {
                mockMvc.perform(get(BASE_URL + "/1").with(ADMIN))
                                .andExpect(status().isOk());
        }

        @Test
        void getMyyntitapahtumaByIdReturnsForbidden() throws Exception {
                mockMvc.perform(get(BASE_URL + "/1").with(JARJESTAJA))
                                .andExpect(status().isForbidden());
        }

        // GET /api/myyntitapahtumat/minun

        @Test
        void getOmatMyyntitapahtumatReturnsOk() throws Exception {
                mockMvc.perform(get(BASE_URL + "/minun").with(MYYJA))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray());
        }

        @Test
        void getOmatMyyntitapahtumatReturnsForbidden() throws Exception {
                mockMvc.perform(get(BASE_URL + "/minun").with(JARJESTAJA))
                                .andExpect(status().isForbidden());
        }

        // GET /api/myyntitapahtumat/myyja/{myyjaId}

        @Test
        void getMyyjanMyyntitapahtumatReturnsOk() throws Exception {
                mockMvc.perform(get(BASE_URL + "/myyja/1").with(ADMIN))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray());
        }

        @Test
        void getMyyjanMyyntitapahtumatReturnsForbidden() throws Exception {
                mockMvc.perform(get(BASE_URL + "/myyja/1").with(MYYJA))
                                .andExpect(status().isForbidden());
        }

        @Test
        void getMyyjanMyyntitapahtumatReturnsUnauthorized() throws Exception {
                mockMvc.perform(get(BASE_URL + "/myyja/1"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void getMyyjanMyyntitapahtumatReturnsEmptyList() throws Exception {
                mockMvc.perform(get(BASE_URL + "/myyja/99999").with(ADMIN))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));
        }

}
