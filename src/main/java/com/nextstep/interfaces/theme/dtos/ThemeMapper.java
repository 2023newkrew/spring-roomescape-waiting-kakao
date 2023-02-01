package com.nextstep.interfaces.theme.dtos;

import com.nextstep.domains.theme.Theme;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface ThemeMapper {

    @Mapping(target = "id", ignore = true)
    Theme fromRequest(ThemeRequest request);

    ThemeResponse toResponse(Theme theme);
}
