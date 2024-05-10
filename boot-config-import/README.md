## Bean声明查找
在存在多个@Configuration配置类场合下，按照以下流程查找Bean声明
1. 由`ConfigurationClassPostProcessor`对多个@Configuration配置类，按@Order配置顺序执行
2. 相同@Order顺序，由`ConfigurationClassParser`对每一个Configuration进行解析，对应方法为`doProcessConfigurationClass`。以下解析类型的顺序：
   1. MemberClass 内部类或同级兄弟类
   2. PropertySource 指定
   3. ComponentScan 扫描
      1. 先检查@Configuration类自身是否有@ComponentScan
      2. 检查非直接@ComponentScan修饰
         SpringBoot应用的组件通常在此步被加载，因为启动类使用了@SpringBootApplication，内含ComponentScan，扫描包路径默认为启动类所属包路径及所有子包
   4. 检查@Configuration类是否有@Import修饰
      @Import支持多种传入值，分别说明
      1. ImportSelector
         1. 是DeferredImportSelector，优先级最低，添加到Parse的队列`deferredImportSelectors`，等待所有的Configuration都解析完毕后，再解析该队列
            1. 到解析DeferredImportSelector步骤，可能存在多个DeferredImportSelector，其初始化顺序由各个DeferredImportSelector的@Order控制
            2. DeferredImportSelectorGroupingHandler内存储一个groupings集合，默认按DeferredImportSelector进行分组，除非重写DeferredImportSelector的getImportGroup方法
         2. 否则，调用`ImportSelector#selectImports`获得bean的类路径集合，将其当做候选Configuration来处理，参考@Import 普通Object的支线流程
      2. ImportBeanDefinitionRegister
         1. // todo
      3. Object 将其视为 Configuration，递归调用`ConfigurationClassParser#doProcessConfigurationClass`解析方法
         1. 如果Object确实是Configuration实例，加载其中配置
         2. 如果Object只是一个普通类，`doProcessConfigurationClass`不会有任何效果，最后直接将普通类添加到Parser的configurationClasses数组内，进入后续BeanDefinition创建环节
   5. 检查@Configuration类是否有@ImportResource修饰。常用于导入XML配置文件
   6. 解析类内含@Bean修饰的方法
   7. 解析是否有接口默认方法被@Bean修饰
   8. 检查是否有父类，按上述步骤对父类进行递归解析

## BeanDefinition创建
由`ConfigurationClassBeanDefinitionReader`负责加载创建，执行入口为`loadBeanDefinitions`方法，对每个Configuration调用`loadBeanDefinitionsForConfigurationClass`
1. 判断Configuration是否有@Conditional修饰，判断是否需要skip
2. 判断Configuration是否通过@Import注解导入，是则注册到BeanRegistry内
   1. 通过`AnnotationConfigUtils.processCommonDefinitionAnnotations`检查公共注解，例如@Primary、@DependsOn等
   2. 通过`scopeMetadataResolver.resolveScopeMetadata`查询bean的Scope配置
   3. 生成beanName
   4. 将信息更新到Configuration对应的beanDefinition内
   5. 完成beanRegistry注册
3. 循环取出BeanMethod
   1. 使用`conditionEvaluator`对bean的@Conditional进行条件判断
   2. 获取@Bean注解的配置
   3. 决定beanName是取@Bean注解的name属性，还是方法名称
   4. `isOverriddenByExistingDefinition`判断是否覆盖
      1. 不存在同名bean，则继续原流程
      2. 存在，已存在同名bean也是通过@Bean注解声明
         1. 是同一个Configuration声明：**终止**当前beanMethod的注册。因此，同一个Configuration声明多个同名bean，仅第一个生效，其它无效
         2. 不是同一个Configuration声明：**覆盖**已声明bean
      3. 存在，已存在同名bean是通过scan创建出来的：当前bean**覆盖**扫描出来的bean
      4. 存在，已存在同名bean的role属于框架声明的，允许应用级别role的bean覆盖，当前bean**覆盖**框架同名bean
      5. 存在，通过xml创建的bean：**终止**当前beanMethod的注册。（如果registry配置了不允许覆盖，还会抛出异常）
   5. 通过`AnnotationConfigUtils.processCommonDefinitionAnnotations`检查公共注解，例如@Primary、@DependsOn等
   6. 其它信息填充到bd，如ProxyMode、scope、beanFactory引用、init-method/destroy-method
   7. 完成beanRegistry注册
      1. 在ListableBeanFactory注册时，会进行二次override检查，包括：beanFactory是否开启禁止Override
4. 完成Configuration的ImportResource导入
5. 完成Configuration的ImportBeanDefinitionRegister导入