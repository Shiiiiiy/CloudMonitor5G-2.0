package com.datang.dao.testPlan;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.testPlan.HttpCommandURL;

/**
 * httpCommand测试命令必选浏览地址和随机浏览地址dao
 * 
 * @author yinzhipeng 2015-04-21
 * 
 */
@Repository
public class HttpCommandURLDao extends
		GenericHibernateDao<HttpCommandURL, Integer> {
	/**
	 * 查询所有必选浏览地址
	 * 
	 * @return
	 */
	public List<HttpCommandURL> findAllMustURL() {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				HttpCommandURL.class);
		createCriteria.add(Expression.eq("isUrlMust", "0"));
		return createCriteria.list();
	}

	/**
	 * 查询所有随机浏览地址
	 * 
	 * @return
	 */
	public List<HttpCommandURL> findAllRandomURL() {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				HttpCommandURL.class);
		createCriteria.add(Expression.eq("isUrlMust", "1"));
		return createCriteria.list();
	}

	/**
	 * 根据url获取必选浏览地址
	 * 
	 * @param url
	 * @return
	 */
	public HttpCommandURL findMustURLByUrl(String url) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				HttpCommandURL.class);
		createCriteria.add(Expression.eq("url", url));
		createCriteria.add(Expression.eq("isUrlMust", "0"));
		return (HttpCommandURL) createCriteria.uniqueResult();
	}

	/**
	 * 根据url获取随机浏览地址
	 * 
	 * @param url
	 * @return
	 */
	public HttpCommandURL findRandomURLByUrl(String url) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				HttpCommandURL.class);
		createCriteria.add(Expression.eq("url", url));
		createCriteria.add(Expression.eq("isUrlMust", "1"));
		return (HttpCommandURL) createCriteria.uniqueResult();
	}

}
