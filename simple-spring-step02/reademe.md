## 基础版本spring容器 - version 1.1

### 核心模型

+ 1、实例的元数据定义 beanDefinition
    + 类明
    + 类class
    + 原始bean元数据定义缓存 hashMap

+ 2、单例存储缓存 hashMap

+ 3、bean创建工厂
    + 顶层定义获取bean行为
    + 中间抽象层定义获取bean抽象获取-创建实现模版行为 AbstractBeanFactory
    + 底层抽象层实现bean的创建流程 AbstractAutowireCapableBeanFactory
    + 末端实现类实现 实例元数据动作 并继承以上的工厂行为
  
## 较上一个版本 新增
+ 1、增加实例创建策略 InstantiationStrategy
  + jdk反射机制实现
  + cglib动态字节码实现
  
+ 2、 底层抽象层实现bean的创建流程 AbstractAutowireCapableBeanFactory
  + 增加 实例创建策略 cglib

+ 3、bean创建工厂拓展能力
  + 增加 指定参数 获取实例