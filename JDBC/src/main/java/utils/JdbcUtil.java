package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;

/**
 * @author zhangyang
 * @date 2021/12/5 22:48
 */
public class JdbcUtil {
    // 封装 JDBC 工具类
    private static final Properties PROP = new Properties();

    static {
        initProp();
    }

    private static final String DRIVER = PROP.getProperty("driver");
    private static final String URL = PROP.getProperty("url");
    private static final String USER = PROP.getProperty("user");
    private static final String PASSWORD = PROP.getProperty("password");

    private static void initProp() {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            assert in != null;
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            PROP.load(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private JdbcUtil() {
    }

    /**
     * @return 获取 sql连接
     * @throws SQLException sql异常
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 关闭资源
     *
     * @param conn sql连接
     * @param ps   操作数据库对象
     * @param rs   返回结果集
     */
    public static void close(Connection conn, Statement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


