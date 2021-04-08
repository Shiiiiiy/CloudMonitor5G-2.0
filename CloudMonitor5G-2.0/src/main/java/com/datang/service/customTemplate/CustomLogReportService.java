package com.datang.service.customTemplate;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.customTemplate.CustomLogReportTask;
import com.datang.domain.report.StatisticeTask;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * 自定义日志统计任务接口
 * 
 * @explain
 * @name CustomLogReportService
 */
public interface CustomLogReportService {
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
	public CustomLogReportTask queryOneByID(Long id);
	
	/**
	 * 多个查询
	 * @author lucheng
	 */
	public List<CustomLogReportTask> queryTaskByIds(List<Long> ids);

	/**
	 * 保存统计任务
	 */
	public void save(CustomLogReportTask statisticeTask);

	/**
	 * 删除统计任务
	 */
	public void delete(List<Long> ids);

	/**
	 * 修改
	 * 
	 * @param CustomLogReportTask
	 */
	public void update(CustomLogReportTask customLogReportTask);
	
	/**
	 * 根据条件查询cqt统计任务
	 * @author lucheng
	 * @date 2020年8月19日 下午7:28:10
	 * @param pageList
	 * @return
	 */
	public List<CustomLogReportTask> findStatisticeTask(PageList pageList);
}
