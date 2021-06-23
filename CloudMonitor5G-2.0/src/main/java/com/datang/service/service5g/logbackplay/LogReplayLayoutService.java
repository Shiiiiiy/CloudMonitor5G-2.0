package com.datang.service.service5g.logbackplay;

import com.datang.domain.testLogItem.LayoutConfig;

import java.util.List;

/**
 * @author shiyan
 */
public interface LogReplayLayoutService {

    /**
     * 获取配置
     * @return 布局配置
     */
    List<LayoutConfig> getLayoutConfig();

    LayoutConfig getLayoutConfig(Long id);

    LayoutConfig getDefaultLayoutConfig();

    LayoutConfig saveConfig(String configValue ,Long id,String name);





}
