package com.ryuzu.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ryuzu.server.config.RabbitConfig;
import com.ryuzu.server.domain.Employee;
import com.ryuzu.server.domain.MailConstants;
import com.ryuzu.server.domain.MailLog;
import com.ryuzu.server.service.IEmployeeService;
import com.ryuzu.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ryuzu
 * @date 2022/3/2 15:25
 */
@Component
public class MailTask {

    @Resource
    private IMailLogService mailLogService;

    @Resource
    private IEmployeeService employeeService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask() {
        List<MailLog> mailLogList = mailLogService.list(new QueryWrapper<MailLog>().eq("status", 0).lt("tryTime", LocalDateTime.now()));
        mailLogList.forEach(mailLog -> {
            // 查看重试次数是否超过3次
            if (mailLog.getCount() >= 3) {
                // 修改状态码为2 表示发送失败
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 2).eq("msgId", mailLog.getMsgId()));
            } else {
                // 更新邮件状态
                mailLogService.update(new UpdateWrapper<MailLog>().eq("msgId", mailLog.getMsgId()).set("count", mailLog.getCount() + 1).set("tryTime",
                        LocalDateTime.now().plusMinutes(MailConstants.OVERTIME)).set(
                        "updateTime",
                        LocalDateTime.now()));

                Employee employee = employeeService.exportEmployee(mailLog.getEid()).get(0);
                // 重新发送邮件
                rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, employee, new CorrelationData(mailLog.getMsgId()));
            }

        });
    }
}
