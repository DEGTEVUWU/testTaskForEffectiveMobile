package testtask.testtaskforeffectivemobile.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequestDTO {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}
