package nextstep.theme.mapper;

import nextstep.theme.domain.ThemeEntity;
import nextstep.theme.dto.ThemeRequest;
import nextstep.theme.dto.ThemeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ThemeMapper {

    @Mapping(target = "id", ignore = true)
    ThemeEntity fromRequest(ThemeRequest request);

    ThemeResponse toResponse(ThemeEntity theme);
}
