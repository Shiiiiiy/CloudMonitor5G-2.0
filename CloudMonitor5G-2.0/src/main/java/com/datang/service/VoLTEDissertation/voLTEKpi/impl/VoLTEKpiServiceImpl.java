/**
 * 
 */
package com.datang.service.VoLTEDissertation.voLTEKpi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.VoLTEDissertation.voLTEKpi.VoLTEKpiDao;
import com.datang.domain.VoLTEDissertation.wholePreview.VoLteKpi;
import com.datang.service.VoLTEDissertation.voLTEKpi.IVoLTEKpiService;

/**
 * voltekpi服务接口实现
 * 
 * @author yinzhipeng
 * @date:2015年11月21日 下午12:16:34
 * @version
 */
@Service
@Transactional
public class VoLTEKpiServiceImpl implements IVoLTEKpiService {
	@Autowired
	private VoLTEKpiDao voLTEKpiDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.voLTEKpi.IVoLTEKpiService#findAll()
	 */
	@Override
	public List<VoLteKpi> findAll() {
		return voLTEKpiDao.queryAllVoLTEKpi();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.voLTEKpi.IVoLTEKpiService#
	 * findVolteKpiByParam(java.lang.String, java.lang.String)
	 */
	@Override
	public List<VoLteKpi> findVolteKpiByParam(String reportType,
			String stairClassify) {
		return voLTEKpiDao.queryVoLTEKpiListByParam(reportType, stairClassify);
	}

}
