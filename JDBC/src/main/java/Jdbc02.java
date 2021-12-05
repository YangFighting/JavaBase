/*
 * 注册驱动的第二种方式 -反射
 *
 * */

import java.sql.*;
import java.text.MessageFormat;

/**
 * @author zhangyang
 * @date 2021/12/5 20:59
 */

@lombok.extern.slf4j.Slf4j
public class Jdbc02 {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {

            //  1. 注册驱动的第二种方式 -反射
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 获取连接
            String url = "jdbc:mysql://***:3306/python";
            String user = "root";
            String password = "***";
            conn = DriverManager.getConnection(url, user, password);
            // 3. 获取数据库操作对象
            stmt = conn.createStatement();

            // 4. 执行sql语句 - 查询
            String sql = "select * from  students";
            resultSet = stmt.executeQuery(sql);
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
            if (stmt != null) {
                try {
                    stmt.close();
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
