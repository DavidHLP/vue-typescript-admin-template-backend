package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.blog.Repository.ArticleRepository;
import com.david.hlp.SpringBootWork.blog.entity.ArticleTable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleRunner {
    private final ArticleRepository articleRepository;

    @PostConstruct
    public void init() {
        List<ArticleTable> articleTables = new ArrayList<>();
        ArticleTable articleTable = ArticleTable
                .builder()
                .disableComment(Boolean.TRUE)
                .pageviews(0)
                .status(Boolean.TRUE)
                .version(0L)
                .imageURL(null)
                .abstractContent("RabbitMQ延迟消息")
                .author("DavidHLP")
                .MarkdownContent("# 延迟消息\n" +
                        "\n" +
                        "在电商的支付业务中，对于一些库存有限的商品，为了更好的用户体验，通常都会在用户下单时立刻扣减商品库存。例如电影院购票、高铁购票，下单后就会锁定座位资源，其他人无法重复购买。\n" +
                        "\n" +
                        "但是这样就存在一个问题，假如用户下单后一直不付款，就会一直占有库存资源，导致其他客户无法正常交易，最终导致商户利益受损！\n" +
                        "\n" +
                        "因此，电商中通常的做法就是：**对于超过一定时间未支付的订单，应该立刻取消订单并释放占用的库存**。\n" +
                        "\n" +
                        "像这种在一段时间以后才执行的任务，我们称之为**延迟任务**，而要实现延迟任务，最简单的方案就是利用MQ的延迟消息了。\n" +
                        "\n" +
                        "在RabbitMQ中实现延迟消息也有两种方案：\n" +
                        "\n" +
                        "-   死信交换机+TTL\n" +
                        "-   延迟消息插件\n" +
                        "\n" +
                        "## 死信交换机和延迟消息\n" +
                        "\n" +
                        "### 死信交换机\n" +
                        "\n" +
                        "当一个队列中的消息满足下列情况之一时，可以成为死信（dead letter）：\n" +
                        "\n" +
                        "-   消费者使用`basic.reject`或 `basic.nack`声明消费失败，并且消息的`requeue`参数设置为false\n" +
                        "-   消息是一个过期消息，超时无人消费\n" +
                        "-   要投递的队列消息满了，无法投递\n" +
                        "\n" +
                        "如果一个队列中的消息已经成为死信，并且这个队列通过*dead-letter-exchange*属性指定了一个交换机，那么队列中的死信就会投递到这个交换机中，而这个交换机就称为**死信交换机**（Dead Letter Exchange）。而此时加入有队列与死信交换机绑定，则最终死信就会被投递到这个队列中。\n" +
                        "\n" +
                        "死信交换机作用\n" +
                        "1.  收集那些因处理失败而被拒绝的消息\n" +
                        "2.  收集那些因队列满了而被拒绝的消息\n" +
                        "3.  收集因TTL（有效期）到期的消息\n" +
                        "\n" +
                        "### 延迟消息\n" +
                        "\n" +
                        "前面两种作用场景可以看做是把死信交换机当做一种消息处理的最终兜底方案，与消费者重试时讲的`RepublishMessageRecoverer`作用类似。\n" +
                        "\n" +
                        "而最后一种场景，大家设想一下这样的场景：\n" +
                        "\n" +
                        "如图，有一组绑定的交换机（`ttl.fanout`）和队列（`ttl.queue`）。但是`ttl.queue`没有消费者监听，而是设定了死信交换机`hmall.direct`，而队列`direct.queue1`则与死信交换机绑定，RoutingKey是blue：\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20delayed%20messages/mid1.png)\n" +
                        "假如我们现在发送一条消息到`ttl.fanout`，RoutingKey为blue，并设置消息的**有效期**为5000毫秒：\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20delayed%20messages/mid2.png)\n" +
                        "> **注意**：尽管这里的`ttl.fanout`不需要RoutingKey，但是当消息变为死信并投递到死信交换机时，会沿用之前的RoutingKey，这样`hmall.direct`才能正确路由消息。\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20delayed%20messages/mid3.png)\n" +
                        "死信被再次投递到死信交换机`hmall.direct`，并沿用之前的RoutingKey，也就是`blue`：\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20delayed%20messages/mid5.png)\n" +
                        "由于`direct.queue1`与`hmall.direct`绑定的key是blue，因此最终消息被成功路由到`direct.queue1`，如果此时有消费者与`direct.queue1`绑定， 也就能成功消费消息了。但此时已经是5秒钟以后了：\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20delayed%20messages/mid5.png)\n" +
                        "也就是说，publisher发送了一条消息，但最终consumer在5秒后才收到消息。我们成功实现了**延迟消息**。\n" +
                        "\n" +
                        "> **注意：**\n" +
                        ">RabbitMQ的消息过期是基于追溯方式来实现的，也就是说当一个消息的TTL到期以后不一定会被移除或投递到死信交换机，而是在消息恰好处于队首时才会被处理。\n" +
                        ">当队列中消息堆积很多的时候，过期消息可能不会被按时处理，因此你设置的TTL时间不一定准确。\n" +
                        "\n" +
                        "## DelayExchange插件\n" +
                        "\n" +
                        "基于死信队列虽然可以实现延迟消息，但是太麻烦了。因此RabbitMQ社区提供了一个延迟消息插件来实现相同的效果。\n" +
                        "\n" +
                        "官方文档说明：**[Scheduling Messages with RabbitMQ](https://www.rabbitmq.com/blog/2015/04/16/scheduling-messages-with-rabbitmq)**\n" +
                        "\n" +
                        "### 下载\n" +
                        "\n" +
                        "插件下载地址：**[rabbitmq-delayed-message-exchange](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange)**\n" +
                        "\n" +
                        "由于我们安装的MQ是`4.0`版本，因此这里下载`4.0.2`版本：\n" +
                        "\n" +
                        "![enter image description here](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20delayed%20messages/2024-11-2514-58-59.png)\n" +
                        "### 安装\n" +
                        "\n" +
                        "- 我们是基于docker-compose的RabbitMQ，只需要修改一下配置文件\n" +
                        "\n" +
                        "```yml\n" +
                        "services:\n" +
                        "  rabbitmq:\n" +
                        "    image: rabbitmq:4.0-management\n" +
                        "    container_name: rabbitmq\n" +
                        "    ports:\n" +
                        "      - \"5672:5672\"\n" +
                        "      - \"15672:15672\"\n" +
                        "    environment:\n" +
                        "      RABBITMQ_DEFAULT_USER: admin\n" +
                        "      RABBITMQ_DEFAULT_PASS: admin\n" +
                        "      RABBITMQ_MNESIA_DIR: /var/lib/rabbitmq/mnesia\n" +
                        "      RABBITMQ_LOG_BASE: /var/log/rabbitmq\n" +
                        "    volumes:\n" +
                        "      - /opt/docker-data/RabbitMQ/mnesia:/var/lib/rabbitmq/mnesia\n" +
                        "      - /opt/docker-data/RabbitMQ/logs:/var/log/rabbitmq\n" +
                        "      - /opt/docker-data/RabbitMQ/rabbitmq_delayed_message_exchange-4.0.2.ez:/opt/rabbitmq/plugins/rabbitmq_delayed_message_exchange-4.0.2.ez\n" +
                        "    command: >\n" +
                        "      bash -c \"rabbitmq-plugins enable rabbitmq_delayed_message_exchange &&\n" +
                        "               rabbitmq-server\"\n" +
                        "    networks:\n" +
                        "      - rabbitmq_network\n" +
                        "\n" +
                        "networks:\n" +
                        "  rabbitmq_network:\n" +
                        "    driver: bridge\n" +
                        "```\n" +
                        "\n" +
                        "### 声明延迟交换机\n" +
                        "\n" +
                        "基于注解方式：\n" +
                        "\n" +
                        "```java\n" +
                        "import lombok.extern.slf4j.Slf4j;\n" +
                        "import org.springframework.amqp.core.Message;\n" +
                        "import org.springframework.amqp.core.MessageProperties;\n" +
                        "import org.springframework.amqp.rabbit.annotation.Exchange;\n" +
                        "import org.springframework.amqp.rabbit.annotation.Queue;\n" +
                        "import org.springframework.amqp.rabbit.annotation.QueueBinding;\n" +
                        "import org.springframework.amqp.rabbit.annotation.RabbitListener;\n" +
                        "import org.springframework.stereotype.Component;\n" +
                        "\n" +
                        "@Component\n" +
                        "@Slf4j\n" +
                        "public class SpringRabbitListener {\n" +
                        "\n" +
                        "    @RabbitListener(bindings = @QueueBinding(\n" +
                        "            value = @Queue(name = \"delay.queue\", durable = \"true\"),\n" +
                        "            exchange = @Exchange(name = \"delay.direct\", delayed = \"true\"),\n" +
                        "            key = \"delay\"\n" +
                        "    ))\n" +
                        "    public void receiveDelayMessage(String msg) {\n" +
                        "        log.info(\"接收到delay.queue的延迟消息：{}\", msg);\n" +
                        "    }\n" +
                        "\n" +
                        "}\n" +
                        "```\n" +
                        "\n" +
                        "基于`@Bean`的方式：\n" +
                        "\n" +
                        "```java\n" +
                        "import lombok.extern.slf4j.Slf4j;\n" +
                        "import org.springframework.amqp.core.*;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "\n" +
                        "@Slf4j\n" +
                        "@Configuration\n" +
                        "public class DelayExchangeConfig {\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    public DirectExchange delayExchange(){\n" +
                        "        return ExchangeBuilder\n" +
                        "                .directExchange(\"delay.direct\") // 指定交换机类型和名称\n" +
                        "                .delayed() // 设置delay的属性为true\n" +
                        "                .durable(true) // 持久化\n" +
                        "                .build();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    public Queue delayedQueue(){\n" +
                        "        return new Queue(\"delay.queue\");\n" +
                        "    }\n" +
                        "    \n" +
                        "    @Bean\n" +
                        "    public Binding delayQueueBinding(){\n" +
                        "        return BindingBuilder.bind(delayedQueue()).to(delayExchange()).with(\"delay\");\n" +
                        "    }\n" +
                        "}\n" +
                        "```\n" +
                        "\n" +
                        "### 发送延迟消息\n" +
                        "\n" +
                        "发送消息时，必须通过x-delay属性设定延迟时间：\n" +
                        "\n" +
                        "```java\n" +
                        "@Test\n" +
                        "void testPublisherDelayMessage() {\n" +
                        "    // 1.创建消息\n" +
                        "    String message = \"hello, delayed message\";\n" +
                        "    // 2.发送消息，利用消息后置处理器添加消息头\n" +
                        "    rabbitTemplate.convertAndSend(\"delay.direct\", \"delay\", message, new MessagePostProcessor() {\n" +
                        "        @Override\n" +
                        "        public Message postProcessMessage(Message message) throws AmqpException {\n" +
                        "            // 添加延迟消息属性\n" +
                        "            message.getMessageProperties().setDelay(5000);\n" +
                        "            return message;\n" +
                        "        }\n" +
                        "    });\n" +
                        "}\n" +
                        "```\n" +
                        "\n" +
                        "> **注意：**\n" +
                        ">\n" +
                        "> 延迟消息插件内部会维护一个本地数据库表，同时使用Elang Timers功能实现计时。如果消息的延迟时间设置较长，可能会导致堆积的延迟消息非常多，会带来较大的CPU开销，同时延迟消息的时间会存在误差。\n" +
                        ">\n" +
                        "> 因此，**不建议设置延迟时间过长的延迟消息**。://www.rabbitmq.com/blog/2015/04/16/scheduling-messages-with-rabbitmq)**\n\n### 下载\n\n插件下载地址：**[rabbitmq-delayed-message-exchange](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange)**\n\n由于我们安装的MQ是`4.0`版本，因此这里下载`4.0.2`版本：\n\n### 安装\n\n- 我们是基于docker-compose的RabbitMQ，只需要修改一下配置文件\n\n### 声明延迟交换机\n\n### 发送延迟消息\n\n发送消息时，必须通过x-delay属性设定延迟时间：")
                .title("RabbitMQ延迟消息")
                .uniqueIdentifier("rabbitmqyan-chi-xiao-xi")
                .build();
        articleTables.add(articleTable);
        articleTable = ArticleTable
                .builder()
                .disableComment(Boolean.TRUE)
                .pageviews(0)
                .status(Boolean.FALSE)
                .version(0L)
                .imageURL(null)
                .abstractContent("RabbitMQ消费者的可靠性")
                .author("DavidHLP")
                .MarkdownContent("# 消费者的可靠性\n" +
                        "\n" +
                        "当RabbitMQ向消费者投递消息以后，需要知道消费者的处理状态如何。因为消息投递给消费者并不代表就一定被正确消费了，可能出现的故障有很多，比如：\n" +
                        "\n" +
                        "-   消息投递的过程中出现了网络故障\n" +
                        "    \n" +
                        "-   消费者接收到消息后突然宕机\n" +
                        "    \n" +
                        "-   消费者接收到消息后，因处理不当导致异常\n" +
                        "\n" +
                        "一旦发生上述情况，消息也会丢失。因此，RabbitMQ必须知道消费者的处理状态，一旦消息处理失败才能重新投递消息。\n" +
                        "\n" +
                        "RabbitMQ如何得知消费者的处理状态则非常的重要\n" +
                        "\n" +
                        "## 消费者确认机制\n" +
                        "\n" +
                        "为了确认消费者是否成功处理消息，RabbitMQ提供了消费者确认机制（**Consumer Acknowledgement**）。即：当消费者处理消息结束后，应该向RabbitMQ发送一个回执，告知RabbitMQ自己消息处理状态。回执有三种可选值：\n" +
                        "\n" +
                        "-   ack：成功处理消息，RabbitMQ从队列中删除该消息\n" +
                        "    \n" +
                        "-   nack：消息处理失败，RabbitMQ需要再次投递消息\n" +
                        "    \n" +
                        "-   reject：消息处理失败并拒绝该消息，RabbitMQ从队列中删除该消息\n" +
                        "    \n" +
                        "一般reject方式用的较少，除非是消息格式有问题，那就是开发问题了。因此大多数情况下我们需要将消息处理的代码通过`try catch`机制捕获，消息处理成功时返回ack，处理失败时返回nack.\n" +
                        "\n" +
                        "由于消息回执的处理代码比较统一，因此SpringAMQP帮我们实现了消息确认。并允许我们通过配置文件设置ACK处理方式，有三种模式：\n" +
                        "\n" +
                        "-   **`none`**：不处理。即消息投递给消费者后立刻ack，消息会立刻从MQ删除。非常不安全，不建议使用\n" +
                        "    \n" +
                        "-   **`manual`**：手动模式。需要自己在业务代码中调用api，发送`ack`或`reject`，存在业务入侵，但更灵活\n" +
                        "    \n" +
                        "-   **`auto`**：自动模式。SpringAMQP利用AOP对我们的消息处理逻辑做了环绕增强，当业务正常执行时则自动返回`ack`. 当业务出现异常时，根据异常判断返回不同结果：\n" +
                        "    \n" +
                        "    -   如果是**业务异常**，会自动返回`nack`；\n" +
                        "        \n" +
                        "    -   如果是**消息处理或校验异常**，自动返回`reject`;\n" +
                        "\n" +
                        "返回Reject的常见异常有：\n" +
                        ">1.  **`org.springframework.amqp.support.converter.MessageConversionException`**  \n" +
                        "    **解释**：  \n" +
                        "    该异常会在 `MessageConverter` 转换传入消息的负载时抛出。  \n" +
                        "    **触发场景**：\n" +
                        ">\n" +
                        ">    -   当使用自定义的消息转换器（如 Jackson 或 Gson）处理消息时，如果消息负载的格式与期望的类型不匹配，可能会抛出此异常。\n" +
                        ">    -   例如，收到的消息是一个无效的 JSON 字符串，而转换器尝试将其解析为 Java 对象。\n" +
                        ">2.  **`org.springframework.messaging.converter.MessageConversionException`**  \n" +
                        "    **解释**：  \n" +
                        "    如果需要额外的转换来将消息映射到 `@RabbitListener` 方法参数上时，由转换服务抛出。  \n" +
                        "    **触发场景**：\n" +
                        ">    \n" +
                        ">    -   使用 `@RabbitListener` 接收消息时，Spring 的转换服务需要将消息负载转换为方法参数的类型。\n" +
                        ">    -   如果转换器找不到合适的转换器或者转换失败，例如尝试将字符串 \"123\" 转换为自定义对象。\n" +
                        "> 3.  **`org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException`**  \n" +
                        "    **解释**：  \n" +
                        "    当 `@RabbitListener` 方法参数使用了 `@Valid` 注解，而传入的数据未通过验证时抛出此异常。  \n" +
                        "    **触发场景**：\n" +
                        " >     \n" +
                        " >    -   在接收消息的方法中，如果参数的类使用了 `javax.validation` 注解（如 `@NotNull` 或 `@Size`），而实际传入的数据不符合验证规则时，会触发此异常。\n" +
                        " >    -   例如，某个字段要求不为空 (`@NotNull`)，但实际收到的数据该字段为 `null`。\n" +
                        "> 4.  **`org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException`**  \n" +
                        "    **解释**：  \n" +
                        "    如果消息转换为的类型不正确，且与目标方法的参数类型不匹配时抛出此异常。  \n" +
                        "    **触发场景**：\n" +
                        ">    \n" +
                        ">    -   当接收的消息负载与参数类型不匹配时会触发此异常。例如，方法参数声明为 `Message<Foo>`，但实际接收到 `Message<Bar>`。\n" +
                        ">    -   这种情况通常发生在消费者端未能正确匹配消息的预期类型，或者转换器的设置有误。\n" +
                        ">5.  **`java.lang.NoSuchMethodException`**（自 1.6.3 版本添加）  \n" +
                        "    **解释**：  \n" +
                        "    如果在反射调用中找不到目标方法，则会抛出此异常。  \n" +
                        "    **触发场景**：\n" +
                        ">  \n" +
                        ">  -   监听器方法的签名不正确，无法通过反射调用。例如，方法的参数类型声明错误，导致无法找到合适的目标方法。\n" +
                        ">    -   通常由于开发者配置错误或者代码变更导致的签名不匹配引起。\n" +
                        ">6.  **`java.lang.ClassCastException`**（自 1.6.3 版本添加）  \n" +
                        "    **解释**：  \n" +
                        "    如果试图将对象强制转换为不兼容的类型，则会抛出此异常。  \n" +
                        "    **触发场景**：\n" +
                        ">    \n" +
                        ">    -   在消费者方法中显式地对消息负载或其他对象进行类型转换时，如果类型不兼容，则会抛出此异常。\n" +
                        ">    -   例如，将字符串强制转换为自定义的 Java 对象时，未正确使用消息转换器，导致类型转换失败。\n" +
                        "\n" +
                        "通过下面的配置可以修改SpringAMQP的ACK处理方式：\n" +
                        "\n" +
                        "```YAML\n" +
                        "spring:\n" +
                        "  rabbitmq:\n" +
                        "    listener:\n" +
                        "      simple:\n" +
                        "        acknowledge-mode: none # 不做处理\n" +
                        "```\n" +
                        "\n" +
                        "  \n" +
                        "\n" +
                        "修改consumer服务的SpringRabbitListener类中的方法，模拟一个消息处理的异常：\n" +
                        "\n" +
                        "```Java\n" +
                        "@RabbitListener(queues = \"simple.queue\")\n" +
                        "public void listenSimpleQueueMessage(String msg) throws InterruptedException {\n" +
                        "    log.info(\"spring 消费者接收到消息：【\" + msg + \"】\");\n" +
                        "    if (true) {\n" +
                        "        throw new MessageConversionException(\"故意的\");\n" +
                        "    }\n" +
                        "    log.info(\"消息处理完成\");\n" +
                        "}\n" +
                        "```\n" +
                        "\n" +
                        "测试可以发现：当消息处理发生异常时，消息依然被RabbitMQ删除了。\n" +
                        "\n" +
                        "我们再次把确认机制修改为auto：\n" +
                        "\n" +
                        "```YAML\n" +
                        "spring:\n" +
                        "  rabbitmq:\n" +
                        "    listener:\n" +
                        "      simple:\n" +
                        "        acknowledge-mode: auto # 自动ack\n" +
                        "```\n" +
                        "\n" +
                        "在异常位置打断点，再次发送消息，程序卡在断点时，可以发现此时消息状态为`unacked`（未确定状态）：\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20consumer%20reliability/mid1.png)\n" +
                        "\n" +
                        "放行以后，由于抛出的是**消息转换异常**，因此Spring会自动返回`reject`，所以消息依然会被删除：\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20consumer%20reliability/mid2.png)\n" +
                        "我们将异常改为RuntimeException类型：\n" +
                        "\n" +
                        "```Java\n" +
                        "@RabbitListener(queues = \"simple.queue\")\n" +
                        "public void listenSimpleQueueMessage(String msg) throws InterruptedException {\n" +
                        "    log.info(\"spring 消费者接收到消息：【\" + msg + \"】\");\n" +
                        "    if (true) {\n" +
                        "        throw new RuntimeException(\"故意的\");\n" +
                        "    }\n" +
                        "    log.info(\"消息处理完成\");\n" +
                        "}\n" +
                        "```\n" +
                        "\n" +
                        "在异常位置打断点，然后再次发送消息测试，程序卡在断点时，可以发现此时消息状态为`unacked`（未确定状态）：\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20consumer%20reliability/mid3.png)\n" +
                        "\n" +
                        "放行以后，由于抛出的是业务异常，所以Spring返回`ack`，最终消息恢复至`Ready`状态，并且没有被RabbitMQ删除：\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20consumer%20reliability/mid4.png)\n" +
                        "当我们把配置改为`auto`时，消息处理失败后，会回到RabbitMQ，并重新投递到消费者。\n" +
                        "\n" +
                        "## 失败重试机制\n" +
                        "\n" +
                        "当消费者出现异常后，消息会不断requeue（重入队）到队列，再重新发送给消费者。如果消费者再次执行依然出错，消息会再次requeue到队列，再次投递，直到消息处理成功为止。\n" +
                        "\n" +
                        "极端情况就是消费者一直无法执行成功，那么消息requeue就会无限循环，导致mq的消息处理飙升，带来不必要的压力：\n" +
                        "\n" +
                        "![](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20consumer%20reliability/mid5.png)\n" +
                        "当然，上述极端情况发生的概率还是非常低的，不过不怕一万就怕万一。为了应对上述情况Spring又提供了消费者失败重试机制：在消费者出现异常时利用本地重试，而不是无限制的requeue到mq队列。\n" +
                        "\n" +
                        "修改consumer服务的application.yml文件，添加内容：\n" +
                        "\n" +
                        "```yml\n" +
                        "spring:  \n" +
                        "  rabbitmq:  \n" +
                        "    host: 127.0.0.1  \n" +
                        "    port: 5672  \n" +
                        "  virtual-host: /David  \n" +
                        "    username: Spike  \n" +
                        "    password: '#Alone117'  \n" +
                        "  listener:  \n" +
                        "      simple:  \n" +
                        "        acknowledge-mode: auto # 自动ack  \n" +
                        "  retry:  \n" +
                        "          enabled: true # 启用重试机制  \n" +
                        "  max-attempts: 3 # 最大重试次数  \n" +
                        "  initial-interval: 100 # 初始重试间隔时间 (毫秒)  \n" +
                        "  multiplier: 1.5 # 每次重试的时间间隔倍数  \n" +
                        "  max-interval: 10000 # 最大重试间隔时间 (毫秒)  \n" +
                        "  stateless: false # true无状态；false有状态。如果业务中包含事务，这里改为false\n" +
                        " prefetch: 1 # 每次处理一条消息  \n" +
                        "  connection-timeout: 1000 # 连接超时时间 (毫秒)  \n" +
                        "```\n" +
                        "\n" +
                        "> stateless: true\n" +
                        "> -   每次处理消息时，不会记录消息的重试上下文。\n" +
                        "> -   重试次数只在当前线程中生效，每次重试失败后上下文都会重置。\n" +
                        "> -   对于每次重试来说，消费者会像处理一条新的消息一样，重新开始尝试。\n" +
                        ">\n" +
                        "> *stateless* : *true* 无状态；*false* 有状态。如果业务中包含事务，这里改为 *false*\n" +
                        "\n" +
                        "重启consumer服务，重复之前的测试。可以发现：\n" +
                        "\n" +
                        "-   消费者在失败后消息没有重新回到MQ无限重新投递，而是在本地重试了3次\n" +
                        "    \n" +
                        "-   本地重试3次以后，抛出了`AmqpRejectAndDontRequeueException`异常。查看RabbitMQ控制台，发现消息被删除了，说明最后SpringAMQP返回的是`reject`\n" +
                        "\n" +
                        "> **注意：**\n" +
                        "> -   开启本地重试时，消息处理过程中抛出异常，不会requeue到队列，而是在消费者本地重试\n" +
                        "> -   重试达到最大次数后，Spring会返回reject，消息会被丢弃\n" +
                        "\n" +
                        "## 失败处理策略\n" +
                        "\n" +
                        "在之前的测试中，本地测试达到最大重试次数后，消息会被丢弃。这在某些对于消息可靠性要求较高的业务场景下，显然不太合适了。\n" +
                        "\n" +
                        "因此Spring允许我们自定义重试次数耗尽后的消息处理策略，这个策略是由`MessageRecovery`接口来定义的，它有3个不同实现：\n" +
                        "\n" +
                        "-   `RejectAndDontRequeueRecoverer`：重试耗尽后，直接`reject`，丢弃消息。默认就是这种方式\n" +
                        "    \n" +
                        "-   `ImmediateRequeueMessageRecoverer`：重试耗尽后，返回`nack`，消息重新入队\n" +
                        "    \n" +
                        "-   `RepublishMessageRecoverer`：重试耗尽后，将失败消息投递到指定的交换机\n" +
                        "    \n" +
                        "\n" +
                        "比较优雅的一种处理方案是`RepublishMessageRecoverer`，失败后将消息投递到一个指定的，专门存放异常消息的队列，后续由人工集中处理。\n" +
                        "\n" +
                        "1. 在consumer服务中定义处理失败消息的交换机和队列\n" +
                        "\n" +
                        "```Java\n" +
                        "@Bean\n" +
                        "public DirectExchange errorMessageExchange(){\n" +
                        "    return new DirectExchange(\"error.direct\");\n" +
                        "}\n" +
                        "@Bean\n" +
                        "public Queue errorQueue(){\n" +
                        "    return new Queue(\"error.queue\", true);\n" +
                        "}\n" +
                        "@Bean\n" +
                        "public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange){\n" +
                        "    return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with(\"error\");\n" +
                        "}\n" +
                        "```\n" +
                        "\n" +
                        "  \n" +
                        "\n" +
                        "2. 定义一个RepublishMessageRecoverer，关联队列和交换机\n" +
                        "\n" +
                        "```Java\n" +
                        "@Bean\n" +
                        "public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate){\n" +
                        "    return new RepublishMessageRecoverer(rabbitTemplate, \"error.direct\", \"error\");\n" +
                        "}\n" +
                        "```\n" +
                        "\n" +
                        "完整代码如下：\n" +
                        "\n" +
                        "```java\n" +
                        "import lombok.extern.slf4j.Slf4j;\n" +
                        "import org.springframework.amqp.core.*;\n" +
                        "import org.springframework.amqp.rabbit.core.RabbitTemplate;\n" +
                        "import org.springframework.amqp.rabbit.retry.MessageRecoverer;\n" +
                        "import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "\n" +
                        "/**\n" +
                        " * 配置错误消息处理的 Direct Exchange、队列以及消息恢复器。\n" +
                        " */\n" +
                        "@Configuration\n" +
                        "@Slf4j\n" +
                        "public class ErrorDirectConfig {\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 定义一个 DirectExchange，用于路由错误消息。\n" +
                        "     *\n" +
                        "     * @return DirectExchange 实例\n" +
                        "     */\n" +
                        "    @Bean\n" +
                        "    public DirectExchange errorMessageExchange() {\n" +
                        "        return new DirectExchange(\"error.direct\");\n" +
                        "    }\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 定义一个持久化队列，用于存储错误消息。\n" +
                        "     *\n" +
                        "     * @return Queue 实例\n" +
                        "     */\n" +
                        "    @Bean\n" +
                        "    public Queue errorQueue() {\n" +
                        "        return new Queue(\"error.queue\", true);\n" +
                        "    }\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 将错误队列绑定到错误交换机，并设置路由键为 \"error\"。\n" +
                        "     *\n" +
                        "     * @param errorQueue 队列\n" +
                        "     * @param errorMessageExchange 交换机\n" +
                        "     * @return Binding 实例\n" +
                        "     */\n" +
                        "    @Bean\n" +
                        "    public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange) {\n" +
                        "        return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with(\"error\");\n" +
                        "    }\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 定义一个 RepublishMessageRecoverer，当消息处理失败达到最大重试次数时，\n" +
                        "     * 将错误消息重新发布到指定的交换机和路由键。\n" +
                        "     *\n" +
                        "     * @param rabbitTemplate RabbitTemplate 实例\n" +
                        "     * @return MessageRecoverer 实例\n" +
                        "     */\n" +
                        "    @Bean\n" +
                        "    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate) {\n" +
                        "        return new RepublishMessageRecoverer(rabbitTemplate, \"error.direct\", \"error\");\n" +
                        "    }\n" +
                        "}\n" +
                        "```\n" +
                        "\n" +
                        "## 业务幂等性\n" +
                        "\n" +
                        "### 何为幂等性\n" +
                        "\n" +
                        "**幂等**是一个数学概念，用函数表达式来描述是这样的：`f(x) = f(f(x))`，例如求绝对值函数。\n" +
                        "\n" +
                        "在程序开发中，则是指同一个业务，执行一次或多次对业务状态的影响是一致的。例如：\n" +
                        "\n" +
                        "-   根据id删除数据\n" +
                        "    \n" +
                        "-   查询数据\n" +
                        "    \n" +
                        "-   新增数据\n" +
                        "\n" +
                        "但数据的更新往往不是幂等的，如果重复执行可能造成不一样的后果。比如：\n" +
                        "\n" +
                        "-   取消订单，恢复库存的业务。如果多次恢复就会出现库存重复增加的情况\n" +
                        "    \n" +
                        "-   退款业务。重复退款对商家而言会有经济损失。\n" +
                        "\n" +
                        "所以，我们要尽可能避免业务被重复执行。\n" +
                        "\n" +
                        "然而在实际业务场景中，由于意外经常会出现业务被重复执行的情况，例如：\n" +
                        "\n" +
                        "-   页面卡顿时频繁刷新导致表单重复提交\n" +
                        "    \n" +
                        "-   服务间调用的重试\n" +
                        "    \n" +
                        "-   **MQ消息的重复投递**\n" +
                        "\n" +
                        "我们在用户支付成功后会发送MQ消息到交易服务，修改订单状态为已支付，就可能出现消息重复投递的情况。如果消费者不做判断，很有可能导致消息被消费多次，出现业务故障。\n" +
                        "\n" +
                        "举例：\n" +
                        "\n" +
                        "1.  假如用户刚刚支付完成，并且投递消息到交易服务，交易服务更改订单为**已支付**状态。\n" +
                        "    \n" +
                        "2.  由于某种原因，例如网络故障导致生产者没有得到确认，隔了一段时间后**重新投递**给交易服务。\n" +
                        "    \n" +
                        "3.  但是，在新投递的消息被消费之前，用户选择了退款，将订单状态改为了**已退款**状态。\n" +
                        "    \n" +
                        "4.  退款完成后，新投递的消息才被消费，那么订单状态会被再次改为**已支付**。业务异常。\n" +
                        "\n" +
                        "因此，我们必须想办法保证消息处理的幂等性。这里给出两种方案：\n" +
                        "\n" +
                        "-   唯一消息ID\n" +
                        "    \n" +
                        "-   业务状态判断\n" +
                        "\n" +
                        "### 唯一消息ID\n" +
                        "\n" +
                        "这个思路非常简单：\n" +
                        "\n" +
                        "1.  每一条消息都生成一个唯一的id，与消息一起投递给消费者。\n" +
                        "    \n" +
                        "2.  **消费者接收到消息后处理自己的业务，业务处理成功后将消息ID保存到数据库**\n" +
                        "    \n" +
                        "3.  **如果下次又收到相同消息，去数据库查询判断是否存在，存在则为重复消息放弃处理。**\n" +
                        "\n" +
                        "通过SpringAMQP的MessageConverter自带了MessageID的功能，我们只要开启这个功能即可。\n" +
                        "\n" +
                        "以Jackson的消息转换器为例：\n" +
                        "\n" +
                        "```Java\n" +
                        "@Configuration\n" +
                        "public class JsonConfig {\n" +
                        "    @Bean\n" +
                        "    public MessageConverter messageConverter(){\n" +
                        "        // 1.定义消息转换器\n" +
                        "        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();\n" +
                        "        // 2.配置自动创建消息id，用于识别不同消息，也可以在业务中基于ID判断是否是重复消息\n" +
                        "        jackson2JsonMessageConverter.setCreateMessageIds(true);\n" +
                        "        return jackson2JsonMessageConverter;\n" +
                        "    }\n" +
                        "}\n" +
                        "```\n" +
                        "\n" +
                        "- 生产者\n" +
                        "\n" +
                        "```java\n" +
                        "    @Test\n" +
                        "    public void workQueueK() throws InterruptedException {\n" +
                        "            Map<String,Object> msg = new HashMap<>();\n" +
                        "        String queueName = \"RabbitMQSample\";\n" +
                        "        msg.put(\"name\", \"David\");\n" +
                        "        msg.put(\"age\",\"11\");\n" +
                        "        rabbitTemplate.convertAndSend(queueName, msg);\n" +
                        "    }\n" +
                        "```\n" +
                        "\n" +
                        "- 消费者\n" +
                        "\n" +
                        "```java\n" +
                        "    @RabbitListener(queues = \"RabbitMQSample\")\n" +
                        "    public void receiveMessage(Message message) {\n" +
                        "        // 获取消息属性\n" +
                        "        MessageProperties properties = message.getMessageProperties();\n" +
                        "\n" +
                        "        // 输出消息 ID\n" +
                        "        String messageId = properties.getMessageId();\n" +
                        "        System.out.println(\"Received Message ID: \" + messageId);\n" +
                        "\n" +
                        "        // 输出消息内容\n" +
                        "        System.out.println(\"Message Body: \" + new String(message.getBody()));\n" +
                        "    }\n" +
                        "```\n" +
                        "\n" +
                        "### 业务判断\n" +
                        "\n" +
                        "业务判断就是基于业务本身的逻辑或状态来判断是否是重复的请求或消息，不同的业务场景判断的思路也不一样。\n" +
                        "\n" +
                        "例如我们当前案例中，处理消息的业务逻辑是把订单状态从未支付修改为已支付。因此我们就可以在执行业务时判断订单状态是否是未支付，如果不是则证明订单已经被处理过，无需重复处理。\n" +
                        "\n" +
                        "相比较而言，消息ID的方案需要改造原有的数据库，所以我更推荐使用业务判断的方案。\n" +
                        "\n" +
                        "以支付修改订单的业务为例，下面我提供一伪代码的逻辑\n" +
                        "\n" +
                        "- 在做业务的时候我们可以查询一下Version版本号是否能对上\n" +
                        "\n" +
                        "```sql\n" +
                        "select \n" +
                        "\t* \n" +
                        "from \n" +
                        "\torder_table \n" +
                        "where \n" +
                        "\tVersion == #{Version} \n" +
                        "\t\tand \n" +
                        "\tOrderId == #{OrderId}\n" +
                        "```\n" +
                        "\n" +
                        "- 在做业务完成后我们更新Version版本号为下一个版本\n" +
                        "\n" +
                        "```sql\n" +
                        "UPDATE order_table\n" +
                        "SET Version = #{Version + 1}, ...\n" +
                        "WHERE condition;\n" +
                        "```\n" +
                        "\n" +
                        "- 如果重复获取到这个业务，我们在查询的时候是无法对应上Version的这个业务就不会重复执行\n" +
                        "\n" +
                        "上述代码逻辑上符合了幂等判断的需求，但是由于判断和更新是两步动作，因此在极小概率下可能存在线程安全问题。\n" +
                        "\n" +
                        "## 兜底方案\n" +
                        "\n" +
                        "虽然我们利用各种机制尽可能增加了消息的可靠性，但也不好说能保证消息100%的可靠。万一真的MQ通知失败该怎么办呢？\n" +
                        "\n" +
                        "有没有其它兜底方案，能够确保订单的支付状态一致呢？\n" +
                        "\n" +
                        "其实思想很简单：既然MQ通知不一定发送到交易服务，那么交易服务就必须自己**主动去查询**支付状态。这样即便支付服务的MQ通知失败，我们依然能通过主动查询来保证订单状态的一致。\n" +
                        "\n" +
                        "流程如下：\n" +
                        "\n" +
                        "![enter image description here](https://davidhlp.asia/d/HLP/Blog/RobbitMQ/RabbitMQ%20consumer%20reliability/2024-11-2419-54-42.png)\n" +
                        "图中黄色线圈起来的部分就是MQ通知失败后的兜底处理方案，由交易服务自己主动去查询支付状态。\n" +
                        "\n" +
                        "不过需要注意的是，交易服务并不知道用户会在什么时候支付，如果查询的时机不正确（比如查询的时候用户正在支付中），可能查询到的支付状态也不正确。\n" +
                        "\n" +
                        "那么问题来了，我们到底该在什么时间主动查询支付状态呢？\n" +
                        "\n" +
                        "这个时间是无法确定的，因此，通常我们采取的措施就是利用**定时任务**定期查询，例如每隔20秒就查询一次，并判断支付状态。如果发现订单已经支付，则立刻更新订单状态为已支付即可。\n" +
                        "\n" +
                        "至此，消息可靠性的问题已经解决了。\n" +
                        "\n" +
                        "综上，支付服务与交易服务之间的订单状态一致性是如何保证的？\n" +
                        "\n" +
                        "-   首先，支付服务会正在用户支付成功以后利用MQ消息通知交易服务，完成订单状态同步。\n" +
                        "-   其次，为了保证MQ消息的可靠性，我们采用了生产者确认机制、消费者确认、消费者失败重试等策略，确保消息投递的可靠性\n" +
                        "-   最后，我们还在交易服务设置了定时任务，定期查询订单支付状态。这样即便MQ通知失败，还可以利用定时任务作为兜底方案，确保订单支付状态的最终一致性。://www.rabbitmq.com/blog/2015/04/16/scheduling-messages-with-rabbitmq)**\n\n### 下载\n\n插件下载地址：**[rabbitmq-delayed-message-exchange](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange)**\n\n由于我们安装的MQ是`4.0`版本，因此这里下载`4.0.2`版本：\n\n### 安装\n\n- 我们是基于docker-compose的RabbitMQ，只需要修改一下配置文件\n\n### 声明延迟交换机\n\n### 发送延迟消息\n\n发送消息时，必须通过x-delay属性设定延迟时间：")
                .title("RabbitMQ消费者的可靠性")
                .uniqueIdentifier("rabbitmqxiao-fei-zhe-de-ke-kao-xing")
                .build();
        articleTables.add(articleTable);
        articleRepository.saveAll(articleTables);
    }
}
