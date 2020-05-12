package cn.com.yusys.rabbitmq.routing;


import cn.com.yusys.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 提供者
 * 路由模式使用的交换机类型为： direct
 *
 * @author huyang
 * @date 2020/5/11 15:51
 */
public class Producer {

    // 交换机名称
    static final String DIRECT_EXCHANGE = "direct_exchange";

    // 队列名称
    static final String DIRECT_QUEUE_INSERT = "direct_queue_insert";

    // 队列名称
    static final String DIRECT_QUEUE_UPDATE = "direct_queue_update";

    public static void main(String[] args) throws Exception {

        // 获取连接
        Connection connection = ConnectionUtil.getConnection();

        // 创建频道
        Channel channel = connection.createChannel();

        /**
         * 声明交换机
         * 参数一：交换机名称
         * 参数二：交换机类型：fanout、topic、direct、headers
         */
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明(创建)队列
        /*
        参数一：队列名称
        参数二：是否定义持久化队列
        参数三：是否独占本次连接
        参数四：是否在不使用的时候自动删除队列
        参数五：队列其他参数
         */
        channel.queueDeclare(DIRECT_QUEUE_INSERT, true, false, false, null);
        channel.queueDeclare(DIRECT_QUEUE_UPDATE, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(DIRECT_QUEUE_INSERT, DIRECT_EXCHANGE, "insert");
        channel.queueBind(DIRECT_QUEUE_UPDATE, DIRECT_EXCHANGE, "update");

        // 要发送的消息
        String messsage = "新增了商品，路由模式；routing key 为 insert ";
        /**
         * 参数一：交换机名称，如果没有指定则使用默认Default Exchange
         * 参数二：路由key，简单模式可以传递队列名称
         * 参数三：消息其他属性
         * 参数四：消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE, "insert", null, messsage.getBytes());
        System.out.println("已经发送消息： " + messsage);

        // 要发送的消息
        messsage = "修改了商品，路由模式；routing key 为 update ";
        /**
         * 参数一：交换机名称，如果没有指定则使用默认Default Exchange
         * 参数二：路由key，简单模式可以传递队列名称
         * 参数三：消息其他属性
         * 参数四：消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE, "update", null, messsage.getBytes());
        System.out.println("已经发送消息： " + messsage);

        // 释放资源
        channel.close();
        connection.close();
    }
}
