package nextstep.theme.service;

import nextstep.theme.domain.Theme;

import java.util.List;

public interface ThemeService {

    Theme create(Theme theme);

    Theme getById(Long id);

    List<Theme> getAll();

    Theme update(Long id, Theme theme);

    boolean deleteById(Long id);
}
