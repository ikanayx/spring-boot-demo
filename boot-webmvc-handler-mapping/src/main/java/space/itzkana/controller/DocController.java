package space.itzkana.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.itzkana.annotation.ApiVersion;
import space.itzkana.model.Doc;
import space.itzkana.model.ResponseResult;

@RestController
@RequestMapping("doc")
@Slf4j
public class DocController {

    @GetMapping("/{id}")
    public ResponseResult<Doc> get(@PathVariable("id") String id, @ModelAttribute("reqVersion") String reqVersion) {
        return ResponseResult.success(new Doc(id, "default"));
    }

    @GetMapping("/{id}")
    @ApiVersion("1.0.0")
    public ResponseResult<Doc> get_1_0_0(@PathVariable("id") String id, @ModelAttribute("reqVersion") String reqVersion) {
        return ResponseResult.success(new Doc(id, "1.0.0"));
    }

    @GetMapping("/{id}")
    @ApiVersion("1.9.0")
    public ResponseResult<Doc> get_1_9_0(@PathVariable("id") String id, @ModelAttribute("reqVersion") String reqVersion) {
        return ResponseResult.success(new Doc(id, "1.9.0"));
    }

    @GetMapping("/{id}")
    @ApiVersion("1.10.0")
    public ResponseResult<Doc> get_1_10_0(@PathVariable("id") String id, @ModelAttribute("reqVersion") String reqVersion) {
        return ResponseResult.success(new Doc(id, "1.10.0"));
    }

    @GetMapping("/{id}")
    @ApiVersion("1.10.1")
    public ResponseResult<Doc> get_1_10_1(@PathVariable("id") String id, @ModelAttribute("reqVersion") String reqVersion) {
        return ResponseResult.success(new Doc(id, "1.10.1"));
    }
}
