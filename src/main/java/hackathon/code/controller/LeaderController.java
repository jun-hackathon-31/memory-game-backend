package hackathon.code.controller;

import hackathon.code.dto.LeaderDTO;
import hackathon.code.dto.UserDTO;
import hackathon.code.mapper.LeaderMapper;
import hackathon.code.mapper.UserMapper;
import hackathon.code.repository.LeaderRepository;
import hackathon.code.repository.RoundRepository;
import hackathon.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

import static hackathon.code.controller.LeaderboardController.LEADERBOARD_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + LEADERBOARD_CONTROLLER_PATH)
public class LeaderboardController {

    public static final String LEADERBOARD_CONTROLLER_PATH = "/leaderboard";

    @Autowired
    private LeaderRepository leaderRepository;

    @Autowired
    private LeaderMapper leaderMapper;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LeaderDTO>> index() {
        var leaders = leaderRepository.findAll().stream()
                .sorted(Comparator.comparingInt(leader -> leader.getMoves()))
                .map(leaderMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(leaders.size()))
                .body(leaders);
    }
}
