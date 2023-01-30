package nextstep.theme.service;

import lombok.RequiredArgsConstructor;
import nextstep.exception.NotExistEntityException;
import nextstep.theme.dao.ThemeDao;
import nextstep.theme.model.Theme;
import nextstep.theme.model.ThemeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeDao themeDao;

    @Transactional
    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    @Transactional
    public void delete(Long id) {
        themeDao.findById(id)
                .orElseThrow(NotExistEntityException::new);

        themeDao.delete(id);
    }
}
