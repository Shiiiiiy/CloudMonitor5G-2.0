package com.datang.service.customTemplate;

import java.io.File;
import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.customTemplate.CustomReportTemplatePojo;

/**
 * 自定义模板服务
 * @author lucheng
 * @date 2020年12月22日 下午2:49:58
 */
public interface CustomTemplateService {
	
	/**
	 * 查询自定义报表模板表信息
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQuery(PageList pageList);
	
	/**
	 * 根据id查询报表模板信息
	 * @author lucheng
	 * @date 2020年12月22日 下午6:45:19
	 * @param id
	 * @return
	 */
	public CustomReportTemplatePojo find(Long id);
	
	/**
	 * 上传自定义报表模板表信息
	 * @param cityId
	 *            市级域ID
	 * @param xlsFile
	 *            上传的excel文件
	 */
	public void importReportTemplate(Long cityId, File importFile,String importFileFileName);
	
	/**
	 * 删除自定义模板
	 * @author lucheng
	 * @date 2020年12月22日 下午3:14:31
	 * @param idsList
	 */
	public void deleteCustomTemplate(List<Long> idsList);
	
	/**
	 * 根据参数查询模板信息
	 * @author lucheng
	 * @date 2020年12月25日 下午5:19:16
	 * @param pageList
	 * @return
	 */
	public List<CustomReportTemplatePojo> queryTemplateByParam(PageList pageList);

	/**
	 * 根据参数获取excel的公式计算结果重新保存到新的excel
	 * @author lucheng
	 * @date 2020年12月30日 下午1:25:06
	 * @param page 参数：任务名称，日志id，模板的kpi，excel保存路径 
	 * @return 新的得到值的excel路径
	 */
	public String modifyExcelValue(PageList page);
	
}
