package nextstep.theme.service;

import nextstep.theme.dto.ThemeRequest;
import nextstep.theme.dto.ThemeResponse;

import java.util.List;

public interface ThemeService {

    ThemeResponse create(ThemeRequest request);

    ThemeResponse getById(Long id);

    List<ThemeResponse> getAll();

    ThemeResponse update(Long id, ThemeRequest request);

    boolean deleteById(Long id);
}
