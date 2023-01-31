package nextstep.theme;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.exceptions.exception.NotExistEntityException;
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
        themeDao.findById(id).orElseThrow(NotExistEntityException::new);
        themeDao.delete(id);
    }
}
