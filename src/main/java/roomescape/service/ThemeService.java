package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeControllerPostBody;
import roomescape.entity.Theme;
import roomescape.exception.NotExistThemeException;
import roomescape.repository.ThemeRepository;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository repository;

    public Theme getTheme(long id) {
        var target = repository.selectById(id);
        if (target.isEmpty()) {
            throw new NotExistThemeException(id);
        }
        return target.get();
    }

    public long createTheme(ThemeControllerPostBody body) {
        return repository.insert(body.getName(), body.getDesc(), body.getPrice());
    }

    public Stream<Theme> pageTheme(int page) {
        return repository.selectPage(30, Math.max(page, 0))
                         .stream();
    }

    public void deleteTheme(long id) {
        var affectedRows = repository.delete(id);
        if (affectedRows == 0) {
            throw new NotExistThemeException(id);
        }
    }
}
