# 基础版本spring容器 - version 2.0

## 核心模型

### BeanFactory工厂
+ 1、实例的元数据定义 BeanDefinition
    + 类名
    + 类class
    + 原始bean元数据定义缓存 hashMap
    + 属性值 （基础类型 + 引用类型）
+ 2、实例属性元数据定义 PropertyValue
    + 属性名称
    + 属性值(对象引用/基础类型)

+ 3、单例存储缓存 DefaultSingletonBeanRegistry
  + Map<String,Object> singletonBeansMap

+ 4、BeanFactory创建工厂
    + 顶层定义获取bean行为
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
        + todo 自定义初始化 invokeBeanInitMethods 
        + 后置 applyBeanPostProcessorAfterInitialization
    + 末端实现类实现 实例元数据动作 并继承以上的工厂行为
+ 5、数据源读取配置策略 core.io包
+ 6、bean元数据xml配置读取方式 XmlBeanDefinitionReader

### 容器上下文Application
+ 1、增加 系统上下文容器 ApplicationContext
  + 抽象上下文定义 AbstractApplicationContext
    + refresh 容器初始化模版行为定义
    + invokeBeanFactoryPostProcessors 容器加载配置完后增强方法
    + registryBeanPostProcessors 注册bean实例级别增强
  + 初始化抽象上下文 AbstractRefreshableApplicationContext
    + refreshBeanFactory  定义容器加载配置初始化
    + loadBeanDefinitions 定义加载配置抽象
  + xml加载抽象上下文 AbstractXmlApplicationContext
    + loadBeanDefinitions 实现加载配置方式xmlReader
  + 基于项目路径xml配置的上下文 ClasspathApplicationContext
    + 定义 配置文件位置
    + 初始化动作在实例化之后

## 较上一个版本1.3 新增
+ 1、增加 系统上下文容器 ApplicationContext
  
+ 2、增加 beanFactory增强配置 BeanFactoryPostProcessor

+ 3、增加 bean实例增强配置 BeanPostProcessor

+ 4、增加 beanFactory工厂注册 BeanPostProcessor
  + 属性 List<BeanPostProcessor> beanPostProcessors
  
+ 5、增加 底层抽象层实现bean的创建流程 AbstractAutowireCapableBeanFactory
  + 拓展bean实例化增强行为
    + 前置
    + 自定义初始化
    + 后置