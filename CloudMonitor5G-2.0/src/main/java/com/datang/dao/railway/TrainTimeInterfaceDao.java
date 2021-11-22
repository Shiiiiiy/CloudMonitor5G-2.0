package com.datang.dao.railway;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.railway.TrainPojo;
import com.datang.domain.railway.TrainTimeInterfacePojo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 14:03
 * @Version 1.0
 **/
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TrainTimeInterfaceDao extends GenericHibernateDao<TrainTimeInterfacePojo, Long> {

}