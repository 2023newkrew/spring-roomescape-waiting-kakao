package roomwaiting.nextstep.theme;

import roomwaiting.nextstep.schedule.ScheduleDao;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

import static roomwaiting.support.Messages.*;

@Service
public class ThemeService {
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;

    public ThemeService(ThemeDao themeDao, ScheduleDao scheduleDao) {
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
    }

    public Long create(ThemeRequest themeRequest) {
        if (themeDao.isExistsByNameAndPrice(themeRequest)){
            throw new DuplicateKeyException(ALREADY_REGISTERED_THEME.getMessage());
        }
        return themeDao.save(themeRequest.toEntity());

    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public void delete(Long id) {
        themeDao.findById(id).orElseThrow(() ->
                new NullPointerException(THEME_NOT_FOUND.getMessage() + ID + id));
        if (scheduleDao.isExistsByThemeId(id)) {
            throw new DuplicateKeyException(ALREADY_REGISTERED_SCHEDULE.getMessage());
        }
        themeDao.delete(id);
    }
}
