package nextstep.utils.batch.writer;

import java.util.List;

public interface Writer<T> {

    void write(List<T> items);

}
