package nextstep.theme.service;

import nextstep.theme.domain.ThemeEntity;

import java.util.List;

public interface ThemeService {

    ThemeEntity create(ThemeEntity theme);

    ThemeEntity getById(Long id);

    List<ThemeEntity> getAll();

    ThemeEntity update(Long id, ThemeEntity theme);

    boolean deleteById(Long id);
}
