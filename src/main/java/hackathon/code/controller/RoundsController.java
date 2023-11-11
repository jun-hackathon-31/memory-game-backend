package hackathon.code.controller;

import hackathon.code.dto.RoundCreateDTO;
import hackathon.code.dto.RoundDTO;
import hackathon.code.exception.ResourceNotFoundException;
import hackathon.code.mapper.RoundMapper;
import hackathon.code.repository.RoundRepository;
import hackathon.code.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hackathon.code.controller.RoundsController.ROUND_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + ROUND_CONTROLLER_PATH)
@AllArgsConstructor
public class RoundsController {

    public static final String ROUND_CONTROLLER_PATH = "/rounds";

    public static final String ID = "/{id}";

    private final RoundRepository roundRepository;

    private final UserRepository userRepository;

    private final RoundMapper roundMapper;

    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public RoundDTO show(@PathVariable Long id) {
        var round = roundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Round with id %s not found", id)));
        return roundMapper.map(round);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RoundDTO>> index() {
        var rounds = roundRepository.findAll().stream()
                .map(roundMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(rounds.size()))
                .body(rounds);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public RoundDTO create(@Valid @RequestBody RoundCreateDTO roundData) {
        var round = roundMapper.map(roundData);

        var gamerName = roundData.getUserName();
        var gamer = userRepository.findByName(gamerName).get();

        round.setGamer(gamer);
        roundRepository.save(round);

        return roundMapper.map(round);
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        roundRepository.deleteById(id);
    }
}
