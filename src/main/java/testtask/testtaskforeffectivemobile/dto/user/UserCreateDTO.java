package testtask.testtaskforeffectivemobile.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    @Size(min = 2)
    private String login;
    @NotBlank
    @Size(min = 8, max = 24)
    private String password;
    @NotBlank
    private BigDecimal balance;
    @NotBlank
    private Set<String> phoneNumber;
    @NotBlank
    private Set<String> email;

}
