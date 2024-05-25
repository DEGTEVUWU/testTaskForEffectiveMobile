package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import testtask.testtaskforeffectivemobile.dto.user.UserCreateDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserUpdateDTO;
import testtask.testtaskforeffectivemobile.exeption.ResourceNotFoundException;
import testtask.testtaskforeffectivemobile.mapper.UserMapper;
import testtask.testtaskforeffectivemobile.model.User;
import testtask.testtaskforeffectivemobile.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsManager {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PhoneNumberService phoneNumberService;
    private final EmailService emailService;
    private final BankAccountService bankAccountService;

    public List<UserDTO> getAll() {
        var users = userRepository.findAll();
        var result = users.stream()
            .map(userMapper::toDTO)
            .toList();
        return result;
    }


    public UserDTO create(UserCreateDTO userData) {
        createDependencies(userData);
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
        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        userMapper.update(userData, user);
        userRepository.save(user);
        var userDTO = userMapper.toDTO(user);
        return userDTO;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void createDependencies(UserCreateDTO userCreateDTO) {
        phoneNumberService.createOrGetExisting(userCreateDTO.getPhoneNumber());
        emailService.createOrGetExisting(userCreateDTO.getEmail());
//        bankAccountService.create(userCreateDTO.getBalance());
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
