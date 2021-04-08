package com.datang.dao.oppositeOpen;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dResultPojo;

@Repository
public class OppositeOpen3dResultDao extends GenericHibernateDao<OppositeOpen3dResultPojo,Long>{

	@SuppressWarnings("rawtypes")
	public OppositeOpen3dResultPojo findByParamId(Long id) {
		Criteria criteria = this.getHibernateSession().createCriteria(OppositeOpen3dResultPojo.class);
		Criteria c2 = criteria.createCriteria("plan4GParam");
		c2.add(Restrictions.eq("id", id));
		List list = criteria.list();
		if(list == null || list.size() < 1){
			return null;
		}else{
			return (OppositeOpen3dResultPojo) list.get(0);
		}
	}

}
