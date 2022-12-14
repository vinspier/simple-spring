# 基础版本spring容器 - version 2.2

## 核心模型

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
  
+ 5、数据源读取配置策略 core.io包

+ 6、bean元数据xml配置读取器 XmlBeanDefinitionReader
  + 定义标签 XmlTagEnum
  + 增加解析 自定义初始化、销毁方法解析

### bean生命周期
+ 1、实例初始化定义接口 InitializingBean

+ 2、实例销毁定义接口 DisposableBean
  + 销毁接口适配累DisposableBeanAdapter
    + 兼容 未实现DisposableBean接口但定义了init-method的类
    
+ 3、BeanFactory工厂通知 BeanFactoryAware

+ 4、bean加载器通知 BeanClassLoaderAware

+ 5、bean名称通知 BeanNameAware


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


## 较上一个版本2.1 新增 通知机制
+ 1、增加 BeanFactory工厂通知 BeanFactoryAware

+ 2、增加 bean加载器通知 BeanClassLoaderAware
  
+ 2、增加 bean名称通知 BeanNameAware

+ 3、增加 bean上下文容器通知 ApplicationContextAware
  + 有ApplicationContextAware处理器完成通知动作
    + ApplicationContextAwareProcessor