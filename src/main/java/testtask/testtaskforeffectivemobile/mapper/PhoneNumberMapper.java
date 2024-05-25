package testtask.testtaskforeffectivemobile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import testtask.testtaskforeffectivemobile.dto.bankAccount.BankAccountDTO;
import testtask.testtaskforeffectivemobile.dto.phoneNumber.PhoneNumberDTO;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.model.Email;
import testtask.testtaskforeffectivemobile.model.PhoneNumber;
import testtask.testtaskforeffectivemobile.repository.PhoneNumberRepository;
import testtask.testtaskforeffectivemobile.service.PhoneNumberService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
    uses = { ReferenceMapper.class, JsonNullableMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PhoneNumberMapper {
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    public abstract PhoneNumberDTO toDTO(PhoneNumber model);
    public abstract PhoneNumber toModel(String phoneNumber);


    public Set<PhoneNumber> mapStringsToPhoneNumberEntities(Set<String> stringSet) {
        return stringSet.stream()
            .map(phoneNumberRepository::findByPhoneNumber)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }

}
