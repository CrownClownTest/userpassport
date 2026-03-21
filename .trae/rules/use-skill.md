**项目应用的一些规范**：

使用lombox简化BO DTO 等类型的声明

使用springdoc-openapi-starter-webmvc-ui 生成api文档，且要包含 @Operation ，@Parameter，@Tag，@Schema等标签来标识

数据库操作我们使用jpa 来操作数据库 我们的链接是 postgresql 

我希望配置数据库链接这部分是一个通过spring-db.yml 进行配置配置数据库链接然后在启动的时候自行加载

