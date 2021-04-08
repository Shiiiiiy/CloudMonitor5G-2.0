/**
 * 
 */
package com.datang.dao.platform.projectParam;

import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.embbCover.StationDistanceLTEPojo;
import com.datang.domain.embbCover.StationDistanceNRPojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.Cell5GNbCell;
import com.datang.domain.platform.projectParam.Cell5GtdlNbCell;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.platform.projectParam.GsmCell;
import com.datang.domain.platform.projectParam.Lte5GCell;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.platform.projectParam.TdlGsmNbCell;
import com.datang.domain.platform.projectParam.TdlNbCell;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;

/**
 * lte站间距信息dao
 * 
 * @author lucheng
 * @date:2020年5月27日 上午9:38:38
 * @version
 */
@Repository
public class StationDistanceLTEDao extends GenericHibernateDao<StationDistanceLTEPojo, Long> {

	public List<StationDistanceLTEPojo> queryStationLteDistance(String region){
		Criteria criteria = this.getHibernateSession().createCriteria(StationDistanceLTEPojo.class);
		criteria.add(Restrictions.eq("regin", region));
		List<StationDistanceLTEPojo> stationDistanceLTEPojos = (List<StationDistanceLTEPojo>)criteria.list();
		return stationDistanceLTEPojos;
	}
}
