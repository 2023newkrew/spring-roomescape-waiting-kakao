package nextstep.service;

import nextstep.domain.theme.Theme;
import nextstep.domain.theme.ThemeDao;
import nextstep.dto.request.ThemeRequest;
import nextstep.dto.response.ThemeResponse;
import nextstep.error.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ThemeResponse> findAll() {
        return themeDao.findAll()
                .stream()
                .map(ThemeResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public void checkThemeExists(Long id) {
        if (!themeDao.existsById(id)) {
            throw new ApplicationException(THEME_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public Theme findById(Long id) {
        return themeDao.findById(id)
                .orElseThrow(() -> new ApplicationException(THEME_NOT_FOUND));
    }

    @Transactional
    public void delete(Long id) {
        checkThemeExists(id);
        themeDao.delete(id);
    }
}
