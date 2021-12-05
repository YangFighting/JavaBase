import utils.JdbcUtil;

import java.sql.*;

/**
 * @author zhangyang
 * @date 2021/12/5 23:02
 */
@lombok.extern.slf4j.Slf4j
public class Jdbc05 {
    public static void main(String[] args) {
        // 使用 封装类 进行模糊查询
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            String sql = "select * from  students where name like ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "张%");
            log.info("sql: {}", ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                log.warn(rs.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JdbcUtil.close(conn, ps, rs);
        }
    }
}
