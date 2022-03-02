package com.ryuzu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Ryuzu
 * @date 2022/2/14 21:21
 */
@SpringBootApplication
@MapperScan(basePackages = "com.ryuzu.server.mapper")
@EnableScheduling
public class YebApplication {
    public static void main(String[] args) {

        SpringApplication.run(YebApplication.class, args);

    }
}
