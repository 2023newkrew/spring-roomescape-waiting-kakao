package nextstep.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Theme {
    private Long id;
    private String name;
    private String desc;
    private int price;

    public Theme(String name, String desc, int price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
    }
}
