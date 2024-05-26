package testtask.testtaskforeffectivemobile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.repository.BankAccountRepository;

import java.util.List;

@Service
public class BalanceUpdateService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Transactional
    public void updateBalances() {
        List<BankAccount> accounts = bankAccountRepository.findAll();

        accounts.forEach((BankAccount account) -> {
                double updatedBalance = account.getBalance() * 1.05;
                double maxBalance = account.getInitialDeposit() * 2.07;
                account.setBalance(Math.min(updatedBalance, maxBalance));
                bankAccountRepository.save(account);
        });
    }
}
