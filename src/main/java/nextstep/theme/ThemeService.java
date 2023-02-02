package nextstep.theme;

import org.springframework.stereotype.Service;

import java.util.List;
import nextstep.exceptions.exception.NotFoundObjectException;
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
        themeDao.findById(id).orElseThrow(NotFoundObjectException::new);
        themeDao.delete(id);
    }
}
