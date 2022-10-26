# 基础版本spring容器 - version 3.2
---
## 核心模型
---
### BeanFactory工厂
+ 1、实例的元数据定义 BeanDefinition
    + 类class
    + 属性值 （基础类型 + 引用类型）
    + 自定义配置 初始逻辑方法 initMethodName
    + 自定义配置 销毁逻辑方法 destroyMethodName
+ 2、实例属性元数据定义 PropertyValue
    + 属性名称
    + 属性值(对象引用/基础类型)

+ 3、实例注册器 DefaultSingletonBeanRegistry
  + 单例存储缓存 Map<String,Object> singletonBeansMap
  + 可销毁实例缓存 Map<String, DisposableBean> disposableBeansMap

+ 4、BeanFactory创建工厂
    + 顶层定义获取bean行为 BeanFactory
      + 增加 指定名称 获取bean实例
      + 增加 指定名称、类型 获取bean实例
      + 增加 指定类型 获取bean实例
    + 中间抽象层定义获取bean抽象获取-创建实现模版行为 AbstractBeanFactory
      + 增加bean实例增强配置 List<BeanPostProcessor> beanPostProcessors
      + 属性配置值处理器 List<StringValueResolver> valueResolvers
    + 底层抽象层实现bean的创建流程 AbstractAutowireCapableBeanFactory
      + 增加 实例创建策略 InstantiationStrategy
        + jdk反射机制实现
        + cglib动态字节码实现
      + 增加 实例属性填充
      + 增加 bean初始化流程 initializeBean 拓展初始化前后增强
        + 前置 applyBeanPostProcessorBeforeInitialization
        + 自定义初始化 invokeBeanInitMethods
          + 接口定义初始化入口
          + 配置定义初始化入口
        + 后置 applyBeanPostProcessorAfterInitialization
      + 增加 可销毁DisposableBean 注册入口
        + registryDisposableBeanIfNecessary方法
    + 末端工厂实现类 DefaultListableBeanFactory
      + 继承以上的工厂行为，实现实例、元数据动作
      + 增加 销毁实例入口 destroySingletons
+ 4、1 外部自定义实例注册容器支持FactoryBeanRegistrySupport
    + 容器实例存储缓存 ConcurrentHashMap<String,Object> factoryBeanMap
    + 在beanFactory容器创建bean时 判断是否是FactoryBean的继承者 会注册其getObject方法返回的结果
  + 增加 FactoryBean外部自定义 bean实例化工厂
  
+ 5、数据源读取配置策略 core.io包

+ 6、bean元数据xml配置读取器 XmlBeanDefinitionReader
  + 定义标签 XmlTagEnum
  + 增加解析 自定义初始化、销毁方法解析

---
### bean生命周期
+ 0、工厂级别增强处理 BeanFactoryPostProcessor
  + postProcessBeanFactory bean容器加载完配置后执行

+ 1、提前于bean实例初始化BeanPostProcessor的子类
  + 容器上下文增强处理 ApplicationContextAwareProcessor
  + 实例化前置通知 InstantiationAwareBeanPostProcessor
    + bean初始化前置处理 postProcessBeforeInstantiation

+ 2、各种通知处理Aware
  + BeanFactory工厂通知 BeanFactoryAware
  + bean加载器通知 BeanClassLoaderAware
  + bean名称通知 BeanNameAware

+ 2、bean实例级别增强处理 BeanPostProcessor
  + postProcessBeforeInitialization 前置
  + postProcessAfterInitialization 后置

+ 3、实例初始化定义接口 InitializingBean
  + afterPropertiesSet
  + 1、在beanFactory对bean填充完原始属性之后
  + 2、在beanPostProcessor对初始化 前置增强之后
  + 3、在bean配置 自定义配置初始化方法之前
  + 4、在beanPostProcessor对初始化 后置增强之前

+ 4、实例销毁定义接口 DisposableBean
  + 销毁接口适配累DisposableBeanAdapter
    + 兼容 未实现DisposableBean接口但定义了init-method的类

