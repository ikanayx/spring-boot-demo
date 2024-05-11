package space.itzkana.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorsEnum {

    SUCCESS("0", "success"),

    PARAM_INVALID("400", "invalid parameter"),

    AUTHENTICATE_INVALID("401", "no authentication"),

    AUTHORITIES_NO("403", "no authorities"),

    SYSTEM_ERROR("500", "failed"),
    ;

    private final String code;
    private final String message;
}
