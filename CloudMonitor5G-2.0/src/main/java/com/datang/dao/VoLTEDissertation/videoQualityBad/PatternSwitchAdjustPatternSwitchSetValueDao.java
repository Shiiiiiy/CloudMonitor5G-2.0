package com.datang.dao.VoLTEDissertation.videoQualityBad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.videoQualityBad.patternSwitch.PatternSwitchAdjustPatternSwitchSetValue;
import com.datang.domain.VoLTEDissertation.videoQualityBad.patternSwitch.VideoQualityBadPatternSwitch;

/**
 * volte质量专题---volte视频质差---模式转换--优化建议：调整模式转换设置值Dao
 * 
 * @explain
 * @name PatternSwitchAdjustPatternSwitchSetValueDao
 * @author shenyanwei
 * @date 2017年6月5日下午3:32:44
 */
@Repository
@SuppressWarnings("all")
public class PatternSwitchAdjustPatternSwitchSetValueDao extends
		GenericHibernateDao<PatternSwitchAdjustPatternSwitchSetValue, Long> {
	/**
	 * 根据模式转换问题获取优化建议
	 * 
	 * @param videoQualityBadPatternSwitch
	 * @return
	 */
	public List<PatternSwitchAdjustPatternSwitchSetValue> queryDownDispatchSmallInspectCellUserAndExistDisturb(
			VideoQualityBadPatternSwitch videoQualityBadPatternSwitch) {

		if (null == videoQualityBadPatternSwitch
				|| null == videoQualityBadPatternSwitch.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				PatternSwitchAdjustPatternSwitchSetValue.class);
		Criteria criteria = createCriteria
				.createCriteria("videoQualityBadPatternSwitch");
		criteria.add(Restrictions.eq("id", videoQualityBadPatternSwitch.getId()));
		return createCriteria.list();
	}

}
