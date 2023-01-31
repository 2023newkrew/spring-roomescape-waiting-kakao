package nextstep.theme;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Theme {
    private final Long id;
    @NonNull
    private final String name;
    @NonNull
    private final String desc;
    @NonNull
    private final Integer price;


    public Theme(String name, String desc, int price) {
        this(null, name, desc, price);
    }

}
