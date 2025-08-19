package club.hm.matrix.auth.oauth2.server.vo;

import club.hm.matrix.auth.oauth2.server.enums.ClientID;
import club.hm.matrix.auth.oauth2.server.enums.GrantType;

import java.io.Serializable;

public interface IGrantTypeRequest extends Serializable {
    GrantType getGrantType();
}
