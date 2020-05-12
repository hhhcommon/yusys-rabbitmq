package cn.com.yusys.rabbitmq.publishSubscribe;


import cn.com.yusys.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 提供者
 * 发布与订阅模式使用的交换机类型为： fanout---广播
 * @author huyang
 * @date 2020/5/11 15:51
 */
public class Producer {

    // 交换机名称
    static final String FANOUR_EXCHANGE = "fanout_exchange";

    // 队列名称
    static final String FANOUR_QUEUE_1 = "fanout_queue_1";

    // 队列名称
    static final String FANOUR_QUEUE_2 = "fanout_queue_2";

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
        channel.exchangeDeclare(FANOUR_EXCHANGE, BuiltinExchangeType.FANOUT);

        // 声明(创建)队列
        /*
        参数一：队列名称
        参数二：是否定义持久化队列
        参数三：是否独占本次连接
        参数四：是否在不使用的时候自动删除队列
        参数五：队列其他参数
         */
        channel.queueDeclare(FANOUR_QUEUE_1, true, false, false, null);
        channel.queueDeclare(FANOUR_QUEUE_2, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(FANOUR_QUEUE_1,FANOUR_EXCHANGE,"");
        channel.queueBind(FANOUR_QUEUE_2,FANOUR_EXCHANGE,"");

        // 要发送的消息
        for (int i = 0; i < 30; i++) {
            String messsage = "哈哈，欢迎使用rabbitMq--publishSubscribe-----" + i;
            /**
             * 参数一：交换机名称，如果没有指定则使用默认Default Exchange
             * 参数二：路由key，简单模式可以传递队列名称
             * 参数三：消息其他属性
             * 参数四：消息内容
             */
            channel.basicPublish(FANOUR_EXCHANGE,"", null, messsage.getBytes());
            System.out.println("已经发送消息： " + messsage);
        }
        // 释放资源
        channel.close();
        connection.close();
    }
}
