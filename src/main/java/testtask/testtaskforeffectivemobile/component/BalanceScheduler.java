package testtask.testtaskforeffectivemobile.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import testtask.testtaskforeffectivemobile.repository.BankAccountRepository;
import testtask.testtaskforeffectivemobile.service.BalanceUpdateService;

@Component
@EnableAsync
public class BalanceScheduler {

    @Autowired
    private BalanceUpdateService balanceUpdateService;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Scheduled(fixedRate = 60000)
    @Async     //предусмотрено(на всякий случай) ассинхронное выполнение задачи независимо от др. задач
    public void updateBalancesTask() {
        if (!bankAccountRepository.findAll().isEmpty()) {
            balanceUpdateService.updateBalances();
        }
    }
}
