package testtask.testtaskforeffectivemobile.mapper;

import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import testtask.testtaskforeffectivemobile.model.BaseEntity;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {
    @Autowired
    private EntityManager entityManager; //интерфейс для взаимодействия в БД в JTA

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }
}
//данный класс нужен, чтоб вернуть класс любой модели, наследюущей базовую_сущность, используется
//в мапперах для каждого отдельного класса, чтоб преобразовывать по айди одни сущности в поля другого класса
// подобной контсрукцией     @Mapping(target = "category", source = "categoryId")