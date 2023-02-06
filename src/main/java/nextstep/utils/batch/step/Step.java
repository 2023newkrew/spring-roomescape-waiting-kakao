package nextstep.utils.batch.step;

import lombok.Builder;
import nextstep.utils.TransactionUtil;
import nextstep.utils.batch.processor.Processor;
import nextstep.utils.batch.reader.Reader;
import nextstep.utils.batch.writer.Writer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Step<T, V> {

    private Reader<T> reader;
    private Processor<T, V> processor;
    private Writer<V> writer;
    private TransactionUtil transactionUtil;

    @Builder
    public Step(Reader reader, Processor processor, Writer writer, TransactionUtil transactionUtil) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
        this.transactionUtil = transactionUtil;
    }

    public void start() {
        transactionUtil.executeTask(() -> {
            List<T> items = reader.read();

            List<V> processedItems = Objects.nonNull(processor)
                    ? items.stream()
                        .map(processor::process)
                        .collect(Collectors.toList())
                    : (List<V>) items;

            if (Objects.nonNull(writer)) {
                writer.write(processedItems);
            }
        });
    }

    public Reader<T> getReader() {
        return reader;
    }

    public Processor<T, V> getProcessor() {
        return processor;
    }

    public Writer<V> getWriter() {
        return writer;
    }

    public TransactionUtil getTransactionUtil() {
        return transactionUtil;
    }
}
