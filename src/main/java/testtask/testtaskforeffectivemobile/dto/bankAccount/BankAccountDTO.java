package testtask.testtaskforeffectivemobile.dto.bankAccount;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class BankAccountDTO {
    private Long id;
    private String balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
