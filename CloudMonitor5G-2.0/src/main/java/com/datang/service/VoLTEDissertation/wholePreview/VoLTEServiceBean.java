/**
 * 
 */
package com.datang.service.VoLTEDissertation.wholePreview;

import java.util.LinkedHashMap;
import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.dao.entity.PageListArray;
import com.datang.common.service.sql.SqlService;
import com.datang.common.util.StringUtils;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * volte专题---volte整体分析共用service抽象类
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 下午1:15:51
 * @version
 */
public abstract class VoLTEServiceBean implements IVoLTEService {
	protected SqlService sqlService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.wholePreview.IVoLTEService#queryKpi
	 * (com
	 * .datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam)
	 */
	@Override
	public AbstractPageList queryKpi(VoLTEWholePreviewParam inputParam) {
		if (null == inputParam
				|| !StringUtils.hasText(inputParam.getTestLogItemIds())) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		PageListArray<Object[]> result = new PageListArray<Object[]>();
		result = sqlService.parse(inputParam);
		System.out.println(sqlService.getSql());
		List<Object[]> datas = result.getDatas();
		List rows = easyuiPageList.getRows();
		// List<Map<String, Object>> list = new ArrayList<>();
		for (Object[] objects : datas) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			List<String> fields = result.getFields();
			for (String string : fields) {
				int index = result.getIndex(string);
				map.put(string, objects[index]);
			}
			// list.add(map);
			rows.add(map);
		}
		// easyuiPageList.setRows(list);
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.wholePreview.IVoLTEService#exportKpi
	 * (
	 * com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam
	 * )
	 */
	@Override
	public AbstractPageList exportKpi(VoLTEWholePreviewParam inputParam) {
		return queryKpi(inputParam);
	}

	/**
	 * @return the sqlService
	 */
	public SqlService getSqlService() {
		return sqlService;
	}

	/**
	 * @param sqlService
	 *            the sqlService to set
	 */
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}
}
