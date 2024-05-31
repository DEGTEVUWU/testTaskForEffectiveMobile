package testtask.testtaskforeffectivemobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testtask.testtaskforeffectivemobile.model.EmailAddress;

import java.util.Optional;
import java.util.Set;

public interface EmailRepository extends JpaRepository<EmailAddress, Long> {
    Set<EmailAddress> findByEmailIn(Set<String> emails);
    Optional<EmailAddress> findByEmail(String email);
//    Optional<Email> findById(Long emailId);
}
