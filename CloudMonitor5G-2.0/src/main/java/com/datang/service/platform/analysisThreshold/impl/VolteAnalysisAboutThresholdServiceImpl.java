package com.datang.service.platform.analysisThreshold.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.dao.platform.analysisThreshold.VolteAnalysisAboutThresholdDao;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisAboutThreshold;
import com.datang.domain.security.User;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisAboutThresholdService;

/**
 * Volte分析门限Service实现
 * 
 * @author shenyanwei
 * @date 2016年4月28日下午1:53:27
 */
@Service
@Transactional
public class VolteAnalysisAboutThresholdServiceImpl implements
		IVolteAnalysisAboutThresholdService {

	@Autowired
	private VolteAnalysisAboutThresholdDao volteAnalysisAboutThresholdDao;

	/**
	 * 传入参数Id查询 返回查询结果
	 */
	@Override
	public VolteAnalysisAboutThreshold selectById(Long id) {

		return volteAnalysisAboutThresholdDao.find(id);
	}

	/**
	 * 保存实体
	 */
	@Override
	public void save(VolteAnalysisAboutThreshold volteAnalysisAboutThreshold) {
		volteAnalysisAboutThresholdDao.create(volteAnalysisAboutThreshold);

	}

	/**
	 * 根据用户查询实体
	 */
	@Override
	public List<VolteAnalysisAboutThreshold> selectByUser(User user) {

		return volteAnalysisAboutThresholdDao.selectByUser(user);
	}

	/**
	 * 修改门限
	 */
	@Override
	public void update(VolteAnalysisAboutThreshold volteAnalysisAboutThreshold) {
		volteAnalysisAboutThresholdDao.updateBlob(volteAnalysisAboutThreshold);

	}

	/**
	 * 删除一个门限值
	 */
	@Override
	public void delete(Long id) {
		volteAnalysisAboutThresholdDao.delete(id);
	}

	@Override
	public VolteAnalysisAboutThreshold selectByType(String thresholdType) {

		return volteAnalysisAboutThresholdDao.selectByType(thresholdType);
	}

	@Override
	public List<VolteAnalysisAboutThreshold> selectCompareByUser(User user) {
		List<VolteAnalysisAboutThreshold> volteAnalysisAboutTList = null;

		if (user != null) {
			volteAnalysisAboutTList = volteAnalysisAboutThresholdDao
					.selectByUser(user);
		}
		Map<String, VolteAnalysisAboutThreshold> map = new HashMap<String, VolteAnalysisAboutThreshold>();
		for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : volteAnalysisAboutTList) {
			map.put(volteAnalysisAboutThreshold.getNameEn(),
					volteAnalysisAboutThreshold);
		}
		List<VolteAnalysisAboutThreshold> list = new ArrayList<VolteAnalysisAboutThreshold>();
		list.add(new VolteAnalysisAboutThreshold(null, "MOS均值恶化门限",
				VolteAnalysisThresholdTypeConstant.MOS_VORSEN, "1", user));
		list.add(new VolteAnalysisAboutThreshold(null, "MOS均值稍降门限",
				VolteAnalysisThresholdTypeConstant.MOS_DECLINE, "0.4", user));
		list.add(new VolteAnalysisAboutThreshold(null, "RSRP均值恶化门限(dBm)",
				VolteAnalysisThresholdTypeConstant.RSRP_AVAREGE_VORSEN, "-100",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "RSRP均值稍降门限(dBm)",
				VolteAnalysisThresholdTypeConstant.RSRP_AVAREGE_DECLINE, "-95",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "RSRP差值恶化门限",
				VolteAnalysisThresholdTypeConstant.RSRP_DIFFERENCE_VORSEN,
				"10", user));
		list.add(new VolteAnalysisAboutThreshold(null, "RSRP差值稍降门限",
				VolteAnalysisThresholdTypeConstant.RSRP_DIFFERENCE_DECLINE,
				"5", user));
		list.add(new VolteAnalysisAboutThreshold(null, "SINR均值恶化门限",
				VolteAnalysisThresholdTypeConstant.SINR_AVAREGE_VORSEN, "6",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "SINR差值恶化门限",
				VolteAnalysisThresholdTypeConstant.SINR_DIFFERENCE_VORSEN, "4",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "SINR均值稍降门限",
				VolteAnalysisThresholdTypeConstant.SINR_AVAREGE_DECLINE, "9",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "SINR差值稍降门限",
				VolteAnalysisThresholdTypeConstant.SINR_DIFFERENCE_DECLINE,
				"2", user));
		list.add(new VolteAnalysisAboutThreshold(null, "RTP丢包率均值恶化门限(%)",
				VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_AVAREGE_VORSEN,
				"30", user));
		list.add(new VolteAnalysisAboutThreshold(
				null,
				"RTP丢包率差值恶化门限(%)",
				VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_DIFFERENCE_VORSEN,
				"30", user));
		list.add(new VolteAnalysisAboutThreshold(
				null,
				"RTP丢包率均值稍降门限(%)",
				VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_AVAREGE_DECLINE,
				"20", user));
		list.add(new VolteAnalysisAboutThreshold(
				null,
				"RTP丢包率差值稍降门限(%)",
				VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_DIFFERENCE_DECLINE,
				"20", user));
		list.add(new VolteAnalysisAboutThreshold(null, "栅格大小(米)",
				VolteAnalysisThresholdTypeConstant.GRIDSIZE, "500", user));
		for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : list) {
			VolteAnalysisAboutThreshold aboutThreshold = map
					.get(volteAnalysisAboutThreshold.getNameEn());
			if (aboutThreshold != null) {
				volteAnalysisAboutThreshold.setId(aboutThreshold.getId());
				volteAnalysisAboutThreshold.setValue(aboutThreshold.getValue());
			}
		}
		return list;
	}

}
