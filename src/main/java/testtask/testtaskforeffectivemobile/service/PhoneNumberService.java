package testtask.testtaskforeffectivemobile.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import testtask.testtaskforeffectivemobile.dto.phoneNumber.PhoneNumberDTO;
import testtask.testtaskforeffectivemobile.exeption.PhoneNumberAlreadyExistsException;
import testtask.testtaskforeffectivemobile.exeption.ResourceNotFoundException;
import testtask.testtaskforeffectivemobile.mapper.PhoneNumberMapper;
import testtask.testtaskforeffectivemobile.model.PhoneNumber;
import testtask.testtaskforeffectivemobile.repository.PhoneNumberRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
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
        if(phoneNumberRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new DataIntegrityViolationException("PhoneNumber is already in use: " + phoneNumber);
        }
        PhoneNumber phoneNumberModel = phoneNumberMapper.toModel(phoneNumber);
        phoneNumberRepository.save(phoneNumberModel);
        return phoneNumberMapper.toDTO(phoneNumberModel);
    }

    public List<PhoneNumberDTO> getAll() {
        var phoneNumbers = phoneNumberRepository.findAll();
        var result = phoneNumbers.stream()
            .map(phoneNumberMapper::toDTO)
            .toList();
        return result;
    }

    public PhoneNumberDTO findById(Long id) {
        var phoneNumber = phoneNumberRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("PhoneNumber with id " + id + " not found!"));
        var phoneNumberDTO = phoneNumberMapper.toDTO(phoneNumber);
        return phoneNumberDTO;
    }
}
