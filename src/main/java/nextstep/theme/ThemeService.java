package nextstep.theme;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    public Optional<Theme> findById(Long id) {
        return themeDao.findById(id);
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public int delete(Long id) {
        return themeDao.delete(id);
    }
}
