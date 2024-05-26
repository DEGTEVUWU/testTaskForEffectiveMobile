package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testtask.testtaskforeffectivemobile.dto.TransferRequestDTO;
import testtask.testtaskforeffectivemobile.dto.bankAccount.BankAccountCreateDTO;
import testtask.testtaskforeffectivemobile.dto.bankAccount.BankAccountDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserCreateDTO;
import testtask.testtaskforeffectivemobile.exeption.InsufficientFundsException;
import testtask.testtaskforeffectivemobile.exeption.ResourceNotFoundException;
import testtask.testtaskforeffectivemobile.mapper.BankAccountMapper;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.model.User;
import testtask.testtaskforeffectivemobile.repository.BankAccountRepository;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private BankAccountMapper bankAccountMapper;

    public BankAccountDTO create(User user, BigDecimal balance) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(balance);
        bankAccount.setInitialDeposit(balance);
        bankAccountRepository.save(bankAccount);
        user.setBankAccount(bankAccount);
        return bankAccountMapper.toDTO(bankAccount);
    }
    @Transactional
    public void transfer(TransferRequestDTO transferRequestDTO) {
        BigDecimal amount = transferRequestDTO.getAmount();

        BankAccount fromAccount = bankAccountRepository.findById(transferRequestDTO.getFromAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("BankAccount with id " +
                transferRequestDTO.getFromAccountId() + " not found!"));
        BankAccount toAccount = bankAccountRepository.findById(transferRequestDTO.getToAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("BankAccount with id " +
                transferRequestDTO.getToAccountId() + " not found!"));
        validateAccountsAndAmount(fromAccount, toAccount, amount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
    }

    private void validateAccountsAndAmount(BankAccount fromAccount, BankAccount toAccount, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromAccount.getId());
        }
    }
}
