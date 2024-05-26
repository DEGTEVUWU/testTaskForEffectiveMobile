package testtask.testtaskforeffectivemobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testtask.testtaskforeffectivemobile.model.BankAccount;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
