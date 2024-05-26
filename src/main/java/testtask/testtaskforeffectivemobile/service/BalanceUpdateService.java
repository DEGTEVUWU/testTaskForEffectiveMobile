package testtask.testtaskforeffectivemobile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BalanceUpdateService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Transactional
    public void updateBalances() {
        List<BankAccount> accounts = bankAccountRepository.findAll();

        accounts.forEach((BankAccount account) -> {
            BigDecimal updatedBalance = account.getBalance().multiply(new BigDecimal("1.05"));
            BigDecimal maxBalance = account.getInitialDeposit().multiply(new BigDecimal("2.07"));
            BigDecimal newBalance = updatedBalance.compareTo(maxBalance) > 0 ? maxBalance : updatedBalance;

            account.setBalance(newBalance);
            bankAccountRepository.save(account);
        });
    }
}
