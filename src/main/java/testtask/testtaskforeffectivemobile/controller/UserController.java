package testtask.testtaskforeffectivemobile.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import testtask.testtaskforeffectivemobile.dto.TransferRequestDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserCreateDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserParamsDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserUpdateDTO;
import testtask.testtaskforeffectivemobile.service.BankAccountService;
import testtask.testtaskforeffectivemobile.service.UserService;
import testtask.testtaskforeffectivemobile.utils.UserUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserUtils userUtils;
    private static final String CURRENT_USER = "@userUtils.getCurrentUser().getId() == #id";
    private final BankAccountService bankAccountService;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<UserDTO>> index(
        @ModelAttribute UserParamsDTO userParamsDTO,
        @RequestParam(defaultValue = "1") int pageNumber
    ) {
        Page<UserDTO> users = userService.getAll(userParamsDTO, pageNumber);
        return ResponseEntity.ok()
//            .header("X-Total-Count", String.valueOf(users.size()))
            .body(users);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO userData)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserDTO user = userService.create(userData);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @PostMapping("/{id}/bankAccount/transfer")
    @PreAuthorize(CURRENT_USER)
    public ResponseEntity<?> transferMoney(
        @RequestBody TransferRequestDTO transferRequestDTO,
        @PathVariable Long id
    ) {
        bankAccountService.transfer(transferRequestDTO);
        return ResponseEntity.ok().build();
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
    @PreAuthorize(CURRENT_USER)
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

    @DeleteMapping("/{id}/phone-numbers/{phoneNumberId}")
    @PreAuthorize(CURRENT_USER)
    public ResponseEntity<?> deletePhoneNumber(@PathVariable Long id,
                                               @PathVariable Long phoneNumberId) {
        userService.deletePhoneNumber(id, phoneNumberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/emails/{emailId}")
    @PreAuthorize(CURRENT_USER)
    public ResponseEntity<?> deleteEmail(@PathVariable Long id,
                                         @PathVariable Long emailId) {
        userService.deleteEmail(id, emailId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/phone-numbers")
    @PreAuthorize(CURRENT_USER)
    public ResponseEntity<?> deletePhoneNumber(@PathVariable Long id,
                                               @RequestParam String phoneNumberValue) {
        userService.deletePhoneNumber(id, phoneNumberValue);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/emails")
    @PreAuthorize(CURRENT_USER)
    public ResponseEntity<?> deleteEmail(@PathVariable Long id,
                                         @RequestParam String emailValue) {
        userService.deleteEmail(id, emailValue);
        return ResponseEntity.ok().build();
    }
}
