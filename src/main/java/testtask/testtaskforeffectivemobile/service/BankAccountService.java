package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import testtask.testtaskforeffectivemobile.dto.bankAccount.BankAccountCreateDTO;
import testtask.testtaskforeffectivemobile.dto.bankAccount.BankAccountDTO;
import testtask.testtaskforeffectivemobile.dto.bankAccount.BankAccountUpdateDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserCreateDTO;
import testtask.testtaskforeffectivemobile.mapper.BankAccountMapper;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.model.User;
import testtask.testtaskforeffectivemobile.repository.BankAccountRepository;

@Service
@AllArgsConstructor
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;
    private BankAccountMapper bankAccountMapper;

    public BankAccountDTO create(User user, Double balance) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(balance);
        bankAccount.setInitialDeposit(balance);
        bankAccountRepository.save(bankAccount);
        user.setBankAccount(bankAccount);
        return bankAccountMapper.toDTO(bankAccount);
    }
}
