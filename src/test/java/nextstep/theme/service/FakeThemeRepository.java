package nextstep.theme.service;

import nextstep.theme.domain.Theme;
import nextstep.theme.repository.ThemeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.util.*;

public class FakeThemeRepository implements ThemeRepository {

    private final Map<Long, Theme> themes = new HashMap<>();

    private Long idIndex = 0L;

    @Override
    public Theme insert(Theme theme) throws DuplicateKeyException {
        validateDuplicate(theme);
        theme.setId(++idIndex);
        themes.put(idIndex, theme);

        return theme;
    }

    private void validateDuplicate(Theme theme) {
        boolean duplicated = themes.values()
                .stream()
                .anyMatch(t -> theme.getName()
                        .equals(t.getName()));
        if (duplicated) {
            throw new DuplicateKeyException("");
        }
    }

    @Override
    public Theme getById(Long id) {
        return themes.get(id);
    }

    @Override
    public List<Theme> getAll() {
        ArrayList<Theme> themes = new ArrayList<>(this.themes.values());
        themes.sort(Comparator.comparing(Theme::getId));

        return themes;
    }

    @Override
    public boolean update(Long id, Theme theme) throws DuplicateKeyException {
        validateDuplicate(theme);
        var dbTheme = themes.get(id);
        if (dbTheme == null) {
            return false;
        }
        dbTheme = new Theme(dbTheme.getId(), theme.getName(), theme.getDesc(), theme.getPrice());
        themes.put(id, dbTheme);

        return true;
    }

    @Override
    public boolean delete(Long id) {
        if (id == -1L) {
            throw new DataIntegrityViolationException("");
        }
        return themes.keySet()
                .removeIf(id::equals);
    }
}