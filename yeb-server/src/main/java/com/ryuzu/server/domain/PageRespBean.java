package com.ryuzu.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ryuzu
 * @date 2022/2/25 17:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRespBean {

    /**
     * 总条数
     */
    private Long total;

    /**
     * 数据list
     */
    private List<?> data;
}
