package nextstep.utils.batch;

import lombok.Builder;
import nextstep.utils.TransactionUtil;

import java.util.List;
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

            List<V> processedItems = items.stream()
                    .map(processor::process)
                    .collect(Collectors.toList());

            writer.write(processedItems);
        });
    }
}
