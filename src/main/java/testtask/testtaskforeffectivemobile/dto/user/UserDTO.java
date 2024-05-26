package testtask.testtaskforeffectivemobile.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    private JsonNullable<String> firstName;
    private JsonNullable<String> lastName;
    private JsonNullable<String> surname;
    private JsonNullable<LocalDate> birthDate;
    private JsonNullable<String> login;
    private JsonNullable<BigDecimal> balance;
    private JsonNullable<Set<String>> phoneNumber;
    private JsonNullable<Set<String>> email;
}
