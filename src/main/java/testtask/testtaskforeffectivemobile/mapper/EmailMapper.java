package testtask.testtaskforeffectivemobile.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import testtask.testtaskforeffectivemobile.dto.bankAccount.BankAccountDTO;
import testtask.testtaskforeffectivemobile.dto.email.EmailDTO;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.model.Email;
import testtask.testtaskforeffectivemobile.repository.EmailRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
    uses = { ReferenceMapper.class, JsonNullableMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class EmailMapper {
    @Autowired
    private EmailRepository emailRepository;

    public abstract EmailDTO toDTO(Email model);
    public abstract Email toModel(String email);

    public Set<Email> mapStringsToEmailEntities(Set<String> stringSet) {
        return stringSet.stream()
            .map(emailRepository::findByEmail)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }
}
