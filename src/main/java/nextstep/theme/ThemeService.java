package nextstep.theme;

import lombok.RequiredArgsConstructor;
import nextstep.support.exception.NonExistThemeException;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(NonExistThemeException::new);
        themeDao.delete(id);
    }
}
