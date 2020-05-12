package cn.com.yusys.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * mq创建连接工具类
 *
 * @author huyang
 * @date 2020/5/12 14:41
 */
public class ConnectionUtil {

    public static Connection getConnection() throws IOException, TimeoutException {

        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();

        // 主机地址
        connectionFactory.setHost("192.168.254.20");

        // 连接端口
        connectionFactory.setPort(5672);

        //虚拟机名称:默认为 /
        connectionFactory.setVirtualHost("/yusys");

        // 连接用户名
        connectionFactory.setUsername("heima");

        // 连接密码
        connectionFactory.setPassword("heima");

        // 创建连接
        Connection connection = connectionFactory.newConnection();

        return connection;
    }
}
