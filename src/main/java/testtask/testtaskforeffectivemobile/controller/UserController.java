package testtask.testtaskforeffectivemobile.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import testtask.testtaskforeffectivemobile.dto.user.UserCreateDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserUpdateDTO;
import testtask.testtaskforeffectivemobile.service.UserService;
import testtask.testtaskforeffectivemobile.utils.UserUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserUtils userUtils;
    private static final String CURRENT_USER = "@userUtils.getCurrentUser().getId() == #id";

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> index() {
        List<UserDTO> users = userService.getAll();
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(users.size()))
            .body(users);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO userData)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserDTO user = userService.create(userData);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> show(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(CURRENT_USER)
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserUpdateDTO userData, @PathVariable Long id) {
        UserDTO user = userService.update(userData, id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserDTO> updateUserContactInfo(@PathVariable Long id,
                                                         @RequestBody UserUpdateDTO userUpdateDTO) {
        UserDTO user = userService.updateUserContactInfo(id, userUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(CURRENT_USER)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
