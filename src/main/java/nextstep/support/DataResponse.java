package nextstep.support;

import java.util.List;

public class DataResponse<T extends List<?>> {
    private final T data;

    public DataResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public static <T extends List<?>> DataResponse<T> of(T obj) {
        return new DataResponse<>(obj);
    }
}