---
### 容器上下文Application
+ 1、增加 系统上下文容器 ApplicationContext
  + 抽象上下文定义 AbstractApplicationContext
    + refresh 容器初始化模版行为定义
    + invokeBeanFactoryPostProcessors 容器加载配置完后增强方法
    + registryBeanPostProcessors 注册bean实例级别增强
    + close 关闭容器
    + registryShutdownHook 关闭容器钩子函数
  + 初始化抽象上下文 AbstractRefreshableApplicationContext
    + refreshBeanFactory  定义容器加载配置初始化
    + loadBeanDefinitions 定义加载配置抽象
  + xml加载抽象上下文 AbstractXmlApplicationContext
    + loadBeanDefinitions 实现加载配置方式xmlReader
  + 基于项目路径xml配置的上下文 ClasspathApplicationContext
    + 定义 配置文件位置
    + 初始化动作在实例化之后

  + bean上下文容器通知 ApplicationContextAware
    + 有ApplicationContextAware处理器完成通知动作
      + ApplicationContextAwareProcessor

---
### 容器 事件发布体系
+ 1、增加 事件定义 ApplicationEvent
  + 所有的事件必须继承抽象事件 ApplicationEvent

+ 2、增加 事件发布定义 ApplicationEventPublisher
  + AbstractApplicationContext 继承 AbstractApplicationContext 发布事件能力

+ 3、增加 事件监听定义 ApplicationListener
  + 所有的事件监听必须实现 ApplicationListener

+ 4、增加 事件监听发布处理中心枢纽 ApplicationEventMultiCater
  + 内部/外部监听器存储容器 Set<ApplicationListener<? extends ApplicationEvent>> listeners
  + 事件监听器注册入口 addApplicationListener addApplicationListeners
  + 事件发布处理入口 multiCastEvent
  + 核心处理中心 AbstractApplicationEventMultiCater

+ 5、spring容器内部自带事件和监听器
  + ApplicationContextEventListener
  + ApplicationContextCloseEvent
  + ApplicationContextRefreshEvent

---
### bean注解定义,包扫描，普通属性、bean属性xml配置/注解注入
+ 1、增加注册基础支持
  + bean标注 Component
  + bean使用范围 Scope
  + bean注入注解(类型匹配) @Autowired
  + bean名称修饰注解(搭配 @Autowired) @Qualifier
  + 属性注入注解 @Value
  
+ 2、xml读取解析 增加 包扫描支持
  + 增加解析 component-scan标签base-package属性

+ 3、增加注解bean读取解析
  + 扫描包下被注解的class文件 ClasspathScanningCandidateComponentProvider
  + 解析class属性 注册beanDefinition元数据定义 ClasspathBeanDefinitionScanner

+ 4、bean属性支持配置文件支持(***xml形式配置方式属性注入***) PropertyPlaceholderConfigurer
  + 属性配置规则 ${config_name}
  + 实现BeanFactoryPostProcessor,在beanFactory加载完bean定义后 处理属性配置替换beanDefinition的属性值

+ 5、配置文件属性配置解析器 StringValueResolver
  + 解析使用@Value注解或者xml配置中 ${}占位符表达式 进行属性配置
  + 在PropertyPlaceholderConfigurer读取配置资源文件，并注册 ***属性值解析起***
  + 在AutowiredAnnotationBeanPostProcessor中 反射获取@Value修饰的属性 并解析回调解析器

+ 6、增加 注解式bean解析增强处理器 ***AutowiredAnnotationBeanPostProcessor***
  + 继承自InstantiationAwareBeanPostProcessor ，增强bean初始化前
    + ***postProcessPropertyValues*** 允许bean初始化之前修改属性
  + 该处理器注册到bean容器时机: 在注解包扫描完类加载并注册完@Component @Service等修饰的类定义之后
    ,在ClasspathBeanDefinitionScanner中提前注册该处理器
  + ***resolveValueAnnotation*** 处理普通值属性注入
  + ***resolveAutowiredAnnotation*** 处理bean属性注入
