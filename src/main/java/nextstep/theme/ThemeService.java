package nextstep.theme;

import lombok.RequiredArgsConstructor;
import nextstep.exception.dataaccess.DataAccessErrorCode;
import nextstep.exception.dataaccess.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public void delete(Long id) {
        Optional<Theme> theme = themeDao.findById(id);
        if (theme.isEmpty()) {
            throw new DataAccessException(DataAccessErrorCode.THEME_NOT_FOUND);
        }

        themeDao.delete(id);
    }
}
