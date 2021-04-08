/**
 * 
 */
package com.datang.service.platform.projectParam;

import java.io.File;
import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.embbCover.StationDistanceLTEPojo;
import com.datang.domain.embbCover.StationDistanceNRPojo;
import com.datang.domain.platform.projectParam.CellInfo;

/**
 * 工程参数service接口
 * 
 * @author yinzhipeng
 * @date:2015年10月19日 下午4:39:11
 * @version
 */
public interface IProjectParamService {

	/**
	 * 上传5G小区表信息
	 * 
	 * @param cityId
	 *            市级域ID
	 * @param infoType
	 *            运营商类型
	 * @param xlsFile
	 *            上传的excel文件
	 * @return int[] 第一位为excel记录数,第二位为导入失败记录数
	 */
	public int[] import5GCell(Long cityId, String infoType, File xlsFile);

	/**
	 * 上传邻区表信息
	 * 
	 * @param cityId
	 *            市级域ID
	 * @param infoType
	 *            运营商类型
	 * @param operatorType
	 *            网络制式类型
	 * @param xlsFile
	 *            上传的excel文件
	 * @return int[] 第一位为excel记录数,第二位为导入失败记录数
	 */
	public int[] import5GNbCell(Long cityId, String infoType,
			String operatorType, File xlsFile);

	/**
	 * 上传小区表信息
	 * 
	 * @param cityId
	 *            市级域ID
	 * @param infoType
	 *            运营商类型
	 * @param operatorType
	 *            网络制式类型
	 * @param xlsFile
	 *            上传的excel文件
	 * @return int[] 第一位为excel记录数,第二位为导入失败记录数
	 */
	public int[] importCell(Long cityId, String infoType, String operatorType,
			File xlsFile);

	/**
	 * 上传邻区表信息
	 * 
	 * @param cityId
	 *            市级域ID
	 * @param infoType
	 *            运营商类型
	 * @param operatorType
	 *            网络制式类型
	 * @param xlsFile
	 *            上传的excel文件
	 * @return int[] 第一位为excel记录数,第二位为导入失败记录数
	 */
	public int[] importNbCell(Long cityId, String infoType,
			String operatorType, File xlsFile);
	/**
	 * 上传邻区表信息
	 * 
	 * @param cityId
	 *            市级域ID
	 * @param infoType
	 *            运营商类型
	 * @param operatorType
	 *            网络制式类型
	 * @param xlsFile
	 *            上传的excel文件
	 * @return int[] 第一位为excel记录数,第二位为导入失败记录数
	 */
	public int[] importLte5GCell(Long cityId, String infoType,String operatorType, File importFile);
	
	/**
	 * 上传5G规划表信息
	 * 
	 * @param cityId
	 *            市级域ID
	 *            网络制式类型
	 * @param xlsFile
	 *            上传的excel文件
	 * @return int[] 第一位为excel记录数,第二位为导入失败记录数
	 */
	public String[] import5GPlanManageParam(Long cityId, String operatorType, File importFile,String importFileFileName);
	
	/**
	 * 上传4G规划表信息
	 * 
	 * @param cityId
	 *            市级域ID
	 *            网络制式类型
	 * @param xlsFile
	 *            上传的excel文件
	 * @return int[] 第一位为excel记录数,第二位为导入失败记录数
	 */
	public String[] import4GPlanManageParam(Long cityId, String operatorType, File importFile,String importFileFileName);
	
	/**
	 * 查询规划参数表信息
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQuery(PageList pageList);
	
	/**
	 * 查询导入的5G规划参数表详细信息
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQueryParam5G(PageList pageList);
	
	/**
	 * 查询导入的4G规划参数表详细信息
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQueryParam4G(PageList pageList);
	
	/**
	 * 根据id查询规划工参导入信息
	 * @param id
	 * @return
	 */
	public CellInfo findById(Long id);
	
	/**
	 * 保存5g站间距
	 * @param stationDistanceNRPojo
	 */
	public void createNrDistance(StationDistanceNRPojo stationDistanceNRPojo);
	
	/**
	 * 保存4g站间距
	 * @param stationDistanceLTEPojo
	 */
	public void createLteDistance(StationDistanceLTEPojo stationDistanceLTEPojo);
	
	/**
	 * 查询5g区域对应已保存的站间距
	 * @param region
	 */
	public List<StationDistanceNRPojo> queryStationNRDistance(String region);
	
	/**
	 * 查询4g区域对应已保存的站间距
	 * @param region
	 */
	public List<StationDistanceLTEPojo> queryStationLteDistance(String region);
	
	/**
	 * 删除5g区域对应已保存的站间距
	 * @param region
	 */
	public void deleteNrDistance(List<StationDistanceNRPojo> list);
	
	/**
	 * 删除4g区域对应已保存的站间距
	 * @param region
	 */
	public void deleteLteDistance(List<StationDistanceLTEPojo> list);
	
	/**
	 * 根据小区名称获取最近邻区
	 * @param cellName 小区名称
	 * @param coverType 网络制式
	 */
	public Object getDistanceByCellName(String cellName,Integer coverType);
	
	
	/**
	 * 根据id删除工参，包括cellinfo，规划工参表的记录，生成报告后的参数表
	 */
	public void deleteCellById(List<String> idsList);

}
