package nextstep.service;

import nextstep.domain.theme.Theme;
import nextstep.domain.theme.ThemeDao;
import nextstep.dto.request.ThemeRequest;
import nextstep.error.ApplicationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.error.ErrorType.THEME_NOT_FOUND;

@Service
public class ThemeService {
    private ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public void delete(Long id) {
        Theme theme = themeDao.findById(id);
        if (theme == null) {
            throw new ApplicationException(THEME_NOT_FOUND);
        }

        themeDao.delete(id);
    }
}
