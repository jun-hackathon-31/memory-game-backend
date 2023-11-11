package hackathon.code.controller;

import hackathon.code.dto.UserCreateDTO;
import hackathon.code.dto.UserDTO;
import hackathon.code.dto.UserUpdateDTO;
import hackathon.code.exception.ResourceNotFoundException;
import hackathon.code.mapper.UserMapper;
import hackathon.code.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hackathon.code.controller.UsersController.USER_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
@AllArgsConstructor
public class UsersController {

    public static final String USER_CONTROLLER_PATH = "/users";

    public static final String ID = "/{id}";

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        var user =  userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s not found", id)));
        return userMapper.map(user);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> index() {
        var users = userRepository.findAll().stream()
                .map(userMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody final UserCreateDTO userData) {
        var user = userMapper.map(userData);
        userRepository.save(user);

        return userMapper.map(user);
    }

    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@RequestBody @Valid UserUpdateDTO userData, @PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s not found", id)));
        userMapper.update(userData, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
