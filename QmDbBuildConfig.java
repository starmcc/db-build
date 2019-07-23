
/**
 * 自动构建配置
 *
 * @Author: qm
 * @Date: 2019/7/22 16:38
 */
public class QmDbBuildConfig {

    // JDBC driver name and database URL
    private String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/";
    // 字符集
    private String defaultCharacter = "utf8mb4";
    private String collate = "utf8mb4_general_ci";
    //  Database credentials
    private String username = "root";
    private String password = "123456";

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDefaultCharacter() {
        return defaultCharacter;
    }

    public void setDefaultCharacter(String defaultCharacter) {
        this.defaultCharacter = defaultCharacter;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
