package com.ryuzu.mail;


import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Ryuzu
 * @date 2022/3/1 20:18
 */

public class TestApp {


    public void testMail() throws MessagingException, GeneralSecurityException {
        // 配置发送邮件的环境属性
        Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");//遇到最多的坑就是下面这行，不加要报“A secure connection is required”错。
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        // 发件人的账号
        props.put("mail.user", "ryu2u@qq.com");
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", "dwej usby oebx bbfd");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("ryu2u@qq.com");
                String password = props.getProperty("CHENhaitao1015");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("ryu2u@qq.com"));
        message.setFrom(form);

        // 设置收件人
        InternetAddress to = new InternetAddress("182210222209@stu.just.edu.cn");
        message.setRecipient(RecipientType.TO, to);

        // 设置抄送，抄送和密送如果不写正确的地址也要报错。最好注释不用。
//        InternetAddress cc = new InternetAddress("");
//        message.setRecipient(RecipientType.CC, cc);
//
//        // 设置密送，其他的收件人不能看到密送的邮件地址
//        InternetAddress bcc = new InternetAddress("");
//        message.setRecipient(RecipientType.CC, bcc);

        // 设置邮件标题
        message.setSubject("JAVA测试邮件");

        // 设置邮件的内容体
        message.setContent("<a href='http://www.XXX.org'>测试的邮件</a>", "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);
    }


    public void test02() throws MessagingException {
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        Session session = Session.getInstance(props);

//邮件内容部分
        Message msg = new MimeMessage(session);
        msg.setSubject("JAVA测试邮件");
        StringBuilder builder = new StringBuilder();
        builder.append("测试发送邮件");
        builder.append("\n data " + new Date());
        msg.setText(builder.toString());
//邮件发送者
        msg.setFrom(new InternetAddress("ryu2u@qq.com"));

//发送邮件
        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "ryu2u@qq.com", "qluxkhjketisbddj");

        transport.sendMessage(msg, new Address[] { new InternetAddress("182210222209@stu.just.edu.cn") });
        transport.close();


    }

}
