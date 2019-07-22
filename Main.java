package com.starmcc.build;

/**
 * test
 *
 * @Author: qm
 * @Date: 2019/7/22 16:43
 */
public class Main {

    public static void main(String[] args) throws Exception {
        QmDbBuildConfig config = new QmDbBuildConfig();
        config.setUrl("jdbc:mysql://localhost:3306/test_db");
        config.setUsername("root");
        config.setPassword("123");
        QmDbBuildClient build = new QmDbBuildClient(config);
        build.buildDataBase();
        build.buildSqlFile(true,"build/qm-blog.sql");
        System.out.println("OK");
    }
}
