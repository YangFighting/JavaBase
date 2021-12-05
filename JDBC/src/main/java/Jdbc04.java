import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @author zhangyang
 * @date 2021/12/5 22:11
 */
@lombok.extern.slf4j.Slf4j
public class Jdbc04 {

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

    public static void main(String[] args) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        try {

            //  1. 注册驱动
            Class.forName(DRIVER);

            // 2. 获取连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 3. 获取数据库操作对象
            // Statement 无法防止sql注入
            String sql = "select * from  students where name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,"张三");

            // 4. 执行sql语句 - 查询
            resultSet = pstmt.executeQuery();
            // 5. 处理返回数据集
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                double height = resultSet.getDouble("height");
                log.warn(MessageFormat.format("name: {0}, age: {1}, height: {2}", name, String.valueOf(age), String.valueOf(height)));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();

        } finally {
            // 6. 释放资源
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
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
}
