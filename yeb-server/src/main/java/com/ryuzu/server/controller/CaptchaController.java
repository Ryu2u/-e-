package com.ryuzu.server.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码控制器
 *
 * @author Ryuzu
 * @date 2022/2/20 18:29
 */
@RestController
public class CaptchaController {

    @Resource
    private DefaultKaptcha defaultKaptcha;

    @GetMapping(value = "/captcha", produces = "image/jpeg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        // 定义response输出内容为image/jpeg类型
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Controller", "no-store,no-cache,must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader)
        response.addHeader("Cache-Controller","post-check=0,pre-check=0");
        // Set standard HTTP/1.1 no-cache header
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        //--------------------------生成验证码 begin------------------------/
        // 获取验证码文本内容
        String text = defaultKaptcha.createText();
        System.out.println("验证码内容:" + text);
        // 将验证码文本内容放入session
        request.getSession().setAttribute("captcha", text);
        // 根据验证码文本内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream servletOutputStream = null;
        try {
          servletOutputStream = response.getOutputStream();
            ImageIO.write(image, "jpg", servletOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (servletOutputStream != null) {
                try {
                    servletOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //--------------------------生成验证码 end  ------------------------/
    }
}
