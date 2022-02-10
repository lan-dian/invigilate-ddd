package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.Config;
import com.hfut.invigilate.mapper.ConfigMapper;
import com.hfut.invigilate.service.ConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 配置 服务实现类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

}
