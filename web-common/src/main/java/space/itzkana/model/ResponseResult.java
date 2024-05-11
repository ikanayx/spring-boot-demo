package space.itzkana.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseResult<T> {

    private long timestamp;
    private String code;
    private String message;
    private T data;

    public static <T> ResponseResult<T> success(T data) {
        return ResponseResult.<T>builder()
                .timestamp(System.currentTimeMillis())
                .code(ErrorsEnum.SUCCESS.getCode())
                .message(ErrorsEnum.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    public static <T> ResponseResult<T> fail(String code, String message, T data) {
        return ResponseResult.<T>builder()
                .timestamp(System.currentTimeMillis())
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ResponseResult<T> fail(ErrorsEnum error, T data) {
        return fail(error.getCode(), error.getMessage(), data);
    }

    public static <T> ResponseResult<T> fail(String message, T data) {
        return fail(ErrorsEnum.SYSTEM_ERROR.getCode(), message, data);
    }
}
