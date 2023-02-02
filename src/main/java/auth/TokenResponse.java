package auth;

public class TokenResponse {
    private String accessToken;

    /* RestAssured에서 사용 */
    @SuppressWarnings("unused")
    public TokenResponse() {
    }

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
