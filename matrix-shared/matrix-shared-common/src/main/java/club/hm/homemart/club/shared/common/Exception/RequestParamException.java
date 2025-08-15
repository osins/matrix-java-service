package club.hm.homemart.club.shared.common.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestParamException extends CustomException {
    private Integer code = 5000;
    private String message = "请求参数错误";

    public RequestParamException(String message) {
        super(message);

        this.message = message;
    }
}
