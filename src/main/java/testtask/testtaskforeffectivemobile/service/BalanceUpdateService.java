package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ssl.NoSuchSslBundleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testtask.testtaskforeffectivemobile.exeption.ResourceNotFoundException;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BalanceUpdateService {
    private BankAccountRepository bankAccountRepository;

    @Transactional
    public void updateBalances() {
        try {
            List<BankAccount> accounts = bankAccountRepository.findAll();
            accounts.forEach((BankAccount account) -> {
                BigDecimal updatedBalance = account.getBalance().multiply(new BigDecimal("1.05"));
                BigDecimal maxBalance = account.getInitialDeposit().multiply(new BigDecimal("2.07"));
                BigDecimal newBalance = updatedBalance.compareTo(maxBalance) > 0 ? maxBalance : updatedBalance;

                account.setBalance(newBalance);
                bankAccountRepository.save(account);
            });
        } catch (ResourceNotFoundException e) {
        System.out.println(e.getMessage());
        }
    }
}
