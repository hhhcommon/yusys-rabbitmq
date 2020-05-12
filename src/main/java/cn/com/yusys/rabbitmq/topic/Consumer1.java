package cn.com.yusys.rabbitmq.topic;

import cn.com.yusys.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 *
 * @author huyang
 * @date 2020/5/11 21:44
 */
public class Consumer1 {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();

        // 创建频道
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(Producer.TOPIC_EXCHANGE,BuiltinExchangeType.TOPIC);

        // 声明(创建)队列
        /*
        参数一：队列名称
        参数二：是否定义持久化队列
        参数三：是否独占本次连接
        参数四：是否在不使用的时候自动删除队列
        参数五：队列其他参数
         */
        // 创建消费者，并设置消费处理
        channel.queueDeclare(Producer.TOPIC_QUEUE_1, true, false, false, null);

        // 队列绑定交换机
        channel.queueBind(Producer.TOPIC_QUEUE_1, Producer.TOPIC_EXCHANGE,"item.#");

        // 监听消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
             *
             * @param consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * @param envelope 消息包内容，可从中获取消息id，消息routingkey,交换机，消息和重转标记（收到消息失败后是否需要重新发送）
             * @param properties 消息属性
             * @param body 消息
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("=============消费者1开始=======================");
                // 路由key
                System.out.println("路由key: " + envelope.getRoutingKey());
                // 交换机
                System.out.println("交换机： " + envelope.getExchange());
                // 消息id
                System.out.println("消息id: " + envelope.getDeliveryTag());
                // 收到的消息
                System.out.println("接收到的消息： " + new String(body, "utf-8"));
                System.out.println("=============消费者1结束=======================");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        /**
         * 监听消息
         * 参数一：队列名称
         * 参数二：是否自动确认，设置为true表示消息接收到后自动向mq回复收到了，mq接收到后回复后会删除消息；设置为false则需要手动确认
         */
        channel.basicConsume(Producer.TOPIC_QUEUE_1, true, consumer);

        // 不关闭资源，应该一直监听消息
//        channel.close();
//        connection.close();
    }
}
