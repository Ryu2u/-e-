package com.ryuzu.server.service.impl;

import com.ryuzu.server.domain.PoliticsStatus;
import com.ryuzu.server.mapper.PoliticsStatusMapper;
import com.ryuzu.server.service.IPoliticsStatusService;
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
public class PoliticsStatusServiceImpl extends ServiceImpl<PoliticsStatusMapper, PoliticsStatus> implements IPoliticsStatusService {

}
