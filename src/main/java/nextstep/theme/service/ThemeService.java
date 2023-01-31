package nextstep.theme.service;

import nextstep.theme.domain.Theme;
import nextstep.theme.domain.ThemeEntity;

import java.util.List;

public interface ThemeService {

    ThemeEntity create(Theme theme);

    ThemeEntity getById(Long id);

    List<ThemeEntity> getAll();

    ThemeEntity update(Long id, Theme theme);

    boolean deleteById(Long id);
}
