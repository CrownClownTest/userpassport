**项目的目录结构如下**

bo ,business , config , dto , service , resource , third , utils ,cache ,converter,controller,constant

注意如果我们操作一类给你按 最好比如在 bo dto business third converter cache resource 类似的几个文件夹下在用 文件夹进行归类而不要直接统一写到包下面
service：如果定义一个接口 那么需要定义在 service 中 即spring的接口定义需要定义在这里,这里最好至做转换和简单的business调用，尽量在Business层进行概念统一

dto: 依照对应的模块概念等 在dto中定义 service接口 需要的入参和出参定义，这里入参已Request结尾 出参用 DTO结尾

business: 实际的业务代码，具体业务应当都从这里进行处理而不应该在service中

bo: business层能处理的类型 已BO结尾，如果没有就要定义一个，比如service到business的参数就需要通过converter进行转换

converter: 用于转换其他层到business的参数，比如service到business的参数就需要通过converter进行转换

config: 用于spring的启动配置项

resource:关于数据库的操作在这里

cache: 操作缓存的 在这里定义 比如操作redis 的 cache

utils: 工具类的几何

third:调用其他服务或者第三方的 接口定义

controller: 控制器层的定义

constant：常量定义应当放于此处，包括枚举 ，枚举应当放到 constant文件夹中 且应当已 Enum结尾，另外枚举如无特别说明需要两个属性，id 和 desc，id 为枚举的唯一标识，desc 为枚举的描述以及一个通过id转换到枚举的方法
