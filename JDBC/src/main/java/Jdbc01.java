

import java.sql.*;
import java.text.MessageFormat;


/**
 * @author zhangyang
 * @date 2021/12/4 20:36
 */
@lombok.extern.slf4j.Slf4j
public class Jdbc01 {

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            // 1. 注册驱动
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            // 2. 获取连接
            String url = "jdbc:mysql://***:3306/python";
            String user = "root";
            String password = "***";
            conn = DriverManager.getConnection(url, user, password);
            // 3. 获取数据库操作对象
            stmt = conn.createStatement();

            // 4. 执行sql语句 -增加
            String sql = "insert into students(name,age,height,gender,class_id) values ('test','16','180.1',1,1)";
            // 返回影响数据库中的行数
            int cout = stmt.executeUpdate(sql);
            log.warn((cout > 0) ? "新增成功" : "新增失败");

            // 4. 执行sql语句 - 更新
            sql = "update  students set age = 8 where name = 'test';";
            cout = stmt.executeUpdate(sql);
            log.warn((cout > 0) ? "更新成功" : "更新失败");

            // 4. 执行sql语句 - 查询
            sql = "select * from  students";
            resultSet = stmt.executeQuery(sql);
            // 5. 处理返回数据集
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                double height = resultSet.getDouble("height");
                log.warn(MessageFormat.format("name: {0}, age: {1}, height: {2}", name, String.valueOf(age), String.valueOf(height)));
            }

            // 4. 执行sql语句 - 删除
            sql = "delete from students where name = 'test';";
            cout = stmt.executeUpdate(sql);
            log.warn((cout > 0) ? "删除成功" : "删除失败");

        } catch (SQLException e) {
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
