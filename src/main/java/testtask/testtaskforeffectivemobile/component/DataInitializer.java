package testtask.testtaskforeffectivemobile.component;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import net.datafaker.Faker;
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
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final EmailRepository emailRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createUsers(25);
    }

    public void createUsers(int count) {
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setBankAccount(createBankAccount());
            user.setEmail(Set.of(createEmail(), createEmail()));
            user.setPhoneNumber(Set.of(createPhoneNumber(), createPhoneNumber()));
            user.setLogin(faker.internet().slug());
            user.setPasswordDigest(faker.internet().password());
            user.setFirstName(faker.name().firstName());
            user.setSurname(faker.name().lastName());

            userRepository.save(user);
        }
    }

    public EmailAddress createEmail() {
        EmailAddress email = new EmailAddress();
        email.setEmail(faker.internet().emailAddress());
        return email;
    }
    public PhoneNumber createPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(faker.phoneNumber().phoneNumber());
        return phoneNumber;
    }
    public BankAccount createBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(new BigDecimal(faker.number().positive()));
        bankAccount.setInitialDeposit(bankAccount.getBalance());
        bankAccountRepository.save(bankAccount);
        return bankAccount;
    }
}

