/**
 * 
 */
package com.datang.service.VoLTEDissertation.wholePreview.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.datang.common.service.sql.SqlService;
import com.datang.service.VoLTEDissertation.wholePreview.VoLTEServiceBean;

/**
 * volte专题---volte整体分析service实现
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 下午1:32:12
 * @version
 */
@Service
public class VoLTEDissWholePreviewBean extends VoLTEServiceBean {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.wholePreview.VoLTEServiceBean#
	 * setSqlService(com.datang.common.service.sql.SqlService)
	 */
	@Override
	@Resource(name = "voLTEDissWholePreviewSqlServiceBean")
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}

}
