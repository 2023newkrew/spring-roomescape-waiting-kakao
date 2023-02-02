package app.nextstep.repository;

import app.nextstep.dao.ThemeDao;
import app.nextstep.domain.Theme;
import app.nextstep.entity.ThemeEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ThemeRepository {
    private ThemeDao themeDao;

    public ThemeRepository(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Theme findById(Long id) {
        return themeDao.findById(id).toTheme();
    }

    public List<Theme> findAll() {
        List<Theme> themes = new ArrayList<>();
        for (ThemeEntity themeEntity : themeDao.findAll()) {
            themes.add(themeEntity.toTheme());
        }
        return themes;
    }

    public Long save(Theme theme) {
        return themeDao.save(
                theme.getName(),
                theme.getDesc(),
                theme.getPrice());
    }

    public void delete(Long id) {
        themeDao.deleteById(id);
    }
}
