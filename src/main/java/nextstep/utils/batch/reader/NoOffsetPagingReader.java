package nextstep.utils.batch.reader;

public interface NoOffsetPagingReader<T> extends Reader<T> {

    void setChunkSize(int chunkSize);
    void setLastItemId(Long id);

}
