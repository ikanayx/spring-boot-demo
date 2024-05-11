package space.itzkana.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import space.itzkana.TestValidationI18n;
import space.itzkana.model.ErrorsEnum;
import space.itzkana.vo.UserInput;

import java.util.Locale;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = TestValidationI18n.class)
@Slf4j
public class UserControllerTest {

    @Autowired // 先启用自动配置@AutoConfigureMockMvc，才能自动注入
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        // 如果用standaloneSetup创建一个mockMvc对象，项目定义的controllerAdvice和localValidatorFactoryBean，会被mockMvc的其它默认实现覆盖
        // mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }

    @Test
    void contextLoads() throws Exception {
        requestOk();
        changePhone(Locale.of("en", "US"), result -> this.expect(result, "uid can not be null"));
        changePhone(Locale.of("zh", "CN"), result -> this.expect(result, "用户uid不能为空"));
        changePhone(Locale.of("zh", "TW"), result -> this.expect(result, "用戶uid不能為空"));
    }

    void requestOk() throws Exception {
        UserInput input = UserInput.builder().phone("+86 185 2011 4612").build();
        String bodyJson = mapper.writeValueAsString(input);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson);
        mockMvc.perform(request).andExpect(status().isOk());
    }

    void changePhone(Locale locale, Consumer<ResultActions> i18nText) throws Exception {

        UserInput input = UserInput.builder().phone("+86 185 2011 461").build();
        String bodyJson = mapper.writeValueAsString(input);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/user/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(bodyJson);

        if (locale != null) {
            request.header("locale", locale.toString());
        }

        ResultActions actions = mockMvc.perform(request);
        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is(ErrorsEnum.PARAM_INVALID.getMessage())))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorsEnum.PARAM_INVALID.getCode())))
        //.andDo(print())
        ;

        i18nText.accept(actions);
    }

    void expect(ResultActions result, String text) {
        try {
            result.andExpect(jsonPath("$.data[0]", Matchers.is(text)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
