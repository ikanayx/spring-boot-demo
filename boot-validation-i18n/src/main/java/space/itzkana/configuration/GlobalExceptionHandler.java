package space.itzkana.configuration;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import space.itzkana.model.ErrorsEnum;
import space.itzkana.model.ResponseResult;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseResult<?> handleValidationException(Exception e) {
        List<String> errorMessages = new ArrayList<>();
        if (e instanceof BindException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(errorMessages::add);
        } else if (e instanceof ConstraintViolationException) {
            if (e.getMessage() != null) {
                errorMessages.add(e.getMessage());
            }
        } else {
            errorMessages.add(e.getMessage());
        }
        return ResponseResult.fail(ErrorsEnum.PARAM_INVALID, errorMessages);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult<?> handleUnHandleException(Exception exception) {
        log.error("unHandleException", exception);
        return ResponseResult.fail(ErrorsEnum.SYSTEM_ERROR, null);
    }
}
