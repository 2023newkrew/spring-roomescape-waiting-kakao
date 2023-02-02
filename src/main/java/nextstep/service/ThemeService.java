package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.ThemeRequest;
import nextstep.domain.dto.response.ThemeResponse;
import nextstep.repository.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeDao themeDao;

    @Transactional
    public Long addTheme(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findAll() {
        return themeDao.findAll()
                .stream()
                .map(ThemeResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeTheme(Long id) {
        themeDao.deleteById(id);
    }
}
