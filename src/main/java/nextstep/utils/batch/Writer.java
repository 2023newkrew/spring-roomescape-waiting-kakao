package nextstep.utils.batch;

import java.util.List;

public interface Writer<T> {

    void write(List<T> items);

}
