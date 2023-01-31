package nextstep.theme;

import java.util.List;

public interface ThemeDao {
    Long save(Theme theme);

    Theme findById(Long id);

    List<Theme> findAll();

    void delete(Long id);
}
