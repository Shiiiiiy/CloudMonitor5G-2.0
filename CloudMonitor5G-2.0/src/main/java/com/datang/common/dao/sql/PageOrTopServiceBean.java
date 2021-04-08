package com.datang.common.dao.sql;

import java.beans.PropertyDescriptor;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.datang.common.dao.entity.BeanResult;
import com.datang.common.dao.entity.PageListArray;
import com.datang.common.dao.jdbc.JdbcTemplate;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.ReflectUtil;

@Service
public class PageOrTopServiceBean implements PageOrTopService {

	@Resource
	JdbcTemplate jdbc;

	/**
	 * sql 语句
	 */
	private String sql;

	/**
	 * 
	 */
	private Object param;

	// -----------------------------------PageListArray<Object[]>的分页和TOP处理------------------------------------------------------

	/**
	 * 1.third 获取分页或TOPN信息
	 * 
	 * @param param
	 * @return
	 */
	public PageListArray<Object[]> getPageTop(String sql) {

		setSql(sql);

		if (null == sql || sql.isEmpty())
			return new PageListArray<Object[]>();

		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(param
				.getClass());

		if (null == fields || 0 == fields.length)
			return new PageListArray<Object[]>();

		try {
			Object pageQueryValue = null;
			Object curPageValue = null;
			Object pageSizeValue = null;
			Object topNValue = null;

			for (PropertyDescriptor field : fields) {
				String fieldName = field.getName();
				if (fieldName.equals(ParamConstant.PAGE_QUERY))
					pageQueryValue = ReflectUtil.getField(param, fieldName);
				if (fieldName.equals(ParamConstant.CUR_PAGE))
					curPageValue = ReflectUtil.getField(param, fieldName);
				if (fieldName.equals(ParamConstant.PAGE_SIZE))
					pageSizeValue = ReflectUtil.getField(param, fieldName);
				if (fieldName.equals(ParamConstant.TOP_N))
					topNValue = ReflectUtil.getField(param, fieldName);
			}

			return getPageOrTopInfo(pageQueryValue, curPageValue,
					pageSizeValue, topNValue);
		} catch (Exception e) {
			System.err.println(e.toString());
			return new PageListArray<Object[]>();
		}

	}

	/**
	 * 获取分页或TOPN信息
	 * 
	 * @param pageQueryValue
	 * @param curPageValue
	 * @param pageSizeValue
	 * @param topNValue
	 * @return
	 */
	private PageListArray<Object[]> getPageOrTopInfo(Object pageQueryValue,
			Object curPageValue, Object pageSizeValue, Object topNValue) {

		PageListArray<Object[]> result = new PageListArray<Object[]>();

		Integer pageQuery = null;
		Integer curPage = 0;
		Integer pageSize = 0;
		Integer topN = null;
		if (null != pageQueryValue && (pageQueryValue instanceof Integer)) {
			pageQuery = (Integer) pageQueryValue;
		}
		if (null != curPageValue && (curPageValue instanceof Integer)) {
			curPage = (Integer) curPageValue;
		}
		if (null != pageSizeValue && (pageSizeValue instanceof Integer)) {
			pageSize = (Integer) pageSizeValue;
		}
		if (null != topNValue && (topNValue instanceof Integer)) {
			topN = (Integer) topNValue;
		}
		if (null != topN && 0 != topN) {
			if (null != pageQuery && 1 == pageQuery) {
				// TOPN+分页
				result = jdbc.pageAndTopObjectQuery(curPage, pageSize, topN,
						sql);
			} else {
				// 仅TOPN
				result = jdbc.topObjectQuery(topN, sql);
			}

		} else {
			if (null != pageQuery && 1 == pageQuery) {
				// 仅分页
				result = jdbc.pageObjectQuery(curPage, pageSize, sql);
			} else {
				// 既不TOPN,也不分页
				result = jdbc.objectQuery(sql);
			}
		}

		return result;
	}

	/**
	 * 即不TOPN,也不分页
	 * 
	 * @author chenzhiyong
	 * @return Dec 19, 2012
	 */
	public PageListArray<Object[]> getAllInfo(String sql) {
		return jdbc.objectQuery(sql);

	}

	// -----------------------------------BeanResult<T>的分页和TOP处理------------------------------------------------------

	/**
	 * 1.third 获取分页或TOPN信息
	 * 
	 * @param param
	 * @return
	 */
	public <T> BeanResult<T> getPageTop(Class<T> entityClass, String sql) {

		setSql(sql);

		if (null == sql || sql.isEmpty())
			return new BeanResult<T>();

		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(param
				.getClass());

		if (null == fields || 0 == fields.length)
			return new BeanResult<T>();

		try {
			Object pageQueryValue = null;
			Object curPageValue = null;
			Object pageSizeValue = null;
			Object topNValue = null;

			for (PropertyDescriptor field : fields) {
				String fieldName = field.getName();
				if (fieldName.equals(ParamConstant.PAGE_QUERY))
					pageQueryValue = ReflectUtil.getField(param, fieldName);
				if (fieldName.equals(ParamConstant.CUR_PAGE))
					curPageValue = ReflectUtil.getField(param, fieldName);
				if (fieldName.equals(ParamConstant.PAGE_SIZE))
					pageSizeValue = ReflectUtil.getField(param, fieldName);
				if (fieldName.equals(ParamConstant.TOP_N))
					topNValue = ReflectUtil.getField(param, fieldName);
			}

			return getPageOrTopInfo(entityClass, pageQueryValue, curPageValue,
					pageSizeValue, topNValue);
		} catch (Exception e) {
			System.err.println(e.toString());
			return new BeanResult<T>();
		}

	}

	/**
	 * 获取分页或TOPN信息
	 * 
	 * @param pageQueryValue
	 * @param curPageValue
	 * @param pageSizeValue
	 * @param topNValue
	 * @return
	 */
	private <T> BeanResult<T> getPageOrTopInfo(Class<T> entityClass,
			Object pageQueryValue, Object curPageValue, Object pageSizeValue,
			Object topNValue) {

		BeanResult<T> result = new BeanResult<T>();

		Integer pageQuery = null;
		Integer curPage = 0;
		Integer pageSize = 0;
		Integer topN = null;
		if (null != pageQueryValue && (pageQueryValue instanceof Integer))
			pageQuery = (Integer) pageQueryValue;
		if (null != curPageValue && (curPageValue instanceof Integer))
			curPage = (Integer) curPageValue;
		if (null != pageSizeValue && (pageSizeValue instanceof Integer))
			pageSize = (Integer) pageSizeValue;
		if (null != topNValue && (topNValue instanceof Integer))
			topN = (Integer) topNValue;

		if (null != topN && 0 != topN) {
			if (null != pageQuery && 1 == pageQuery) {
				// TOPN+分页
				result = jdbc.pageAndTopBeanQuery(entityClass, curPage,
						pageSize, topN, sql);
			} else {
				// 仅TOPN
				result = jdbc.topBeanQuery(entityClass, topN, sql);
			}

		} else {
			if (null != pageQuery && 1 == pageQuery) {
				// 仅分页
				result = jdbc
						.pageBeanQuery(entityClass, curPage, pageSize, sql);
			} else {
				// 既不TOPN,也不分页
				result = jdbc.beanQuery(entityClass, sql);
			}
		}

		return result;
	}

	/**
	 * @return the param
	 */
	public Object getParam() {
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(Object param) {
		this.param = param;
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql
	 *            the sql to set
	 */
	private void setSql(String sql) {
		this.sql = sql;
	}

}
