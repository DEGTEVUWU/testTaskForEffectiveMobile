package testtask.testtaskforeffectivemobile.mapper;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.internal.build.AllowSysOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import testtask.testtaskforeffectivemobile.dto.bankAccount.BankAccountDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserCreateDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserDTO;
import testtask.testtaskforeffectivemobile.dto.user.UserUpdateDTO;
import testtask.testtaskforeffectivemobile.exeption.ResourceNotFoundException;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.model.EmailAddress;
import testtask.testtaskforeffectivemobile.model.PhoneNumber;
import testtask.testtaskforeffectivemobile.model.User;
import testtask.testtaskforeffectivemobile.repository.EmailRepository;
import testtask.testtaskforeffectivemobile.repository.PhoneNumberRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
    uses = { ReferenceMapper.class, JsonNullableMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Mapping(source = "bankAccount.balance", target = "balance")
    @Mapping(source = "email", target = "email", qualifiedByName = "ModelToEmails")
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "ModelToPhoneNumbers")
    public abstract UserDTO toDTO(User model);

    @Mapping(source = "password", target = "passwordDigest")
    @Mapping(source = "email", target = "email", qualifiedByName = "EmailsToModel")
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "PhoneNumbersToModel")
    public abstract User toModel(UserCreateDTO dto);

    @Mapping(source = "email", target = "email", qualifiedByName = "EmailsToModel")
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "PhoneNumbersToModel")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    @Mapping(source = "email", target = "email", qualifiedByName = "ModelToEmails")
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "ModelToPhoneNumbers")
    public abstract UserUpdateDTO toUpdateDTOForValid(User user);
    /**
     * методы для маппинга из сета строк в сет сущностей
     * речь о тлф номерах  и емайлах
     */
    @Named("EmailsToModel")
    public Set<EmailAddress> EmailsToModel(Set<String> emailsStr) {
        if (emailsStr.isEmpty()) {
            throw new ResourceNotFoundException("EmailsStr set is null or empty!");
        }
        return new HashSet<>(emailRepository.findByEmailIn(emailsStr));
    }
    @Named("PhoneNumbersToModel")
    public Set<PhoneNumber> PhoneNumbersToModel(Set<String> phoneNumbersStr) {
        if (phoneNumbersStr.isEmpty()) {
            throw new ResourceNotFoundException("PhoneNumbersStr set is null or empty!");
        }
        return new HashSet<>(phoneNumberRepository.findByPhoneNumberIn(phoneNumbersStr));
    }

    /**
     * методы для маппинга из сета сущностей в сет строк
     * речь о тлф номерах  и емайлах
     */
    @Named("ModelToEmails")
    public Set<String> ModelToEmails(Set<EmailAddress> emails) {
        if (emails.isEmpty()) {
            throw new ResourceNotFoundException("Emails is null or empty!");
        }
        return emails.stream()
            .map(EmailAddress::getEmail)
            .collect(Collectors.toSet());
    }
    @Named("ModelToPhoneNumbers")
    public Set<String> ModelToPhoneNumbers(Set<PhoneNumber> phoneNumbers) {
        if(phoneNumbers.isEmpty()) {
            throw new ResourceNotFoundException("PhoneNumbers is null or empty!");
        }
        return phoneNumbers.stream()
            .map(PhoneNumber::getPhoneNumber)
            .collect(Collectors.toSet());
    }
}
