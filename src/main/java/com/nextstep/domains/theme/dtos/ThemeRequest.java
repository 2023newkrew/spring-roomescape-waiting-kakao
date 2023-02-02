package com.nextstep.domains.theme.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextstep.domains.theme.entities.ThemeEntity;

public class ThemeRequest {
    private String name;
    private String desc;
    private int price;

    @JsonCreator
    public ThemeRequest(@JsonProperty(value = "name") String name,
                        @JsonProperty(value = "desc") String desc,
                        @JsonProperty(value = "price") int price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }

    public ThemeEntity toEntity() {
        return new ThemeEntity(
                this.name,
                this.desc,
                this.price
        );
    }
}
