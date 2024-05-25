package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import testtask.testtaskforeffectivemobile.dto.email.EmailDTO;
import testtask.testtaskforeffectivemobile.exeption.EmailAlreadyExistsException;
import testtask.testtaskforeffectivemobile.exeption.ResourceNotFoundException;
import testtask.testtaskforeffectivemobile.mapper.EmailMapper;
import testtask.testtaskforeffectivemobile.model.Email;
import testtask.testtaskforeffectivemobile.model.User;
import testtask.testtaskforeffectivemobile.repository.EmailRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmailService {
    private EmailRepository emailRepository;
    private EmailMapper emailMapper;

    public Set<EmailDTO> createOrGetExisting(Set<String> emails) {
        return emails.stream()
            .map(e -> {
                Optional<Email> existingEmail = emailRepository.findByEmail(e);
                if (existingEmail.isPresent()) {
                    throw new EmailAlreadyExistsException("Email already exists: " + e);
                }
                Email email = new Email();
                email.setEmail(e);
                emailRepository.save(email);
                return emailMapper.toDTO(email);
            })
            .collect(Collectors.toSet());
    }
    public EmailDTO addEmail(String email) {
        if(emailRepository.findByEmail(email).isPresent()) {
            throw new DataIntegrityViolationException("Email is already in use: " + email);
        }
        Email emailModel = emailMapper.toModel(email);
        emailRepository.save(emailModel);
        return emailMapper.toDTO(emailModel);
    }
}
