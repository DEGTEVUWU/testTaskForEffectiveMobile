package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import testtask.testtaskforeffectivemobile.dto.email.EmailDTO;
import testtask.testtaskforeffectivemobile.dto.phoneNumber.PhoneNumberDTO;
import testtask.testtaskforeffectivemobile.exeption.EmailAlreadyExistsException;
import testtask.testtaskforeffectivemobile.exeption.PhoneNumberAlreadyExistsException;
import testtask.testtaskforeffectivemobile.mapper.PhoneNumberMapper;
import testtask.testtaskforeffectivemobile.model.Email;
import testtask.testtaskforeffectivemobile.model.PhoneNumber;
import testtask.testtaskforeffectivemobile.repository.PhoneNumberRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhoneNumberService {
    private PhoneNumberRepository phoneNumberRepository;
    private PhoneNumberMapper phoneNumberMapper;

    public Set<PhoneNumberDTO> createOrGetExisting(Set<String> phoneNumbers) {
        return phoneNumbers.stream()
            .map(p -> {
                Optional<PhoneNumber> existingPhoneNumber = phoneNumberRepository.findByPhoneNumber(p);
                if (existingPhoneNumber.isPresent()) {
                    throw new PhoneNumberAlreadyExistsException("PhoneNumber already exists: " + p);
                }
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setPhoneNumber(p);
                phoneNumberRepository.save(phoneNumber);
                return phoneNumberMapper.toDTO(phoneNumber);
            })
            .collect(Collectors.toSet());
    }

    public PhoneNumberDTO addPhoneNumber(String phoneNumber) {
        if(phoneNumberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("PhoneNumber is already in use: " + phoneNumber);
        }
        PhoneNumber phoneNumberModel = phoneNumberMapper.toModel(phoneNumber);
        phoneNumberRepository.save(phoneNumberModel);
        return phoneNumberMapper.toDTO(phoneNumberModel);
    }
}
