package com.datang.service.service5g.questionRoad;

import com.datang.service.influx.bean.QuesRoadThreshold;

import java.util.List;

/**
 * @author shiyan
 * @date 2021/6/23 16:24
 */
public interface QuestionRoadParamService {

    /**
     * 更新参数
     * @param names  字段名
     * @param values 字段值
     */
    void updateThreshold(String[] names,String[] values);

    List<QuesRoadThreshold> queryAll();

}
