package auth.service.model;

import auth.exception.AuthException;
import errors.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;
import java.util.Optional;

@Component
@RequestScope
public class JWTTokenModel {
    // 자체적인 리퀘스트 단위의 상태관리를 위한 요소들
    private Token secretToken = null;

    public void initialize(Jws<Claims> jws) {
        if (!Objects.isNull(secretToken)) {
            // 이미 예약됨 에러
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        secretToken = new JWTTokenModel.Token(
                Long.parseLong(jws.getBody().getSubject()),
                (Boolean) jws.getBody().get("is_admin")
        );
    }

    public Optional<Token> token() {
        return Optional.ofNullable(secretToken);
    }

    @AllArgsConstructor
    @Getter
    public static class Token {
        private long memberId;
        private boolean isAdmin;
    }
}
