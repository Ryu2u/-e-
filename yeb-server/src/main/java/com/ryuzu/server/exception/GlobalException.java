package com.ryuzu.server.exception;

import com.ryuzu.server.domain.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局处理异常
 * @author Ryuzu
 * @date 2022/2/24 12:12
 */
/*@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public RespBean sqlException(Exception e) {

        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("该数据有其他数据关联,操作失败!");
        }
        return RespBean.error("数据库异常,操作失败!");

    }
}*/
