package com.ryuzu.server.service.impl;

import com.ryuzu.server.domain.Nation;
import com.ryuzu.server.mapper.NationMapper;
import com.ryuzu.server.service.INationService;
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
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}
