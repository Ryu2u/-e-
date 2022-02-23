package com.ryuzu.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 swagger controller
 *
 * @author Ryuzu
 * @date 2022/2/19 17:02
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/system/cfg/test")
    public String test() {

        return "/system/cfg/test";

    }

    @GetMapping("/employee/advanced/test")
    public String test2() {

        return "/employee/advanced/test";

    }

    @GetMapping("/employee/basic/test")
    public String test3(){
        return "/employee/basic/test";
    }

}
