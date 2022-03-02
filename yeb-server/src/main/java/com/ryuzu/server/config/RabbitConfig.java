package com.ryuzu.server.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ryuzu.server.domain.MailLog;
import com.ryuzu.server.service.IMailLogService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


/**
 * @author Ryuzu
 * @date 2022/3/1 21:55
 */
@Configuration
public class RabbitConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfig.class);

    public static final String EXCHANGE_NAME = "mail.welcome";

    public static final String QUEUE_NAME = "mail.queue";

    public static final String ROUTING_KEY = "mail.routing.key";


    @Resource
    private CachingConnectionFactory cachingConnectionFactory;

    @Resource
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate getRabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        /**
         * 消息确认回调,确认消息是否达到broker
         *  correlationData : 消息唯一标识
         *  ack : 确认结果
         *  cause : 失败原因
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            LOGGER.info("=============正在发送邮箱==================");
            String msgId = correlationData != null ? correlationData.getId() : "";
            if (ack) {
                LOGGER.info("{}=======>邮件发送成功", msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 1).eq("msgId", msgId));

            }else{
                LOGGER.error("{}======>邮件发送失败======>由于\n{}",msgId,cause);
            }

        });

        /**
         * 消息失败时回调,比如router 不到queue时回调
         *  msg : 消息主题
         *  replyCode : 响应码
         *  replyTest : 响应描述
         *  exchange : 交换机
         *  routing_key : 路由键
         */
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @SneakyThrows
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                LOGGER.error("{}======>消息投递到队列时失败", new String(message.getBody(), "utf-8"));
            }
        });

        return rabbitTemplate;

    }

    @Bean
    public DirectExchange mailExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue mailQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }



    @Bean
    public Binding queueBindExchange(@Qualifier("mailQueue") Queue mailQueue,
                                        @Qualifier("mailExchange") DirectExchange mailExchange){
        return BindingBuilder.bind(mailQueue).to(mailExchange).with(ROUTING_KEY);
    }

}
