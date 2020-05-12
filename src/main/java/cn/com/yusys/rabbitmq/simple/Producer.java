package cn.com.yusys.rabbitmq.simple;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author huyang
 * @date 2020/5/11 15:51
 */
public class Producer {

    static final String QUEUE_NAME = "simple_queue";

    public static void main(String[] args) throws Exception {

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

        // 创建频道
        Channel channel = connection.createChannel();

        // 声明(创建)队列
        /*
        参数一：队列名称
        参数二：是否定义持久化队列
        参数三：是否独占本次连接
        参数四：是否在不使用的时候自动删除队列
        参数五：队列其他参数
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 要发送的消息
        String messsage = "贝吉塔大战孙悟空";
        channel.basicPublish("", QUEUE_NAME, null, messsage.getBytes());
        System.out.println("已经发送消息： " + messsage);

        // 释放资源
        channel.close();
        connection.close();
    }
}
