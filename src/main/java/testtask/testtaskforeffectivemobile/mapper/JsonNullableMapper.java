package testtask.testtaskforeffectivemobile.mapper;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;


@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class JsonNullableMapper {

    public <T> JsonNullable<T> wrap(T entity) {
        return JsonNullable.of(entity);
    }

    public <T> T unwrap(JsonNullable<T> jsonNullable) {
        return jsonNullable == null ? null : jsonNullable.orElse(null);
    }

    @Condition
    public <T> boolean isPresent(JsonNullable<T> nullable) {
        return nullable != null && nullable.isPresent();
    }
}
//класс нужен, чтоб при обрабтки ДТО-модель можно было принимать null, как значнеие,
// и добавлять его в таблы/сущности/дто