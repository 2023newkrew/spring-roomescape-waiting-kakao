package nextstep.theme;

import nextstep.exception.ErrorCode;
import nextstep.exception.RoomEscapeException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!themeDao.deleteById(id)) {
            throw new RoomEscapeException(ErrorCode.ENTITY_NOT_EXISTS);
        }
    }

    public void checkIsExist(Long id) {
        if (themeDao.findById(id).isEmpty()) {
            throw new RoomEscapeException(ErrorCode.ENTITY_NOT_EXISTS);
        }
    }
}
