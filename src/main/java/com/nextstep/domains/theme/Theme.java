package com.nextstep.domains.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Theme {

    @Getter
    @Setter
    private Long id;

    @Getter
    private final String name;

    @Getter
    private final String desc;

    @Getter
    private final Integer price;
}
