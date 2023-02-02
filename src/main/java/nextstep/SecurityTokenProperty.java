package nextstep;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt.token")
public class SecurityTokenProperty {
    private String secretKey;
    private String expireLength;

    public String getSecretKey() {
        return secretKey;
    }

    public String getExpireLength() {
        return expireLength;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpireLength(String expireLength) {
        this.expireLength = expireLength;
    }
}
