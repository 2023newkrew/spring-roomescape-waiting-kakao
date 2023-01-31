package nextstep.theme;

import java.util.List;
import java.util.Objects;
import nextstep.exception.NotFoundException;
import nextstep.theme.dto.ThemeRequest;
import org.springframework.stereotype.Service;

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

    public void deleteById(Long id) {
        Theme theme = themeDao.findById(id);
        if (Objects.isNull(theme)) {
            throw new NotFoundException("해당하는 테마가 존재해야 합니다.", "null", "delete by id",
                    ThemeService.class.getSimpleName());
        }
        themeDao.delete(id);
    }
}
