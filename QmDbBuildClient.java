
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.sql.*;

/**
 * 自动构建工具
 *
 * @Author: qm
 * @Date: 2019/7/22 15:04
 */
public class QmDbBuildClient {

    private static final Logger LOG = LoggerFactory.getLogger(QmDbBuildClient.class);

    private Connection connection = null;
    private Statement stmt = null;
    private QmDbBuildConfig config;

    private QmDbBuildClient() {
    }

    public QmDbBuildClient(QmDbBuildConfig config) {
        this.config = config;
    }

    /**
     * 创建数据库
     */
    public void buildDataBase() throws Exception {
        String baseName = config.getUrl().substring(config.getUrl().lastIndexOf("/") + 1, config.getUrl().length());
        String baseUrl = config.getUrl().substring(0, config.getUrl().lastIndexOf("/") + 1);
        LOG.info("开始构建数据库{} ...", baseName);
        try {
            //STEP 2: 注册驱动
            Class.forName(config.getJdbcDriver());
            //STEP 3: 打开数据库连接
            connection = DriverManager.getConnection(baseUrl, config.getUsername(), config.getPassword());
            LOG.info("连接[{}]成功...", baseUrl);
            connection.setAutoCommit(false);
            //STEP 4: Execute a query
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(0) FROM information_schema.schemata WHERE schema_name = '" + baseName + "'");
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    LOG.info("数据库已存在,跳出创建数据库方法...");
                    return;
                }
            } else {
                throw new Exception();
            }

            String createDataBaseSql =
                    "CREATE DATABASE " + baseName
                            + " DEFAULT CHARACTER SET " + config.getDefaultCharacter()
                            + " COLLATE " + config.getCollate();
            stmt.executeUpdate(createDataBaseSql);
            connection.commit();
            LOG.info("构建数据库成功...");
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            LOG.error("构建数据库发生异常...");
            throw e;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOG.error("数据库连接关闭异常...");
                throw e;
            }
        }
    }


    /**
     * 执行SQL语句
     *
     * @param sql
     */
    public void buildSql(String... sql) throws Exception {
        try {
            LOG.info("开始执行buildSql ...");
            //STEP 2: 注册驱动
            Class.forName(config.getJdbcDriver());
            //STEP 3: 打开数据库连接
            connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            LOG.info("连接[{}]成功 ...", config.getUrl());
            connection.setAutoCommit(false);
            //STEP 4: Execute a query
            stmt = connection.createStatement();
            for (String sqlItem : sql) {
                stmt.execute(sqlItem);
            }
            connection.commit();
            LOG.info("buildSql执行成功 ...");
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            LOG.error("构建数据库发生异常 ...");
            throw e;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOG.error("数据库连接关闭异常 ...");
                throw e;
            }
        }
    }

    /**
     * buildSqlFile
     */
    public void buildSqlFile(boolean transaction, String... sqlFilePath) throws Exception {
        IbatisRunner runner = null;
        try {
            LOG.info("正在执行SQL脚本 ...");
            //STEP 2: 注册驱动
            Class.forName(config.getJdbcDriver());
            //STEP 3: 打开数据库连接
            connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            LOG.info("连接[{}]成功 ...", config.getUrl());
            // 设置不自动提交
            connection.setAutoCommit(!transaction);
            // 创建ScriptRunner
            runner = new IbatisRunner(connection);
            // 设置不自动提交
            runner.setAutoCommit(!transaction);
             /* 
             * setStopOnError参数作用：遇见错误是否停止；
             * （1）false，遇见错误不会停止，会继续执行，会打印异常信息，并不会抛出异常，当前方法无法捕捉异常无法进行回滚操作，无法保证在一个事务内执行；
             * （2）true，遇见错误会停止执行，打印并抛出异常，捕捉异常，并进行回滚，保证在一个事务内执行；
             */
            runner.setStopOnError(transaction);
            /*
             * 按照哪种方式执行
             * 方式一：true则获取整个脚本并执行；
             * 方式二：false则按照自定义的分隔符每行执行；
             */
            runner.setSendFullScript(!transaction);
            runner.setDelimiter(";");
            runner.setFullLineDelimiter(false);
            LOG.info("开始执行SQL脚本 ...");
            for (String path : sqlFilePath) {
                runner.runScript(new InputStreamReader(
                        QmDbBuildClient.class.getClassLoader().getResourceAsStream(path),
                        "UTF-8"));
            }
            if (transaction) {
                connection.commit();
            }
            LOG.info("SQL脚本执行成功 ...");
        } catch (SQLException e) {
            if (transaction && connection != null) {
                connection.rollback();
            }
            LOG.error("执行buildSqlFile发生异常 ...", e);
            throw e;
        } finally {
            try {
                if (runner != null) {
                    runner.closeConnection();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOG.error("数据库连接关闭异常 ...");
                throw e;
            }
        }
    }
}
