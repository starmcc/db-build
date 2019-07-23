# 自动构建SQL工具包

## 前言

使用纯JDBC方式进行构建，不依赖第三方，适用于项目启动时自动构建等工作。

工具贴，后续会上传至Maven。

## 说明

- `IbatisRunner` mybatis提供的类，执行SQL脚本的方法需要调用该类。
- `RuntimeSqlException`  异常类。
- `QmDbBuildConfig` 工具配置对象
- `QmDbBuildClient` 工具客户端
- `Main` 示例



## 使用

> 从`github`上下载源码

```shell
git clone https://github.com/starmcc/db-build.git
```

> 复制进项目中，在需要调用的地方创建`QmDbBuildConfig`对象设置基础配置。

```java
QmDbBuildConfig config = new QmDbBuildConfig();
config.setUrl("jdbc:mysql://localhost:3306/test_db");
config.setUsername("root");
config.setPassword("123");
```

> 创建 `QmDbBuildClient`对象，调用提供的方法构建数据库环境。

```java
QmDbBuildClient build = new QmDbBuildClient(config);
// 构建数据库
build.buildDataBase();
// 执行SQL脚本
build.buildSqlFile(true,"build/test_db.sql");
```



## API

### buildDataBase

构建数据库，自动从`url`连接当中获取数据库名和数据库连接地址，并进行创建操作，当数据库存在时，不执行创建操作。

### buildSqlFile

执行SQL脚本。

> 两个参数

- `transaction`  是否开启事务。
- `sqlFilePath`  SQL脚本路径数组。路径解析为项目中的`resource`下的路径。

### buildSql

执行SQL语句集。

> 一个参数

- sql  执行的SQL语句数组，每个下标为一个SQL语句。



## Author

name: starmcc - qm

email: starczt1992@163.com