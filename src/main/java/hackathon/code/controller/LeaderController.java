package hackathon.code.controller;

import hackathon.code.dto.LeaderDTO;
import hackathon.code.mapper.LeaderMapper;
import hackathon.code.model.Leader;
import hackathon.code.repository.LeaderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

import static hackathon.code.controller.LeaderController.LEADER_CONTROLLER_PATH;

@Tag(name = "Users controller", description = "Interaction with leaders")
@RestController
@RequestMapping("${base-url}" + LEADER_CONTROLLER_PATH)
public class LeaderController {

    public static final String LEADER_CONTROLLER_PATH = "/leaderboard";

    @Autowired
    private LeaderRepository leaderRepository;

    @Autowired
    private LeaderMapper leaderMapper;

    @Operation(summary = "Get list of all leaders")
    @ApiResponse(responseCode = "200", description = "List of all leaders",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeaderDTO.class)) })
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LeaderDTO>> index() {
        var leaders = leaderRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Leader::getMoves))
                .map(leaderMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(leaders.size()))
                .body(leaders);
    }
}
