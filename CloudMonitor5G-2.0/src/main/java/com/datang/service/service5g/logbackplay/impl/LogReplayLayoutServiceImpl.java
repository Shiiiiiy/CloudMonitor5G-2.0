package com.datang.service.service5g.logbackplay.impl;

import com.datang.dao.dao5g.logbackplay.LogReplayLayoutConfigDao;
import com.datang.domain.testLogItem.LayoutConfig;
import com.datang.service.service5g.logbackplay.LogReplayLayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shiyan
 */
@Service
public class LogReplayLayoutServiceImpl implements LogReplayLayoutService {


    @Autowired
    private LogReplayLayoutConfigDao configDao;

    @Override
    public List<LayoutConfig> getLayoutConfig() {
        return configDao.getLayoutConfig();
    }

    @Override
    public LayoutConfig saveConfig(String configValue,Long id,String name) {

        LayoutConfig defaultLayoutConfig = getDefaultLayoutConfig();
        if (defaultLayoutConfig!=null) {
            defaultLayoutConfig.setStatus("0");
            configDao.create(defaultLayoutConfig);
        }

        if(id!=null){
            LayoutConfig  old = getLayoutConfig(id);
            old.setValue(configValue);
            old.setStatus("1");
            return configDao.saveConfig(old);
        }else{
            LayoutConfig config = new LayoutConfig();
            config.setValue(configValue);
            config.setStatus("1");
            config.setName(name);
            return configDao.saveConfig(config);
        }
    }

    @Override
    public LayoutConfig getLayoutConfig(Long id) {

       return configDao.getLayoutConfig(id);
    }

    @Override
    public LayoutConfig getDefaultLayoutConfig() {
        return configDao.getDefaultLayoutConfig();
    }


}
