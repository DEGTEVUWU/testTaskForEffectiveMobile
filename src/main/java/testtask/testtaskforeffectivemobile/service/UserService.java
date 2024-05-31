package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<UserDTO> getAll() {
        var users = userRepository.findAll();
        logger.info("юзеры успешно получены из репозитория");
        var result = users.stream()
            .map(userMapper::toDTO)
            .toList();
        logger.info("юзеры успешно замеплены в дто и в лист");
        return result;
    }

    public Page<UserDTO> getAllByParameters(UserParamsDTO userParamsDTO, int pageNumber) {
        Specification<User> spec = userSpecification.build(userParamsDTO);
        logger.info("создали спецификацию по фильтрации юзеров");
        Page<User> users = userRepository.findAll(spec, PageRequest.of(pageNumber - 1, 10));
        logger.info("получили страницу юзеров из репо по спецификации");
        Page<UserDTO> result = users.map(userMapper::toDTO);
        logger.info("Замапили  страницу с юзерами на дто");
        return result;
    }

    public UserDTO create(UserCreateDTO userData) {
        validate(userData);
        logger.info("Данные из дто по созданию валидны");
        checkingLoginAvailability(userData);
        logger.info("логин юзера не занят");
        createDependencies(userData);
        logger.info("Сущности имейла и тлф номера созданы и добавлены в базу");


        User user = userMapper.toModel(userData);
        logger.info("Замапили дто в модель юзера");
        user.setPasswordDigest(passwordEncoder.encode(user.getPassword()));
        logger.info("зашифровал пароль");
        bankAccountService.create(user, userData.getBalance());
        logger.info("создал банковский аккаунт для юзера");
        userRepository.save(user);
        logger.debug("Добавил юзера в БД");
        var userDTO = userMapper.toDTO(user);
        logger.info("замапали модель в дто");
        return userDTO;
    }

    public UserDTO findById(Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        logger.info("юзер найде в БД");
        var userDTO = userMapper.toDTO(user);
        logger.info("замапали юзера в дто");
        return userDTO;
    }

    public UserDTO update(UserUpdateDTO userData, Long id) {
        validate(userData);
        logger.info("данные на обновление валдины");

        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        logger.info("юзер найден в БД по id");
        userMapper.update(userData, user);
        logger.info("данные юзера обновлены");
        userRepository.save(user);
        logger.info("обновленнный юзер сохранён в репозиторий");
        var userDTO = userMapper.toDTO(user);
        logger.info("модель юзера замапили в дто");
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
        logger.info("данные для частичного обновления валидны");

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        logger.info("юзер найден в БД");

        if(userUpdateDTO.getPhoneNumber() != null) {
            Set<String> phoneNumberSetString = userUpdateDTO.getPhoneNumber().get();
            phoneNumberSetString
                .forEach(phoneNumberService::addPhoneNumber);

            Set<PhoneNumber> newPhoneNumberSet =
                phoneNumberMapper.mapStringsToPhoneNumberEntities(phoneNumberSetString);
            Set<PhoneNumber> oldPhoneNumberSet = user.getPhoneNumber();
            oldPhoneNumberSet.addAll(newPhoneNumberSet);
            user.setPhoneNumber(oldPhoneNumberSet);
            logger.info("в юзера добавили изменённый сет с телефонами");
        }

        if(userUpdateDTO.getEmail() != null) {
            Set<String> emailSetString = userUpdateDTO.getEmail().get();
            emailSetString
                .forEach(emailService::addEmail);

            Set<EmailAddress> newEmailSet = emailMapper.mapStringsToEmailEntities(emailSetString);
            Set<EmailAddress> oldEmailSet = user.getEmail();
            oldEmailSet.addAll(newEmailSet);
            user.setEmail(oldEmailSet);
            logger.info("в юзера добавили изменённый сет с имейлами");
        }
        userRepository.save(user);
        logger.info("сохранили в репо обновлённого юзера");
        return userMapper.toDTO(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        logger.info("удалил юзера по id");
    }


    public void deleteEmail(Long userId, String emailValue) {
        EmailAddress email = emailRepository.findByEmail(emailValue)
            .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
        logger.info("имейл найден в базе по значению");
        deleteEmail(userId, email.getId());
        logger.info("перезгрузка в основной метод удаления имейла");
    }
    public void deleteEmail(Long userId, Long emailId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        logger.info("имейл найден по id");
        if (user.getEmail().size() > 1) {
            user.getEmail().removeIf(email -> email.getId().equals(emailId));
        } else {
            throw new LastEmailContactException(emailId);
        }
        logger.info("нужный имейл удалён из сета");
        emailRepository.deleteById(emailId);
        logger.info("имейл удалён из репозитория по id");
        userRepository.save(user);
        logger.info("юзер с удалённым имейлом сохранен в репозиторий");
    }


    public void deletePhoneNumber(Long userId, String phoneNumberValue) {
        PhoneNumber phoneNumber = phoneNumberRepository.findByPhoneNumber(phoneNumberValue)
            .orElseThrow(() -> new ResourceNotFoundException("PhoneNumber not found"));
        logger.info("тлф номер найден в БД по значению");
        deletePhoneNumber(userId, phoneNumber.getId());
        logger.info("перезгрузка в основной метод удаления тлф номера");
    }

    public void deletePhoneNumber(Long userId, Long phoneNumberId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        logger.info("тлф номер найден по id");
        if (user.getPhoneNumber().size() > 1) {
            user.getPhoneNumber().removeIf(phoneNumber -> phoneNumber.getId().equals(phoneNumberId));
        } else {
            throw new LastEmailContactException(phoneNumberId);
        }
        logger.info("нужный тлф номер удалён из сета");
        phoneNumberRepository.deleteById(phoneNumberId);
        logger.info("тлф номер удалён из репозитория по id");
        userRepository.save(user);
        logger.info("юзер с обновлённым тлф номером сохранен в репозиторий");
    }

    private void createDependencies(UserCreateDTO userCreateDTO) {
        phoneNumberService.createOrGetExisting(userCreateDTO.getPhoneNumber());
        logger.info("создали или получили модель тлф номера");
        emailService.createOrGetExisting(userCreateDTO.getEmail());
        logger.info("создали или получили модель имейла");
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
