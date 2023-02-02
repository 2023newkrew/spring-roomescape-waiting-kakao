package app.nextstep.service;

import app.nextstep.domain.Theme;
import app.nextstep.repository.ThemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {
    private ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Long create(Theme theme) {
        return themeRepository.save(theme);
    }

    public void delete(Long id) {
        themeRepository.delete(id);
    }
}
