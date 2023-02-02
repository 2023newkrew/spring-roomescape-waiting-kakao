package nextstep.theme;

import nextstep.support.NotExistEntityException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public Boolean delete(Long id) {
        return themeDao.delete(id);
    }

    public void checkIsExist(Long id) {
        if (themeDao.findById(id).isEmpty()) {
            throw new NotExistEntityException();
        }
    }
}
