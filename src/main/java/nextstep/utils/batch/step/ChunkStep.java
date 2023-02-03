package nextstep.utils.batch.step;

import nextstep.utils.TransactionUtil;
import nextstep.utils.batch.processor.Processor;
import nextstep.utils.batch.reader.NoOffsetPagingReader;
import nextstep.utils.batch.writer.Writer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChunkStep<T, V> extends Step<T, V> {

    private int chunkSize;
    private boolean endFlag;

    public ChunkStep(int chunkSize, NoOffsetPagingReader reader, Processor processor, Writer writer, TransactionUtil transactionUtil) {
        super(reader, processor, writer, transactionUtil);
        reader.setChunkSize(chunkSize);
        this.chunkSize = chunkSize;
    }

    public void start() {
        while (!endFlag) {
            getTransactionUtil().executeTask(() -> {
                List<T> items = getReader().read();
                if (items.size() < chunkSize) {
                    endFlag = true;
                }

                List<V> processedItems = items.stream()
                        .map(getProcessor()::process)
                        .collect(Collectors.toList());

                if (Objects.nonNull(getWriter())) {
                    getWriter().write(processedItems);
                }
            });
        }
    }

    public static class ChunkStepBuilder<T, V> {

        private int chunkSize;
        private NoOffsetPagingReader<T> reader;
        private Processor<T, V> processor;
        private Writer<V> writer;
        private TransactionUtil transactionUtil;

        public static <T, V> ChunkStepBuilder<T, V> builder() {
            return new ChunkStepBuilder();
        }

        public ChunkStepBuilder<T, V> chunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
            return this;
        }

        public ChunkStepBuilder<T, V> reader(NoOffsetPagingReader<T> reader) {
            this.reader = reader;
            return this;
        }

        public ChunkStepBuilder<T, V> processor(Processor<T, V> processor) {
            this.processor = processor;
            return this;
        }

        public ChunkStepBuilder<T, V> writer(Writer<V> writer) {
            this.writer = writer;
            return this;
        }

        public ChunkStepBuilder<T, V> transactionUtil(TransactionUtil transactionUtil) {
            this.transactionUtil = transactionUtil;
            return this;
        }

        public ChunkStep build() {
            return new ChunkStep(chunkSize, reader, processor, writer, transactionUtil);
        }
    }

}
