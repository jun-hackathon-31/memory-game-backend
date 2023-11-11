//package hackathon.code.controller;
//
//import hackathon.code.dto.UserDTO;
//import hackathon.code.mapper.UserMapper;
//import hackathon.code.repository.RoundRepository;
//import hackathon.code.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Comparator;
//import java.util.List;
//
//import static hackathon.code.controller.LeaderboardController.LEADERBOARD_CONTROLLER_PATH;
//
//@RestController
//@RequestMapping("${base-url}" + LEADERBOARD_CONTROLLER_PATH)
//public class LeaderboardController {
//
//    public static final String LEADERBOARD_CONTROLLER_PATH = "/leaderboard";
//
//    @Autowired
//    private RoundRepository roundRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @GetMapping("")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<List<UserDTO>> index() {
//        var users = userRepository.findAll().stream()
//                .sorted(Comparator.comparingInt(user -> {
//                    var userName = user.getName();
//                    var round = roundRepository.findByUserName(userName).get();
//                    var moves = round.getMoves();
//                    return moves;
//                }))
//                .map(userMapper::map)
//                .toList();
//
//        return ResponseEntity.ok()
//                .header("X-Total-Count", String.valueOf(users.size()))
//                .body(users);
//    }
//}
