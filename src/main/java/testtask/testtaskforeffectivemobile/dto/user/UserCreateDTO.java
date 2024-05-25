package testtask.testtaskforeffectivemobile.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private String surname;
    private LocalDate birthDate;

    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private Double balance;
    @NotBlank
    private Set<String> phoneNumber;
    @NotBlank
    private Set<String> email;

}
