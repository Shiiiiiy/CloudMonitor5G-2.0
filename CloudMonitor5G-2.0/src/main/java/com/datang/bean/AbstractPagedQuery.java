package com.datang.bean;

import java.io.Serializable;
import java.util.Collection;

/**
 * 分页查询对象(java bean),用来包装分页查询条件并构造查询语句.
 * @param <T> 分页查询返回的记录类型
 * @author songzhigang
 */
public abstract class AbstractPagedQuery<T> implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -1288547530935895889L;

    /**
     * 默认一页显示的记录数.
     */
    private static final int DEFAULT_ITEMS_PERPAGE = 20;

    /**
     * 查询的结果集合.
     */
    private Collection<T> queryResults;

    /**
     * 查询条件:查询起始纪录位置.
     */
    private int pagerOffset;

    /**
     * 查询结果,总共条目个数.
     */
    private int pagerCount = 0;

    /**
     * 分页条件:一页的记录条数.
     */
    private int pagerPerPage = DEFAULT_ITEMS_PERPAGE;

    /**
     * @return the pagerCount
     */
    public int getPagerCount() {
        return pagerCount;
    }

    /**
     * @param pPagerCount the pagerCount to set
     */
    public void setPagerCount(int pPagerCount) {
        this.pagerCount = pPagerCount;
    }

    /**
     * @return the pagerOffset
     */
    public int getPagerOffset() {
        return pagerOffset;
    }

    /**
     * @param pPagerOffset the pagerOffset to set
     */
    public void setPagerOffset(int pPagerOffset) {
        this.pagerOffset = pPagerOffset;
    }

    /**
     * @return the pagerPerPage
     */
    public int getPagerPerPage() {
        return pagerPerPage;
    }

    /**
     * @param pPagerPerPage the pagerPerPage to set
     */
    public void setPagerPerPage(int pPagerPerPage) {
        this.pagerPerPage = pPagerPerPage;
    }

    /**
     * @return the queryResults
     */
    public Collection<T> getQueryResults() {
        return queryResults;
    }

    /**
     * @param pQueryResults the queryResults to set
     */
    public void setQueryResults(Collection<T> pQueryResults) {
        this.queryResults = pQueryResults;
    }

    /**
     * 是否是有效的查询条件.
     * @return boolean
     */
    public boolean isValidQuery() {
        return (this.pagerOffset >= 0 && pagerOffset <= pagerCount);
    }
}
