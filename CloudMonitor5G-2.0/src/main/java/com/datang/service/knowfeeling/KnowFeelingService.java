package com.datang.service.knowfeeling;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.knowfeeling.KnowFeelingReportTask;

import java.util.List;
import java.util.Map;

/**
 * @author shiyan
 * @date 2021/9/6 13:21
 */
public interface KnowFeelingService {


    /**
     * 多条件分页查询
     *
     * @param pageList
     * @return
     */
     AbstractPageList pageList(PageList pageList);

    /**
     * 新增
     * @param task
     * @return
     */
    void save(KnowFeelingReportTask task);

    /**
     * 查询业务详单
     * @param logName
     * @return
     */
    List<Map<String,Object>> queryKnowFeelingStatics(String... logName);

    /**
     * 根据id 查找
     * @param id 主键id
     * @return 查找结果
     */
    KnowFeelingReportTask find(Long id );

    /**
     * 批量删除
     * @param ids id
     */
    void delete(List<Long> ids);
}
