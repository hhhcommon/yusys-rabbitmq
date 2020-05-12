package cn.com.yusys.rabbitmq.topic;


import cn.com.yusys.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 提供者
 * 通配符topic使用的交换机类型为： topic
 *
 * @author huyang
 * @date 2020/5/11 15:51
 */
public class Producer {

    // 交换机名称
    static final String TOPIC_EXCHANGE = "topic_exchange";

    // 队列名称
    static final String TOPIC_QUEUE_1 = "topic_queue_1";

    // 队列名称
    static final String TOPIC_QUEUE_2 = "topic_queue_2";

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
        channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);

        // 声明(创建)队列
        /*
        参数一：队列名称
        参数二：是否定义持久化队列
        参数三：是否独占本次连接
        参数四：是否在不使用的时候自动删除队列
        参数五：队列其他参数
         */
        channel.queueDeclare(TOPIC_QUEUE_1, true, false, false, null);
        channel.queueDeclare(TOPIC_QUEUE_2, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(TOPIC_QUEUE_1, TOPIC_EXCHANGE, "item.#");// #表示一个或多，例如 item.insert.abc
        channel.queueBind(TOPIC_QUEUE_2, TOPIC_EXCHANGE, "*.delete");// *表示一个，例如 item.delete

        // 发送消息
        String messgae = "新增了商品，Topic模式：routing key 为 item.insert ";
        channel.basicPublish(TOPIC_EXCHANGE, "item.insert", null, messgae.getBytes());
        System.out.println("已发送消息： " + messgae);

        // 发送消息
        messgae = "修改了商品，Topic模式：routing key 为 item.update ";
        channel.basicPublish(TOPIC_EXCHANGE, "item.update", null, messgae.getBytes());
        System.out.println("已发送消息： " + messgae);

        // 发送消息
        messgae = "删除了商品，Topic模式：routing key 为 product.delete ";
        channel.basicPublish(TOPIC_EXCHANGE, "product.delete", null, messgae.getBytes());
        System.out.println("已发送消息： " + messgae);

        // 发送消息
        messgae = "其他信息，Topic模式：routing key 为 item.delete.abc ";
        channel.basicPublish(TOPIC_EXCHANGE, "item.delete.abc", null, messgae.getBytes());
        System.out.println("已发送消息： " + messgae);

        // 释放资源
        channel.close();
        connection.close();
    }
}
