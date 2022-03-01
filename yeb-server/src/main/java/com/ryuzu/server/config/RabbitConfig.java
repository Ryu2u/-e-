package com.ryuzu.server.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ryuzu
 * @date 2022/3/1 21:55
 */
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "mail.welcome";

    public static final String QUEUE_NAME = "mail.queue";

    public static final String ROUTING_KEY = "mail.routing.key";


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
