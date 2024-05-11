### 校验和i18n文本替换过程

1. Web容器初始化阶段

   1. 创建`@Bean LocalValidatorFactoryBean`时，根据bootstrap.configuration的扫描，validatorFactory由hibernate提供实现
   2. 创建`RequestMappingHandlerAdapter`时，根据mvcValidator生成并设置`webBindingInitializer`属性，该属性在后续接口调用时生成校验用的DataBindFactory
   3. 利用反射，获得业务Controller的Method，包装成为`HandlerMapping`实例

2. 调用接口阶段

   1. 由`DispatchServlet`匹配到目标`HandlerMapping`后，传给对应的HandlerAdapter适配器

      > spring webmvc中@RestController通常对应的适配器类型为：`RequestMappingHanderAdapter`

   2. Adapter适配器接收到`HandlerMapper`对象

      1. 通过`getDataBinderFactory()`方法，创建dataBinderFactory，为后续调用方法提供所需参数

      2. 创建`InvocableHandlerMethod`，为其设置dataBinderFactory、argumentResolvers、returnValueResolvers等解析器后，进入`invokeHandlerMethod()`

      3. 在`getMethodArgumentValues()`步骤，对Controller方法声明的每一个参数，查找合适的`HandlerMethodArgumentResolver`

         > @RequestBody修饰的参数，对应的Resolver是RequestResponseBodyMethodProcessor
         >
         > BindingResults类型的参数，对应的Resolver是ErrorsMethodResolver

      4. 在`RequestResponseBodyMethodResolver#resolveArgument`方法中

         1. 先从ServletRequest解析到请求body数据，实例化对应参数类型对象，MessageConverter做数据转换(如JSON解析)

         2. 使用dataBindFactory生成dataBinder

         3. 调用`AbstractMessageConvertorMethodArgumentResolver#validateIfApplicable()。

            1. 判断参数是否直接被@Valid或@Validated修饰
            2. 参数的类型声明是否有@Validated修饰
            3. 判断注解名称是否以Valid为前缀
            4. 上述条件任意符合一个，使用dataBinder#validate方法进行校验

            i18n文本替换的位置

            - org.hibernate.validator.internal.engine.ValidatorImpl

            - org.hibernate.validator.internal.engine.constraintvalidation.ConstraintTree

            - org.hibernate.validator.internal.engine.validationcontext.AbstractValidationContext#addConstraintFailure

            - org.hibernate.validator.internal.engine.validationcontext.AbstractValidationContext#interpolate

              其中，validatorScopedContext.getMessageInterpolator()由LocalValidatorFactoryBean初始化时设置



### 使用注意事项

#### 分组校验

如果使用@Validated(XxxGroup.class)方式校验某一个分组，则只校验以下属性

- 使用了validation注解修饰
- 注解指定了groups = XxxGroup.class

如果上述条件满足第一点、未满足第二点，该属性将不会被校验

如果有多个属性，在所有分组场景下都需要校验

- 该属性的所有validation注解内groups指定所有分组

  - 优点：每个场景需要校验的字段明确
  - 缺点：会书写大量重复代码

- 使用分组校验时，传入默认分组。如：@Validated({ jakarta.validation.groups.Default.class, XxxGroup.class })

  - 优点：减少重复代码书写；可控制校验分组顺序
  - 缺点：对象存在多处位置需要被校验，上述注解还是需要冗余多份

- 创建分组时，让其继承 jakarta.validation.groups.Default.class

  ```java
  public interface XxxGroup extends jakarta.validation.groups.Default {}
  ```

  - 优点：减少重复代码书写
  - 缺点：校验顺序会固定为：先校验XxxGroup.class标记的字段、再校验无分组（Default.class）字段



### 