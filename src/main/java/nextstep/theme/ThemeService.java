package nextstep.theme;

import static nextstep.exception.ErrorMessage.NOT_EXIST_THEME;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeDao themeDao;

    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public void delete(Long id) {
        themeDao.findById(id)
            .orElseThrow(() -> new NullPointerException(NOT_EXIST_THEME.getMessage()));

        themeDao.delete(id);
    }
}
