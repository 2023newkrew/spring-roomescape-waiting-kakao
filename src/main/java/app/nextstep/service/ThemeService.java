package app.nextstep.service;

import app.nextstep.dao.ThemeDao;
import app.nextstep.domain.Theme;
import app.nextstep.support.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {
    private ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Long create(String name, String desc, int price) {
        return themeDao.save(name, desc, price);
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public void delete(Long id) {
        Theme theme = themeDao.findById(id);
        if (theme == null) {
            throw new EntityNotFoundException();
        }

        themeDao.delete(id);
    }
}
