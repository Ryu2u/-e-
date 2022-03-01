package com.ryuzu.mail;

import com.ryuzu.server.config.RabbitConfig;
import com.ryuzu.server.domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Ryuzu
 * @date 2022/3/1 22:04
 */
@Component
public class MailReceive {
    private static final Logger logger = LoggerFactory.getLogger(MailReceive.class);

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private MailProperties mailProperties;
    @Resource
    private TemplateEngine templateEngine;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handler(Employee employee){
        System.out.println("=============正在发送邮箱==================");
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
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
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
