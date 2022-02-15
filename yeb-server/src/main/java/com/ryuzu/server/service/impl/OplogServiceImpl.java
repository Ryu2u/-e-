package com.ryuzu.server.service.impl;

import com.ryuzu.server.domain.Oplog;
import com.ryuzu.server.mapper.OplogMapper;
import com.ryuzu.server.service.IOplogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@Service
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
