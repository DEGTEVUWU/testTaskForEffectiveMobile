package testtask.testtaskforeffectivemobile.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.model.EmailAddress;
import testtask.testtaskforeffectivemobile.model.PhoneNumber;
import testtask.testtaskforeffectivemobile.model.User;
import testtask.testtaskforeffectivemobile.repository.BankAccountRepository;
import testtask.testtaskforeffectivemobile.repository.EmailRepository;
import testtask.testtaskforeffectivemobile.repository.PhoneNumberRepository;
import testtask.testtaskforeffectivemobile.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Set;

@Component
@Getter
@AllArgsConstructor
public class CreateTestUser {

    private Faker faker;
    private EmailRepository emailRepository;
    private PhoneNumberRepository phoneNumberRepository;
    private BankAccountRepository bankAccountRepository;
    private UserRepository userRepository;

    public User createTestUser() {
        User testUser = new User();
        EmailAddress testEmail = new EmailAddress();
        PhoneNumber testPhoneNumber = new PhoneNumber();
        BankAccount testBankAccount = new BankAccount();

        testEmail.setEmail(faker.internet().emailAddress());
        testPhoneNumber.setPhoneNumber(faker.phoneNumber().phoneNumber());
        testBankAccount.setBalance(BigDecimal.valueOf(faker.number().positive()));


        testUser.setBankAccount(testBankAccount);
        testUser.setEmail(Set.of(testEmail));
        testUser.setPhoneNumber(Set.of(testPhoneNumber));
        testUser.setLogin(faker.internet().slug());
        testUser.setPasswordDigest(faker.internet().password());

        bankAccountRepository.save(testBankAccount);
        userRepository.save(testUser);
        return testUser;
    }
}
