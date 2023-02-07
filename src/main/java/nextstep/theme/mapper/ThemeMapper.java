package nextstep.theme.mapper;

import nextstep.theme.domain.Theme;
import nextstep.theme.dto.ThemeRequest;
import nextstep.theme.dto.ThemeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface ThemeMapper {

    @Mapping(target = "id", ignore = true)
    Theme fromRequest(ThemeRequest request);

    ThemeResponse toResponse(Theme theme);

    List<ThemeResponse> toResponses(List<Theme> themes);
}
