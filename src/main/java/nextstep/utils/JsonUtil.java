package nextstep.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.error.ApplicationException;
import org.springframework.stereotype.Component;

import static nextstep.error.ErrorType.JSON_CONVERT_ERROR;

@Slf4j
@RequiredArgsConstructor
@Component
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public String toString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("[JsonUtils] Object to json mapping failed. Object: {}", obj);
            throw new ApplicationException(JSON_CONVERT_ERROR);
        }
    }

    public <T> T toObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("[JsonUtils] Json to object mapping failed. Json: {}", json);
            throw new ApplicationException(JSON_CONVERT_ERROR);
        }
    }

}
