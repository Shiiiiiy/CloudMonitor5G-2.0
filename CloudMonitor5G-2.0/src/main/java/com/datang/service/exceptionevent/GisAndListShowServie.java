package com.datang.service.exceptionevent;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.exceptionevent.Iads5gExceptionEventTable;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.LteCell;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-20 15:57
 */
public interface GisAndListShowServie {

    AbstractPageList pageList(PageList pageList);

    AbstractPageList pageListMove45g(PageList pageList);

    AbstractPageList pageListMoveSwitch(PageList pageList);

    AbstractPageList pageListAccess(PageList pageList);
    
    AbstractPageList pageListBussiness(PageList pageList);

    Iads5gExceptionEventTable queryRecordById(String fileId);

    /**
     * 根据日志id,时间戳获取采样点经纬度
     * @author maxuancheng
     * date:2019年12月11日 下午4:47:35
     * @param recSeqNo
     * @param timeLong
     * @return
     */
	public List<Object> getGpsPointData(Long recSeqNo, Long timeLong,Integer timeHigh,Integer timeLow,String fileName);

	/**
	 * 根据pci频点获取小区数据
	 * @author maxuancheng
	 * date:2019年12月19日 下午5:24:50
	 * @param nrPci
	 * @param point
	 * @return
	 */
	public List<Cell5G> getCellIdAndCellName(String nrPci, String point);
	
	/**
	 * 根据lte小区友好名获取小区数据
	 * @param lteFriendlyName
	 * @author lucheng
	 * @return
	 */
	public List<LteCell> getLteCell(String lteFriendlyName);
	
	/**
	 * 根据nr小区友好名获取小区数据
	 * @param nrFriendlyName
	 * @author lucheng
	 * @return
	 */
	public List<Cell5G> getNrCell(String nrFriendlyName);

}
