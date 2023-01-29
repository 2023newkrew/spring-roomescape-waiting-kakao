package nextstep.service;

import nextstep.domain.theme.Theme;
import nextstep.domain.theme.ThemeDao;
import nextstep.dto.request.ThemeRequest;
import nextstep.error.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.error.ErrorType.THEME_NOT_FOUND;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @Transactional
    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    @Transactional(readOnly = true)
    public Theme findById(Long id) {
        return themeDao.findById(id)
                .orElseThrow(() -> new ApplicationException(THEME_NOT_FOUND));
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        themeDao.delete(id);
    }
}
