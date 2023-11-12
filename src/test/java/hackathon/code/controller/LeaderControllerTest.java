package hackathon.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackathon.code.model.Leader;
import hackathon.code.repository.LeaderRepository;
import hackathon.code.repository.RoundRepository;
import hackathon.code.repository.UserRepository;
import hackathon.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class LeaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaderRepository leaderRepository;

    @Autowired
    private RoundRepository roundRepository;

    private Leader testLeader;


    @BeforeEach
    public void setUp() {
        var testRound = Instancio.of(modelGenerator.getRoundModel())
                .create();
        var testUser = userRepository.findByName("admin")
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
        testRound.setGamer(testUser);
        roundRepository.save(testRound);

        testLeader = Instancio.of(Leader.class)
                .ignore(Select.field(Leader::getId))
                .supply(Select.field(Leader::getUser), () -> testUser)
                .supply(Select.field(Leader::getMoves), testRound::getMoves)
                .create();
    }

//    @Test
//    public void testIndex() throws Exception {
//        leaderRepository.save(testLeader);
//
//        var request = get("/api/leaderboard");
//        var result = mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andReturn();
//
//        var body = result.getResponse().getContentAsString();
//        assertThatJson(body).isArray();
//    }
}
