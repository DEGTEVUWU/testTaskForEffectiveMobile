package testtask.testtaskforeffectivemobile.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import testtask.testtaskforeffectivemobile.exeption.ValidationException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class Validation {
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^((\\+7|7|8)+([0-9]){10})$"
    );

    @Bean
    public boolean isValidEmail(Collection<String> emails) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        for (String email : emails) {
            if (!emailValidator.isValid(email)) {
                throw new ValidationException("Невалидный email: " + email);
            }
        }
        return true;
    }

    @Bean
    public boolean isValidPhoneNumber(Set<String> phoneNumbers) {
        if (!phoneNumbers.stream().allMatch(phoneNumber -> PHONE_PATTERN.matcher(phoneNumber).matches())) {
            throw new ValidationException("Невалидный телефонный номер: " + phoneNumbers);
        }
        return true;
    }
    public boolean isValidLogin(String login) {
        if (login == null || login.isEmpty()) {
            throw new ValidationException("Логин не может быть пустым!");
        }
        if (login.length() < 2) {
            throw new ValidationException("Логин должен быть длиннее 2 символов");
        }
        if (login.length() > 20) {
            throw new ValidationException("Логин должен быть короче 20 символов");
        }
        return true;
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Пароль не может быть пустым!");
        }
        if (password.length() < 6) {
            throw new ValidationException("Пароль должен быть длиннее 6 символов");
        }
        if (password.length() > 20) {
            throw new ValidationException("Пароль должен быть короче 20 символов");
        }
        return true;
    }

    public boolean isValidBalance(BigDecimal balance) {
        if (balance == null) {
            throw new ValidationException("Баланс не может быть пустым!");
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Баланс не может быть отрицательным!");
        }
        return true;
    }
}
