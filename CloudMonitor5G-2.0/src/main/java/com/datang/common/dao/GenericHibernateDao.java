package com.datang.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.util.GenericManager;

/**
 * 基于spring的泛型DAO实现.
 * 
 * @author songzhigang
 * @param <T>
 * @param <ID>
 * @modify yinzhipeng at 2015-05-15 modify the getHibernateSession method
 * 
 */
public class GenericHibernateDao<T, ID extends Serializable> extends
		HibernateDaoSupport {
	/**
	 * 实体Class.
	 */
	private final Class<T> entityClass;

	/**
	 * 注入SessionFactory
	 * 
	 * @param SessionFactory
	 */
	@Autowired
	@Qualifier("sessionFactory")
	protected void setEntityManagerFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 构造函数，确定实体类型.
	 */
	@SuppressWarnings("unchecked")
	public GenericHibernateDao() {
		this.entityClass = (Class<T>) GenericManager.getGeneric(getClass());
	}

	/**
	 * @return the entityClass
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * 按ID返回实体对象.
	 * 
	 * @param id
	 *            实体ID.
	 * @return T 按返回实体。
	 */
	@SuppressWarnings("unchecked")
	public T find(ID id) {
		return (T) getHibernateSession().get(entityClass, id);
	}

	/**
	 * 返回全部的实体对象.
	 * 
	 * @return List 返回关于T的LIST.
	 */
	@SuppressWarnings("unchecked")
	public Collection<T> findAll() {
		return (Collection<T>) getHibernateSession()
				.createCriteria(entityClass).list();
	}

	/**
	 * 插入新的实体对象.
	 * 
	 * @param entity
	 *            需要保存的实体对象.
	 */
	public void create(T entity) {
		Session session = getHibernateSession();
		session.save(entity);
		session.flush();
	}

	/**
	 * 按ID删除相应的实体对象.
	 * 
	 * @param entityId
	 *            实体对象ID编号.
	 */
	public void delete(ID entityId) {
		if (null != find(entityId)) {
			delete(find(entityId));
		}
	}

	/**
	 * 删除相应的实体对象.
	 * 
	 * @param entity
	 *            需要保存的实体对象.
	 */
	public void delete(T entity) {
		Session session = getHibernateSession();
		session.delete(entity);
		session.flush();
	}

	/**
	 * 按ID修改相应的实体对象.
	 * 
	 * @param entity
	 *            需要修改的实体对象.
	 */
	public void update(T entity) {
		Session session = getHibernateSession();
		session.merge(entity);
		session.flush();
	}

	/**
	 * 按ID修改相应的实体对象，当对象包含类型字段时.
	 * 
	 * @param entity
	 *            需要修改的实体对象.
	 */
	public void updateBlob(T entity) {
		Session session = getHibernateSession();
		session.evict(entity);
		session.update(entity);
		session.flush();
	}

	/**
	 * @return Session
	 */
	public Session getHibernateSession() {
		return this.getSessionFactory().getCurrentSession();
	}

	/**
	 * 取得startIndex页每页pageSize的记录集合.
	 * 
	 * @param detachedCriteria
	 *            查询条件对象
	 * @param pageSize
	 *            每页记录数
	 * @param startIndex
	 *            起始数
	 * @return Collection　结果集
	 */
	@SuppressWarnings("unchecked")
	public Collection<T> findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int pageSize,
			final int startIndex) {
		return (Collection<T>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
						Collection<T> pageItems = criteria
								.setFirstResult(startIndex)
								.setMaxResults(pageSize).list();
						return pageItems;
					}
				});

	}

	/**
	 * 取得所有记录.
	 * 
	 * @param detachedCriteria
	 *            查询条件对象
	 * @return Collection　结果集
	 */
	@SuppressWarnings("unchecked")
	public Collection<T> findAllByCriteria(
			final DetachedCriteria detachedCriteria) {
		return (Collection<T>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						return criteria.list();
					}
				});
	}

	/**
	 * 计算记录的总数.
	 * 
	 * @param detachedCriteria
	 *            查询条件对象
	 * @return int
	 */
	public int getCountByCriteria(final DetachedCriteria detachedCriteria) {
		Integer count = (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						return criteria.setProjection(Projections.rowCount())
								.uniqueResult();
					}
				});
		return count.intValue();
	}

	/**
	 * 根据索引得到分页查询结果
	 * 
	 * @param queryString
	 *            查询HSql
	 * @param values
	 *            值
	 * @param firstReslut
	 *            开始索引
	 * @param maxResults
	 *            最大数目
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List findByPage(final String queryString, final Object[] values,
			final int firstReslut, final int maxResults)
			throws DataAccessException {

		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				if (maxResults > 0 && firstReslut >= 0) {
					queryObject.setFirstResult(firstReslut);
					queryObject.setMaxResults(maxResults);
				}
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}

		});
	}

	public EasyuiPageList getPageList(PageList pageList, Class<T> clazz) {
		if (null == pageList) {
			return new EasyuiPageList();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				clazz);
		long total = 0;
		createCriteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		createCriteria.setFirstResult((pageNum - 1) * rowsCount);
		createCriteria.setMaxResults(rowsCount);
		List list = createCriteria.list();
		if(list.size() > 0){
			total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}

}
