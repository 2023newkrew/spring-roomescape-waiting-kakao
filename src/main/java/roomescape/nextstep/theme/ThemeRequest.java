package roomescape.nextstep.theme;

public record ThemeRequest(String name, String desc, int price) {
    public Theme toEntity() {
        return new Theme(
                this.name,
                this.desc,
                this.price
        );
    }
}
