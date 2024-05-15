package space.itzkana.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.itzkana.model.ResponseResult;
import space.itzkana.openapi.model.ParamsHolder;

@RestController
@RequestMapping("hello")
@Tag(name = "Hello控制器")
public class HelloController {

    @Operation(summary = "Say Hello")
    @GetMapping("/{id}")
    public ResponseResult<String> hello(
            @Parameter(description = "PathVar ID") @PathVariable String id,
            ParamsHolder params) {
        return ResponseResult.success("Hello %s.%s".formatted(params.getTitle(), params.getName()));
    }

    @Operation(summary = "Transmit Data")
    @PostMapping
    public ResponseResult<ParamsHolder> post(@RequestBody ParamsHolder params) {
        System.out.printf("Receive data: %s%n", params);
        return ResponseResult.success(params);
    }
}
