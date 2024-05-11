package space.itzkana.controller;

import jakarta.validation.groups.Default;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.itzkana.model.ErrorsEnum;
import space.itzkana.model.ResponseResult;
import space.itzkana.validator.group.CreateValidateGroup;
import space.itzkana.validator.group.UpdateValidateGroup;
import space.itzkana.vo.UserInput;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("user")
@RestController
public class UserController {

    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResult<UserInput> register(
            @Validated({CreateValidateGroup.class, Default.class})
            @RequestBody UserInput input,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            String message = "invalid parameter:" + IntStream.range(0, errors.size())
                    .mapToObj(idx -> {
                        FieldError fe = (FieldError) errors.get(idx);
                        return "%d %s:%s".formatted(idx + 1, fe.getField(), fe.getDefaultMessage());
                    })
                    .collect(Collectors.joining(";"));
            return ResponseResult.fail(ErrorsEnum.PARAM_INVALID.getCode(), message, null);
        }

        input.setUid(1L);
        return ResponseResult.success(input);
    }

    @PutMapping("phone")
    public ResponseResult<UserInput> changePhone(@Validated(UpdateValidateGroup.class) @RequestBody UserInput input) {
        return ResponseResult.success(input);
    }
}
