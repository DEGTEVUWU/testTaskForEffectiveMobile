package testtask.testtaskforeffectivemobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testtask.testtaskforeffectivemobile.model.Email;
import testtask.testtaskforeffectivemobile.model.PhoneNumber;

import java.util.Optional;
import java.util.Set;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    Set<PhoneNumber> findByPhoneNumberIn(Set<String> phoneNumbers);
    Optional<PhoneNumber> findByPhoneNumber(String phoneNumber);
    Optional<PhoneNumber> findPhoneNumberById(Long phoneNumber);
}
