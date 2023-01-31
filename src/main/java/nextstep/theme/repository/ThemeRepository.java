package nextstep.theme.repository;

import nextstep.theme.domain.ThemeEntity;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface ThemeRepository {

    ThemeEntity insert(ThemeEntity theme) throws DuplicateKeyException;

    ThemeEntity getById(Long id);

    List<ThemeEntity> getAll();

    boolean update(Long id, ThemeEntity theme) throws DuplicateKeyException;

    boolean delete(Long id);
}
