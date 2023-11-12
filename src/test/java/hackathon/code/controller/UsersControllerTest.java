package hackathon.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackathon.code.model.User;
import hackathon.code.repository.UserRepository;
import hackathon.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

//    private JwtRequestPostProcessor token;

    private User testUser;

    @BeforeEach
    public void setUp() {
//        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        userRepository.save(testUser);
    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/users");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShowUserNotFound() throws Exception {
        Long id = 100L;
        userRepository.deleteById(id);

        var request = get("/api/users/{id}", id);
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/users/{id}", testUser.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testUser.getName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var data = Map.of(
                "name", faker.name().fullName()
        );

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByName(data.get("name")).get();

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(data.get("name"));
    }

    @Test
    public void testCreateWithInvalidName() throws Exception {
        var data = Map.of(
                "name", ""
        );

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = Map.of(
                "name", faker.name().fullName()
        );

        var request = put("/api/users/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedUser = userRepository.findById(testUser.getId()).get();

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo(data.get("name"));
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/users/{id}", testUser.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        testUser = userRepository.findById(testUser.getId()).orElse(null);
        assertThat(testUser).isNull();
    }
}
