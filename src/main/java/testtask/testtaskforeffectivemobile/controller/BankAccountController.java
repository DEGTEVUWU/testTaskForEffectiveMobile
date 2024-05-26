package testtask.testtaskforeffectivemobile.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.testtaskforeffectivemobile.dto.TransferRequestDTO;
import testtask.testtaskforeffectivemobile.service.BankAccountService;
import testtask.testtaskforeffectivemobile.service.UserService;
import testtask.testtaskforeffectivemobile.utils.UserUtils;

@RestController
@RequestMapping("/api/bankAccount")
@AllArgsConstructor
@Slf4j
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final UserUtils userUtils;
    private static final String CURRENT_USER = "@userUtils.getCurrentUser().getId() == #id";

}
