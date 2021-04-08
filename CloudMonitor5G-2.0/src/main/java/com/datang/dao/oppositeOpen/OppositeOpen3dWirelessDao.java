package com.datang.dao.oppositeOpen;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dResultPojo;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dWirelessPojo;

@Repository
public class OppositeOpen3dWirelessDao extends GenericHibernateDao<OppositeOpen3dWirelessPojo,Long>{

	@SuppressWarnings("unchecked")
	public OppositeOpen3dWirelessPojo findByParamId(Long id) {
		Criteria criteria = this.getHibernateSession().createCriteria(OppositeOpen3dWirelessPojo.class);
		Criteria c2 = criteria.createCriteria("plan4GParam");
		c2.add(Restrictions.eq("id", id));
		List list = criteria.list();
		if(list == null || list.size() < 1){
			return null;
		}else{
			return (OppositeOpen3dWirelessPojo) list.get(0);
		}
	}

	/**
	 * 根据基站名查询数据
	 * @author maxuancheng
	 * date:2020年3月16日 上午11:07:52
	 * @param siteName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public OppositeOpen3dWirelessPojo findBySiteName(String siteName) {
		Criteria criteria = this.getHibernateSession().createCriteria(OppositeOpen3dWirelessPojo.class);
		criteria.add(Restrictions.eq("siteName", siteName));
		List list = criteria.list();
		if(list == null || list.size() < 1){
			return null;
		}else{
			return (OppositeOpen3dWirelessPojo) list.get(0);
		}
	}
	

}
