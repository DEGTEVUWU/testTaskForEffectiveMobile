package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import testtask.testtaskforeffectivemobile.dto.user.UserCreateDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserParamsDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserUpdateDTO;
import testtask.testtaskforeffectivemobile.exeption.LastEmailContactException;
import testtask.testtaskforeffectivemobile.exeption.LoginAlreadyExistsException;
import testtask.testtaskforeffectivemobile.exeption.ResourceNotFoundException;
import testtask.testtaskforeffectivemobile.exeption.ValidationException;
import testtask.testtaskforeffectivemobile.mapper.EmailMapper;
import testtask.testtaskforeffectivemobile.mapper.PhoneNumberMapper;
import testtask.testtaskforeffectivemobile.mapper.UserMapper;
import testtask.testtaskforeffectivemobile.model.EmailAddress;
import testtask.testtaskforeffectivemobile.model.PhoneNumber;
import testtask.testtaskforeffectivemobile.model.User;
import testtask.testtaskforeffectivemobile.repository.EmailRepository;
import testtask.testtaskforeffectivemobile.repository.PhoneNumberRepository;
import testtask.testtaskforeffectivemobile.repository.UserRepository;
import testtask.testtaskforeffectivemobile.specification.UserSpecification;
import testtask.testtaskforeffectivemobile.validation.Validation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsManager {
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final PhoneNumberMapper phoneNumberMapper;
    private final PasswordEncoder passwordEncoder;
    private final PhoneNumberService phoneNumberService;
    private final EmailService emailService;
    private final BankAccountService bankAccountService;
    private final UserSpecification userSpecification;
    private final Validation validation;

    public List<UserDTO> getAll() {
        var users = userRepository.findAll();
        var result = users.stream()
            .map(userMapper::toDTO)
            .toList();
        return result;
    }

    public Page<UserDTO> getAllByParameters(UserParamsDTO userParamsDTO, int pageNumber) {
        Specification<User> spec = userSpecification.build(userParamsDTO);
        Page<User> users = userRepository.findAll(spec, PageRequest.of(pageNumber - 1, 10));
        Page<UserDTO> result = users.map(userMapper::toDTO);
        log.info("Вывели первую страницу с юзерами дто на экран");
        return result;
    }

    public UserDTO create(UserCreateDTO userData) {
        validate(userData);

        createDependencies(userData);
        checkingLoginAvailability(userData);

        User user = userMapper.toModel(userData);
        user.setPasswordDigest(passwordEncoder.encode(user.getPassword()));
        bankAccountService.create(user, userData.getBalance());
        userRepository.save(user);
        var userDTO = userMapper.toDTO(user);
        return userDTO;
    }

    public UserDTO findById(Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        var userDTO = userMapper.toDTO(user);
        return userDTO;
    }

    public UserDTO update(UserUpdateDTO userData, Long id) {
        validate(userData);

        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        userMapper.update(userData, user);
        userRepository.save(user);
        var userDTO = userMapper.toDTO(user);
        return userDTO;
    }

    /**
     *
     * этот метод реализует частичное обновление (добавление новых) имейлов и телефонов в сущность юзера
     * находим юзера, создаем новые сущности мыла и тлф и маппим сет строк в сет сущностей мейлов и тлф и сливаем этот сет
     * с имеющимся сетом(добавляем)
     */
    public UserDTO updateUserContactInfo(Long userId, UserUpdateDTO userUpdateDTO) {
        validate(userUpdateDTO);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if(userUpdateDTO.getPhoneNumber() != null) {
            Set<String> phoneNumberSetString = userUpdateDTO.getPhoneNumber().get();
            phoneNumberSetString
                .forEach(phoneNumberService::addPhoneNumber);

            Set<PhoneNumber> newPhoneNumberSet =
                phoneNumberMapper.mapStringsToPhoneNumberEntities(phoneNumberSetString);
            Set<PhoneNumber> oldPhoneNumberSet = user.getPhoneNumber();
            oldPhoneNumberSet.addAll(newPhoneNumberSet);
            user.setPhoneNumber(oldPhoneNumberSet);
        }

        if(userUpdateDTO.getEmail() != null) {
            Set<String> emailSetString = userUpdateDTO.getEmail().get();
            emailSetString
                .forEach(emailService::addEmail);

            Set<EmailAddress> newEmailSet = emailMapper.mapStringsToEmailEntities(emailSetString);
            Set<EmailAddress> oldEmailSet = user.getEmail();
            oldEmailSet.addAll(newEmailSet);
            user.setEmail(oldEmailSet);
        }
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }


    public void deleteEmail(Long userId, String emailValue) {
        EmailAddress email = emailRepository.findByEmail(emailValue)
            .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
        deleteEmail(userId, email.getId());
    }
    public void deleteEmail(Long userId, Long emailId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getEmail().size() > 1) {
            user.getEmail().removeIf(email -> email.getId().equals(emailId));
        } else {
            throw new LastEmailContactException(emailId);
        }
        emailRepository.deleteById(emailId);
        userRepository.save(user);
    }


    public void deletePhoneNumber(Long userId, String phoneNumberValue) {
        PhoneNumber phoneNumber = phoneNumberRepository.findByPhoneNumber(phoneNumberValue)
            .orElseThrow(() -> new ResourceNotFoundException("PhoneNumber not found"));
        deletePhoneNumber(userId, phoneNumber.getId());
    }

    public void deletePhoneNumber(Long userId, Long phoneNumberId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getPhoneNumber().size() > 1) {
            user.getPhoneNumber().removeIf(phoneNumber -> phoneNumber.getId().equals(phoneNumberId));
        } else {
            throw new LastEmailContactException(phoneNumberId);
        }
        phoneNumberRepository.deleteById(phoneNumberId);
        userRepository.save(user);
    }

    private void createDependencies(UserCreateDTO userCreateDTO) {
        phoneNumberService.createOrGetExisting(userCreateDTO.getPhoneNumber());
        emailService.createOrGetExisting(userCreateDTO.getEmail());
    }

    private void checkingLoginAvailability(UserCreateDTO userData) {
        Optional<User> anotherUser = userRepository.findByLogin(userData.getLogin());
        if (anotherUser.isPresent()) {
            throw new LoginAlreadyExistsException("User with login " + userData.getLogin()
                + " already exists! Change login!");
        }
    }
    private void validate(UserCreateDTO userData) {
        validation.isValidLogin(userData.getLogin());
        validation.isValidPassword(userData.getPassword());
        validation.isValidBalance(userData.getBalance());
        validation.isValidEmail(userData.getEmail());
        validation.isValidPhoneNumber(userData.getPhoneNumber());
    }
    private void validate(UserUpdateDTO userData) {
        validation.isValidEmail(userData.getEmail().get());
        validation.isValidPhoneNumber(userData.getPhoneNumber().get());
    }

    @Override
    public void createUser(UserDetails userData) {
        var user = new User();
        user.setLogin(userData.getUsername());
        var hashedPassword = passwordEncoder.encode(userData.getPassword());
        user.setPasswordDigest(hashedPassword);
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
}
