package testtask.testtaskforeffectivemobile.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserUpdateDTO {
    private JsonNullable<String> firstName;
    private JsonNullable<String> lastName;
    private JsonNullable<String> surname;
    private JsonNullable<LocalDate> birthDate;

    private JsonNullable<Set<String>> phoneNumber;
    private JsonNullable<Set<String>> email;
}
