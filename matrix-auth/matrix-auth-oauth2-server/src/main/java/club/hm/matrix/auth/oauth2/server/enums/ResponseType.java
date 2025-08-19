package club.hm.matrix.auth.oauth2.server.enums;

import lombok.Getter;

@Getter
public enum ResponseType {
    UNKNOWN("unknown", "Unknown"),
    CODE("code", "Authorization Code"),
    TOKEN("token", "Implicit"),
    DEVICE_CODE("device_code", "Device Code"),
    IDENTITY("identity", "Identity"),
    PASSWORD("password", "Password"),
    ;

    private final String code;
    private final String description;

    ResponseType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ResponseType of(String code){
        if(code==null)
            return UNKNOWN;

        for(ResponseType responseType : values()){
            if(responseType.code.equals(code))
                return responseType;
        }

        return UNKNOWN;
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }
}
