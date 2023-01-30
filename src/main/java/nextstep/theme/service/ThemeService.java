package nextstep.theme.service;

import lombok.RequiredArgsConstructor;
import nextstep.exception.NotExistEntityException;
import nextstep.theme.dao.ThemeDao;
import nextstep.theme.model.Theme;
import nextstep.theme.model.ThemeRequest;
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
                .orElseThrow(NotExistEntityException::new);

        themeDao.delete(id);
    }
}
