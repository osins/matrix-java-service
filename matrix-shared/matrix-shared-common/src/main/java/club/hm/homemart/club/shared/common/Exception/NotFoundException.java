package club.hm.homemart.club.shared.common.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class NotFoundException extends CustomException {
    public NotFoundException(){
        super("未找到该资源");
    }
}
