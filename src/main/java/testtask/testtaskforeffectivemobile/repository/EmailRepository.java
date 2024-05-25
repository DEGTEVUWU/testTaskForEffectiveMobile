package testtask.testtaskforeffectivemobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testtask.testtaskforeffectivemobile.model.Email;

import java.util.Optional;
import java.util.Set;

public interface EmailRepository extends JpaRepository<Email, Long> {
    Set<Email> findByEmailIn(Set<String> emails);
    Optional<Email> findByEmail(String email);
//    Optional<Email> findById(Long emailId);
}
