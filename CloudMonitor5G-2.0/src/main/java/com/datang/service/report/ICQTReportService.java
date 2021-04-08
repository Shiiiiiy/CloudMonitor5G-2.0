package com.datang.service.report;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.report.CQTStatisticeTask;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * CQT统计任务Service接口
 * 
 * @explain
 * @name ICQTReportService
 * @author shenyanwei
 * @date 2016年10月26日上午10:47:48
 */
public interface ICQTReportService {
	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);

	/**
	 * 单个查询
	 */
	public CQTStatisticeTask queryOneByID(Long id);

	/**
	 * 保存统计任务
	 */
	public void save(CQTStatisticeTask statisticeTask);

	/**
	 * 删除统计任务
	 */
	public void delete(List<Long> ids);

	/**
	 * 修改
	 * 
	 * @param statisticeTask
	 */
	public void update(CQTStatisticeTask statisticeTask);

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
