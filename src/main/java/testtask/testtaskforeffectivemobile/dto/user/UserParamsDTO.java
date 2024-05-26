package testtask.testtaskforeffectivemobile.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@ToString
public class UserParamsDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDateGt;
    private String phoneNumberAll;
    private String firstNameCont;
    private String lastNameCont;
    private String surnameCont;
    private String emailAll;
}
