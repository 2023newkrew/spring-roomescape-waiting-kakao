package nextstep.utils.batch.writer;

import java.util.List;

public class CompositeWriter<T> implements Writer<T> {

    private Writer<T>[] writers;

    public void setWriters(Writer<T>... writers) {
        this.writers = writers;
    }

    @Override
    public void write(List<T> items) {
        for (Writer<T> writer : writers) {
            writer.write(items);
        }
    }
}
