package hackathon.code.controller;

import hackathon.code.dto.RoundCreateDTO;
import hackathon.code.dto.RoundDTO;
import hackathon.code.exception.ResourceNotFoundException;
import hackathon.code.mapper.RoundMapper;
import hackathon.code.repository.RoundRepository;
import hackathon.code.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hackathon.code.controller.RoundsController.ROUND_CONTROLLER_PATH;

@Tag(name = "Rounds controller", description = "Manages user rounds")
@RestController
@RequestMapping("${base-url}" + ROUND_CONTROLLER_PATH)
@AllArgsConstructor
public class RoundsController {

    public static final String ROUND_CONTROLLER_PATH = "/rounds";

    public static final String ID = "/{id}";

    private final RoundRepository roundRepository;

    private final UserRepository userRepository;

    private final RoundMapper roundMapper;

    @Operation(summary = "Get a round by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the round",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoundDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Round with that id not found",
                    content = @Content) })
    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public RoundDTO show(
            @Parameter(description = "Id of round to be searched")
            @PathVariable Long id) {
        var round = roundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Round with id %s not found", id)));
        return roundMapper.map(round);
    }

    @Operation(summary = "Get list of all rounds")
    @ApiResponse(responseCode = "200", description = "List of all rounds",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RoundDTO.class)) })
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

    @Operation(summary = "Create new round")
    @ApiResponse(responseCode = "201", description = "Round created",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RoundDTO.class)) })
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public RoundDTO create(
            @Parameter(description = "Round data to save")
            @Valid @RequestBody RoundCreateDTO roundData) {
        var round = roundMapper.map(roundData);

        var gamerName = roundData.getUserName();
        var gamer = userRepository.findByName(gamerName).get();

        round.setGamer(gamer);
        roundRepository.save(round);

        return roundMapper.map(round);
    }

    @Operation(summary = "Delete round by its id")
    @ApiResponse(responseCode = "204", description = "Round deleted", content = @Content)
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @Parameter(description = "Id of round to be deleted")
            @PathVariable Long id) {
        roundRepository.deleteById(id);
    }
}
