package com.datang.dao.stationTest;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stationTest.EceptionCellLogPojo;

/**
 * 日志指标详表Dao
 * @author maxuancheng
 *
 */
@Repository
public class EceptionCellLogDao  extends GenericHibernateDao<EceptionCellLogPojo, Long>{

	/**
	 * 通过文件名查询数据
	 * @author maxuancheng
	 * date:2020年3月27日 上午10:36:08
	 * @param fileName
	 * @return
	 */
	public List<EceptionCellLogPojo> findByFileName(String fileName) {
		Criteria cc = this.getHibernateSession().createCriteria(EceptionCellLogPojo.class);
		cc.add(Restrictions.eq("nrRelatelogname", fileName));
		return cc.list();
	}

}
