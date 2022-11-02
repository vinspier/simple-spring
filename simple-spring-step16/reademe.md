# 简易基础spring容器 - version 3.4
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
      + 增加 指定参数 获取实例
    + 中间抽象层定义获取bean抽象获取-创建实现模版行为 AbstractBeanFactory
      + 增加bean实例增强配置 List<BeanPostProcessor> beanPostProcessors
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
      + 例如：切面切点 动态代理 (废弃)
    + bean实例化后置增强方法 postProcessAfterInstantiation
      + 例如：切面切点 动态代理
    + bean初始化前置属性处理 postProcessPropertyValues
      + 例如：注解式注入属性
  
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
### AOP切面编程
+ 1、切面切点规则定义 AspectjExpressionPointcut
  + 类匹配 ClassFilter
  + 方法匹配 MethodMatcher

+ 2、切点支持配置 AdvisedSupport
  + 目标对象源 TargetSource
    + target目标对象本身可能已经是代理对象了 JDK/CGLIB
    取决于 beanFactory工厂原始实例化的策略 InstantiationStrategy
  + 方法拦截器 MethodInterceptor
    + 目标对象(代理工厂生产)执行目标方法时，会进入代理对象的invoke钩子函数，继而获取拦截器，进入拦截器逻辑（即切面）
    + 在拦截器内部可增强方法(前置、后置、环绕等)
  + 方法校验器 MethodMatcher

+ 3、代理对象生产工厂 AopProxy
  + JDK动态代理 JdkDynamicAopProxy
    + 实现调用处理器 InvocationHandler
  + cglib动态代理 Enhancer生产
    + 需要一个回调拦截 MethodInterceptor

+ 4、通知处理配置
  + 通知配置器抽象 Advisor
    + 获取通知类型 Advice
      + 方法通知 MethodAdvice
        + 前置增强方法通知 MethodBeforeAdvice
  + 切面配置抽象 PointcutAdvisor
    + 获取切面切点 pointcut
  + 切点通知配置实现 AspectjExpressionPointcutAdvisor
    + 切面切点配置 AspectjExpressionPointcut
    + 通知行为配置 Advice

+ 5、符合切面类的动态代理对象自动注入 DefaultAdvisorAutoProxyCreator
  + 实现bean工厂通知 BeanFactoryAware
  + 实例化前置通知 InstantiationAwareBeanPostProcessor
    + 创建bean实例前，获取切面定义，是否需要生成动态代理对象

---
### bean注解定义,包扫描
+ 1、增加注册基础支持
  + bean声明注解 @Component
  + bean使用范围注解 @Scope

+ 2、xml读取解析 增加 包扫描支持
  + 增加解析 component-scan标签base-package属性

+ 3、增加注解bean读取解析
  + 扫描包下被注解的class文件 ClasspathScanningCandidateComponentProvider
  + 解析class属性 注册beanDefinition元数据定义 ClasspathBeanDefinitionScanner

+ 4、增加 bean属性支持配置文件支持 PropertyPlaceholderConfigurer
  + 属性配置规则 ${config_name}
  + 实现BeanFactoryPostProcessor,在beanFactory加载完bean定义后 处理属性配置替换beanDefinition的属性值

---
### bean circle dependency
+ 1、三级缓存 存放不同时期bean实例 DefaultSingletonBeanRegistry
  + 一级缓存 Map<String,Object> singletonBeansMap
    + 存放完整的bean 属性都填充完毕 代理已经完成
  + 二级缓存 Map<String,Object> earlySingletonBeansMap
    + 存放 初始需要暴露被引用的bean 属性还未填充完毕 代理已经完成
  + 三级缓存 Map<String,ObjectFactory<?>> singletonFactoriesMap
    + 存放 bean的创建工厂
    + ObjectFactory<T> 尤其是需要代理工厂创建的实例 会将spring内部实例化的实例进行代理返回代理对象

