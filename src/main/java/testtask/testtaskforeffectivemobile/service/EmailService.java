package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import testtask.testtaskforeffectivemobile.dto.email.EmailDTO;
import testtask.testtaskforeffectivemobile.exeption.EmailAlreadyExistsException;
import testtask.testtaskforeffectivemobile.exeption.ResourceNotFoundException;
import testtask.testtaskforeffectivemobile.mapper.EmailMapper;
import testtask.testtaskforeffectivemobile.model.Email;
import testtask.testtaskforeffectivemobile.model.User;
import testtask.testtaskforeffectivemobile.repository.EmailRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
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

    public List<EmailDTO> getAll() {
        var emails = emailRepository.findAll();
        var result = emails.stream()
            .map(emailMapper::toDTO)
            .toList();
        return result;
    }

    public EmailDTO findById(Long id) {
        var email = emailRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Email with id " + id + " not found!"));
        var emailDTO = emailMapper.toDTO(email);
        return emailDTO;
    }
}
