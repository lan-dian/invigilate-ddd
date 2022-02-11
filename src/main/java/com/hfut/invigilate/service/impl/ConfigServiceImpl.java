package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.Config;
import com.hfut.invigilate.mapper.ConfigMapper;
import com.hfut.invigilate.model.exception.BusinessException;
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

    @Override
    public String getString(String key){
        Config config = lambdaQuery().eq(Config::getName, key).one();
        if(config==null){
            throw new BusinessException("没有"+key+"属性的系统配置");
        }
        return config.getValue();
    }

    @Override
    public Integer getInteger(String key){
        return Integer.valueOf(getString(key));
    }

    @Override
    public Long getLong(String key){
        return Long.valueOf(getString(key));
    }


}
