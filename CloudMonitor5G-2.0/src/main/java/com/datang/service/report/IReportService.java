package com.datang.service.report;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.report.StatisticeTask;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * 统计任务接口
 * 
 * @explain
 * @name IReportService
 * @author shenyanwei
 * @date 2016年9月12日上午11:29:41
 */
public interface IReportService {
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
	public StatisticeTask queryOneByID(Long id);

	/**
	 * 保存统计任务
	 */
	public void save(StatisticeTask statisticeTask);

	/**
	 * 删除统计任务
	 */
	public void delete(List<Long> ids);

	/**
	 * 修改
	 * 
	 * @param statisticeTask
	 */
	public void update(StatisticeTask statisticeTask);

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
	
	/**
	 * 根据条件查询cqt统计任务
	 * @author lucheng
	 * @date 2020年8月19日 下午7:28:10
	 * @param pageList
	 * @return
	 */
	public List<StatisticeTask> findStatisticeTask(PageList pageList);
}
