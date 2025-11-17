package io.osins.shared.common.Exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor
public class CustomException extends RuntimeException {
    private Integer code = 500;
    private String message = "服务异常,请稍后再试.";

    public CustomException(String message) {
        super(message);
    }

    public CustomException(Integer code, String message) {
        super(message);

        this.code = code;
        this.message = message;
    }
}
