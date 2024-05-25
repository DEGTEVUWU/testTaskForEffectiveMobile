package testtask.testtaskforeffectivemobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testtask.testtaskforeffectivemobile.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
