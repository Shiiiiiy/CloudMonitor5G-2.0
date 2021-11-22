package com.datang.dao.railway;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.railway.TrainPojo;
import com.datang.domain.railway.TrainStationPojo;
import com.datang.service.influx.bean.QuesRoadThreshold;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 13:20
 * @Version 1.0
 **/
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TrainStationDao extends GenericHibernateDao<TrainStationPojo, Long> {

    public List<TrainStationPojo> queryAllTrainStation() {
        Criteria createCriteria = this.getHibernateSession().createCriteria(TrainStationPojo.class);
        List list = createCriteria.list();
        return (List<TrainStationPojo>)list;
    }

    public TrainStationPojo findTelegraphCodeByName(String startStationName) {
        Criteria createCriteria = this.getHibernateSession().createCriteria(TrainStationPojo.class);
        createCriteria.add(Restrictions.eq("stationName", startStationName));
        List<TrainStationPojo> list = (List<TrainStationPojo>)createCriteria.list();
        if(list==null || list.size()==0){
            return new TrainStationPojo();
        }else{
            return list.get(0);
        }
    }
}
