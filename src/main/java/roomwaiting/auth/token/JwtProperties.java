package roomwaiting.auth.token;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private Long validateMilliSeconds;

    public String getSecretKey() {
        return secretKey;
    }

    public Long getValidateMilliSeconds() {
        return validateMilliSeconds;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setValidateMilliSeconds(Long validateMilliSeconds) {
        this.validateMilliSeconds = validateMilliSeconds;
    }
}
