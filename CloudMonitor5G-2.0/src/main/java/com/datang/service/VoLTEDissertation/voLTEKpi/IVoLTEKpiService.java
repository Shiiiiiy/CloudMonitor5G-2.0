/**
 * 
 */
package com.datang.service.VoLTEDissertation.voLTEKpi;

import java.util.List;

import com.datang.domain.VoLTEDissertation.wholePreview.VoLteKpi;

/**
 * volteKpi服务接口
 * 
 * @author yinzhipeng
 * @date:2015年11月21日 下午12:13:12
 * @version
 */
public interface IVoLTEKpiService {
	/**
	 * 获取所有VoLTEKpi
	 * 
	 * @return
	 */
	public List<VoLteKpi> findAll();

	/**
	 * 根据参数获取VoLTEKpi
	 * 
	 * @param reportType
	 * @param stairClassify
	 * @return
	 */
	public List<VoLteKpi> findVolteKpiByParam(String reportType,
			String stairClassify);

}
