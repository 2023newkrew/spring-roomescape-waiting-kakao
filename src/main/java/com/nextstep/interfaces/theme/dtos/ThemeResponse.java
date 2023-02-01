package com.nextstep.interfaces.theme.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class ThemeResponse {

    private final Long id;

    private final String name;

    private final String desc;

    private final Integer price;
}
