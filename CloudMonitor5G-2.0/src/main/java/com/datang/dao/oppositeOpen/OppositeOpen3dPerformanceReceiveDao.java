package com.datang.dao.oppositeOpen;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dPerformanceReceivePojo;

@Repository
public class OppositeOpen3dPerformanceReceiveDao extends GenericHibernateDao<OppositeOpen3dPerformanceReceivePojo,Long>{

	/**
	 * 根据工参id获取数据
	 * @author maxuancheng
	 * date:2020年3月16日 下午4:01:36
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public OppositeOpen3dPerformanceReceivePojo findByParamId(Long id) {
		Criteria criteria = this.getHibernateSession().createCriteria(OppositeOpen3dPerformanceReceivePojo.class);
		Criteria c2 = criteria.createCriteria("plan4GParam");
		c2.add(Restrictions.eq("id", id));
		List list = criteria.list();
		if(list == null || list.size() < 1){
			return null;
		}else{
			return (OppositeOpen3dPerformanceReceivePojo)list.get(0);
		}
	}

}