+ 2、拓展 bean实例实例化通知处理器 InstantiationAwareBeanPostProcessor
  + 增加 获取提前需要被暴露的bean 供其他bean引用 getEarlyBeanReference

+ 3、补充 bean创建实例流程 AbstractAutowireCapableBeanFactory
  + 增加 bean循环依赖判断 提前将bean的创建行为存放到bean容器三级缓存中
  + 增加 注册单例前的自身完整性判断 提前通过 getSingleton 判断
  + getSingleton 从一级到三级缓存逐级寻找 直到创建完成
  
___
### 属性类型转换服务
+ 1、转换器 Converter
  + 框架内置 [字符串-> 数字] 转换器 StringToNumberConverter
    + 可通过 StringToNumberConverterFactory工厂创建
  + 程序外置 [字符串-> 日期] 转换器 StringToLocalDateConverter
    + 通过配置 转换服务工厂Bean注入 ConversionServiceFactoryBean

+ 2、转转器生成工厂 ConverterFactory
  + 框架内置 [字符串-> 数字] 转换器工厂 StringToNumberConverterFactory

+ 3、通用转化器配置管理 GenericConverter
  + 转换类型配置项 ConvertiblePair
    + sourceType <-> targetType
  + 定义获取转换器可支持转换类型集 getConvertiblePairTypes
  + 定义转换通用行为 convert

+ 4、转换器注册器 ConverterRegistry
  + 定义注册行为
  + 注册 具体转换器、通用转换器适配、转换器生成工厂

+ 5、转换器服务中心 ConversionService
  + 顶层设计 转换行为定义 canConvert、convert
  + 通用服务实现 GenericConversionService
    + 转换类型配置与转换器配置 Map<GenericConverter.ConvertiblePair,GenericConverter> converters
    + 实现 转化器注册器行为
    + 实现 转换服务 具体转换行为
    + ***定义转换器适配器*** ConverterAdapter
    + ***定义转换器生产工厂适配器*** ConverterFactoryAdapter
---
## 较上一个版本3.4 改动 增加属性类型转换器支持
+ 1、转换器 Converter
  + 框架内置 [字符串-> 数字] 转换器 StringToNumberConverter
    + 可通过 StringToNumberConverterFactory工厂创建
  + 程序外置 [字符串-> 日期] 转换器 StringToLocalDateConverter
    + 通过配置 转换服务工厂Bean注入 ConversionServiceFactoryBean

+ 2、转转器生成工厂 ConverterFactory
  + 框架内置 [字符串-> 数字] 转换器工厂 StringToNumberConverterFactory

+ 3、通用转化器配置管理 GenericConverter
  + 转换类型配置项 ConvertiblePair
    + sourceType <-> targetType
  + 定义获取转换器可支持转换类型集 getConvertiblePairTypes
  + 定义转换通用行为 convert
  
+ 4、转换器注册器 ConverterRegistry
  + 定义注册行为
  + 注册 具体转换器、通用转换器适配、转换器生成工厂

+ 5、转换器服务中心 ConversionService
  + 顶层设计 转换行为定义 canConvert、convert
  + 通用服务实现 GenericConversionService
    + 转换类型配置与转换器配置 Map<GenericConverter.ConvertiblePair,GenericConverter> converters
    + 实现 转化器注册器行为
    + 实现 转换服务 具体转换行为
    + ***定义转换器适配器*** ConverterAdapter
    + ***定义转换器生产工厂适配器*** ConverterFactoryAdapter

+ 6、增加 转换器服务注册和属性处理逻辑
  + 1、在加载bean配置完毕后，先判断是否需要注册转换器，再提前初始化单例
    + AbstractApplicationContext -> refresh -> finishedBeanFactoryInitialization
  + 2、bean容器处理化bean实例之前 处理属性配置
    + applyBeanPostProcessorBeforeApplyingBeanPropertyValues
      + AutowiredAnnotationBeanPostProcessor -> postProcessPropertyValues
    + applyBeanPropertyValues