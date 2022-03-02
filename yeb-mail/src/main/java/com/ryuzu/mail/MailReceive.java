package com.ryuzu.mail;

import com.rabbitmq.client.Channel;
import com.ryuzu.server.config.RabbitConfig;
import com.ryuzu.server.domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Ryuzu
 * @date 2022/3/1 22:04
 */
@Component
public class MailReceive {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceive.class);

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private MailProperties mailProperties;
    @Resource
    private TemplateEngine templateEngine;
    @Resource
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handler(Message message, Channel channel){
        HashOperations hashOperations = redisTemplate.opsForHash();

        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String)headers.get("spring_returned_message_correlation");

        MimeMessage msg = null;
        try {
            if (hashOperations.entries("mail_log").containsKey(msgId)) {
                // 已经被消费过了
                LOGGER.error("邮件已发送!");
                channel.basicAck(tag, false);
                return;
            }
            msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            // 发件人
            helper.setFrom(mailProperties.getUsername());
            // 收件人
            helper.setTo(employee.getEmail());
            // 主题
            helper.setSubject("欢迎入职");
            // 发送日期
            helper.setSentDate(new Date());

            Context context  = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", employee.getName());
            variables.put("posName", employee.getPosition().getName());
            variables.put("joblevelName", employee.getJoblevel().getName());
            variables.put("departmentName", employee.getDepartment().getName());
            context.setVariables(variables);
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            javaMailSender.send(msg);
            // 发送成功
            LOGGER.info("邮件发送成功!");
            // 将消息存入redis
            hashOperations.put("mail_log", msgId, "ok");
            channel.basicAck(tag, false);
        } catch (MessagingException | IOException e) {
            try {
                channel.basicNack(tag, false, true);
            } catch (IOException ioException) {
                LOGGER.error("{}+++++++>消息发送失败",msgId);
                ioException.printStackTrace();
            }
            LOGGER.error("{}+++++++>消息发送失败",msgId);
            e.printStackTrace();
        }

    }
}
