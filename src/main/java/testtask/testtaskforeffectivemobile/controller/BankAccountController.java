package testtask.testtaskforeffectivemobile.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import testtask.testtaskforeffectivemobile.dto.bankAccount.BankAccountDTO;
import testtask.testtaskforeffectivemobile.service.BankAccountService;

import java.util.List;

@RestController
@RequestMapping("/api/bank-account")
@AllArgsConstructor
@Slf4j
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @GetMapping(path = "")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<BankAccountDTO>> index() {
        List<BankAccountDTO> bankAccounts = bankAccountService.getAll();
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(bankAccounts.size()))
            .body(bankAccounts);
    }
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BankAccountDTO> show(@PathVariable Long id) {
        BankAccountDTO bankAccount = bankAccountService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bankAccount);
    }
}
