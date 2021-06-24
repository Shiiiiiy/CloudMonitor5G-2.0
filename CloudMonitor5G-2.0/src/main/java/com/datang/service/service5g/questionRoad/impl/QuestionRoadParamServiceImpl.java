package com.datang.service.service5g.questionRoad.impl;

import com.datang.dao.quesroad.QuesRoadDao;
import com.datang.service.influx.bean.QuesRoadThreshold;
import com.datang.service.service5g.questionRoad.QuestionRoadParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author shiyan
 * @date 2021/6/23 16:25
 */
@Service
public class QuestionRoadParamServiceImpl implements QuestionRoadParamService {

    @Autowired
    private QuesRoadDao quesRoadDao;

    @Override
    public void updateThreshold(String[] names, String[] values) {

        for (int i = 0; i < names.length; i++) {
            QuesRoadThreshold quesRoadThreshold = quesRoadDao.queryByEnName(names[i]);
            quesRoadThreshold.setValue(Double.valueOf(values[i]));
            quesRoadDao.update(quesRoadThreshold);
        }
    }

    @Override
    public List<QuesRoadThreshold> queryAll() {
        return quesRoadDao.queryAll();
    }
}
