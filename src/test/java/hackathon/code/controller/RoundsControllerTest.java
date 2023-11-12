package hackathon.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackathon.code.mapper.RoundMapper;
import hackathon.code.model.Round;
import hackathon.code.repository.RoundRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoundsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoundMapper roundMapper;

    private Round testRound;

    @BeforeEach
    public void setUp() {
        testRound = Instancio.of(modelGenerator.getRoundModel())
                .create();
        var testUser = userRepository.findByName("admin")
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
        testRound.setGamer(testUser);
    }

    @Test
    public void testIndex() throws Exception {
        roundRepository.save(testRound);

        var request = get("/api/rounds");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        roundRepository.save(testRound);

        var request = get("/api/rounds/{id}", testRound.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("userName").isEqualTo(testRound.getGamer().getName()),
                v -> v.node("complexity").isEqualTo(testRound.getComplexity()),
                v -> v.node("moves").isEqualTo(testRound.getMoves())
        );
    }

    @Test
    public void testShowTaskNotFound() throws Exception {
        Long id = 100L;
        roundRepository.deleteById(id);

        var request = get("/api/rounds/{id}", id);
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        var testUser = userRepository.findByName("admin")
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        var data = Map.of(
                "complexity", 4,
                "moves", faker.number().numberBetween(8, 30),
                "userName", testUser.getName()
        );

        var request = post("/api/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateWithInvalidComplexity() throws Exception {
        var testUser = userRepository.findByName("admin")
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        var data = Map.of(
                "moves", faker.number().numberBetween(8, 30),
                "userName", testUser.getName()
        );

        var request = post("/api/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWithInvalidMoves() throws Exception {
        var testUser = userRepository.findByName("admin")
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        var data = Map.of(
                "complexity", 4,
                "userName", testUser.getName()
        );

        var request = post("/api/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDestroy() throws Exception {
        roundRepository.save(testRound);

        var request = delete("/api/rounds/{id}", testRound.getId());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        testRound = roundRepository.findById(testRound.getId()).orElse(null);
        assertThat(testRound).isNull();

    }
}
