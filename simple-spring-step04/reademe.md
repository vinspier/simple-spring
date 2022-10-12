## 基础版本spring容器 - version 1.2

### 核心模型

+ 1、实例的元数据定义 BeanDefinition
    + 类名
    + 类class
    + 原始bean元数据定义缓存 hashMap
    + 属性值 （基础类型 + 引用类型）
+ 2、实例属性元数据定义 PropertyValue
    + 属性名称
    + 属性值(对象引用/基础类型)

+ 3、单例存储缓存 hashMap

+ 4、Bean创建工厂
    + 顶层定义获取bean行为
      + 增加 指定参数 获取实例
    + 中间抽象层定义获取bean抽象获取-创建实现模版行为 AbstractBeanFactory
    + 底层抽象层实现bean的创建流程 AbstractAutowireCapableBeanFactory
      + 增加 实例初始化策略 InstantiationStrategy
        + jdk反射机制实现
        + cglib动态字节码实现
      + 增加 实例属性填充
    + 末端实现类实现 实例元数据动作 并继承以上的工厂行为

## 较上一个版本1.1 新增
+ 1、底层抽象层实现bean的创建流程 AbstractAutowireCapableBeanFactory
  + 增加 实例属性填充 逻辑
    
+ 2、 实例元数据定义增加属性

+ 3、增加实例属性模型
  + 属性名称
  + 属性值(对象引用/基础类型)