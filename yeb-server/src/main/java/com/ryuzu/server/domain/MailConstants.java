package com.ryuzu.server.domain;

/**
 * @author Ryuzu
 * @date 2022/3/2 14:08
 */
public class MailConstants {
    // 消息发送中
    public static final Integer DELIVERING = 0;
    // 消息发送成功
    public static final Integer SUCCESS = 1;
    // 消息发送失败
    public static final Integer FAILURE = 2;
    // 最大重试次数
    public static final Integer MAX_RETRY_COUNT = 3;
    // 消息超时时间 1分钟
    public static final Integer OVERTIME = 1;

}
