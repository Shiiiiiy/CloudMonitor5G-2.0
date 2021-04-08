/**
 * 
 */
package com.datang.service.VoLTEDissertation.wholePreview;

import com.datang.common.action.page.AbstractPageList;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * volte专题---volte整体分析共用service接口
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 上午9:59:01
 * @version
 */
public interface IVoLTEService {
	/**
	 * 查询指标数据
	 * 
	 * @param inputParam
	 * @return
	 */
	public AbstractPageList queryKpi(VoLTEWholePreviewParam inputParam);

	/**
	 * 导出指标数据
	 * 
	 * @param inputParam
	 * @return
	 */
	public AbstractPageList exportKpi(VoLTEWholePreviewParam inputParam);

}
