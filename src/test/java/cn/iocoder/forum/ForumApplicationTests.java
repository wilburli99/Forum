package cn.iocoder.forum;

import cn.iocoder.forum.dao.UserMapper;
import cn.iocoder.forum.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class ForumApplicationTests {

    @Resource
    private DataSource dataSource;
    @Autowired
    private UserMapper userMapper;

    @Test
    void testConnection() throws SQLException {
        System.out.println("datasource = " + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("connection = " + connection);
        connection.close();
    }

    @Test
    public void testMybatis() {
        User user = userMapper.selectByPrimaryKey(1l);
        System.out.println(user.toString());
        System.out.println(user.getUsername());
    }

    @Test
    void contextLoads() {
    }

}
