package com.nextstep.domains.theme;

import com.nextstep.domains.theme.entities.ThemeEntity;
import com.nextstep.domains.theme.dtos.ThemeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {
    private ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    public List<ThemeEntity> findAll() {
        return themeDao.findAll();
    }

    public void delete(Long id) {
        ThemeEntity theme = themeDao.findById(id);
        if (theme == null) {
            throw new NullPointerException();
        }

        themeDao.delete(id);
    }
}
