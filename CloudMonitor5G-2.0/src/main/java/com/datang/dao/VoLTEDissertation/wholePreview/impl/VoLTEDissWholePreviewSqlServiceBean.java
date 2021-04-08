/**
 * 
 */
package com.datang.dao.VoLTEDissertation.wholePreview.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.datang.common.dao.sql.PageOrTopService;
import com.datang.common.service.sql.GroupByService;
import com.datang.common.service.sql.OrderByService;
import com.datang.common.service.sql.SelectService;
import com.datang.common.service.sql.TableService;
import com.datang.common.service.sql.WhereService;
import com.datang.common.service.sql.impl.BuildSqlServiceBean;

/**
 * @author yinzhipeng
 * @date:2015年11月20日 下午2:34:39
 * @version
 */
@Service
public class VoLTEDissWholePreviewSqlServiceBean extends BuildSqlServiceBean {

	/**
	 * 
	 */
	public VoLTEDissWholePreviewSqlServiceBean() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.impl.SqlServiceBean#setSelect(com.datang
	 * .common.service.sql.SelectService)
	 */
	@Override
	@Resource(name = "wholePreviewSelectServiceBean")
	public void setSelect(SelectService select) {
		this.select = select;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.impl.SqlServiceBean#setTable(com.datang
	 * .common.service.sql.TableService)
	 */
	@Override
	@Resource(name = "wholePreviewTableServiceBean")
	public void setTable(TableService table) {
		this.table = table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.impl.SqlServiceBean#setWhere(com.datang
	 * .common.service.sql.WhereService)
	 */
	@Override
	@Resource(name = "wholePreviewWhereServiceBean")
	public void setWhere(WhereService where) {
		this.where = where;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.impl.SqlServiceBean#setGroupBy(com.datang
	 * .common.service.sql.GroupByService)
	 */
	@Override
	@Resource(name = "wholePreviewGroupByServiceBean")
	public void setGroupBy(GroupByService groupBy) {
		this.groupBy = groupBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.impl.SqlServiceBean#setOrderBy(com.datang
	 * .common.service.sql.OrderByService)
	 */
	@Override
	@Resource(name = "orderByServiceBean")
	public void setOrderBy(OrderByService orderBy) {
		this.orderBy = orderBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.impl.SqlServiceBean#setPageService(com.
	 * datang.common.dao.sql.PageOrTopService)
	 */
	@Override
	@Resource(name = "pageOrTopServiceBean")
	public void setPageService(PageOrTopService pageService) {
		this.pageService = pageService;
	}

}
