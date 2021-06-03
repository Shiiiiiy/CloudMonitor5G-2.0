package com.datang.web.beans.testLogItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.datang.common.util.StringUtils;

public class UnicomLogItemPageQueryRequestBean {

	private List<String> fileNameList;

	// 联通日志特有的  省市条件
	private String prov;
	private String city;
	private String taskName;

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * 是否上传完成
	 */  
	private Boolean isFinished;
	/**
	 * 路测文件状态（已解析，未解析等）<br>
	 * 0:上传中,未解析 <br>
	 * 1:上传成功,解析中 <br>   
	 * 2:上传成功,已解析成功 <br>
	 * 其他:上传成功,解析失败
	 */
	private Integer testFileStatus;

	/**
	 * 时候是对比日志查询
	 */
	private Boolean isCompare;
	/**
	 * 已选择的测试文件ID
	 */
	private String selectTestLogItemIds;
	/**
	 * 开始时间
	 */
	private Date beginDate;
	/**
	 * 结束时间
	 */
	private Date endDate;
	/**
	 * 市级区域id集合
	 */
	private String cityIds;
	/**
	 * 预存市级区域id集合
	 */
	private String prestorecityIds;
	/**
	 * 数据来源
	 */
	private String logSource;
	/**
	 * 业务类型
	 */
	private String serviceType;
	/**
	 * 运营商
	 */
	private String operators;
	/**
	 * 设备Id集合
	 */
	private String boxIds;
	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 测试目标
	 */
	private String testTarget;
	/**
	 * 通道号
	 */
	private String galleryNo;
	
	private Set<Long> ids;


	//省份
	private String province;
	
	//运营商
	private String operator; 
	
	//测试时长
	private String testTime;
	
	//文件大小
	private String fileSize;
	
	//状态
	private String status;
	
	//MD5校验
	private String md5Check;
	
	//业务事件校验
	private String trafficCheck;
	
	//5G时长驻留比
	private String kpi1;
	
	//覆盖率（-105&-3）
	private String kpi2;
	
	//FTP下行速率
	private String kpi3;
	
	//FTP上行速率
	private String kpi4;
	
	//百度云下载
	private String kpi5;
	
	//VOLTE主叫EPS FB接通率
	private String kpi6;
	
	//VOLTE主叫掉线率
	private String kpi7;
	
	//FR成功率
	private String kpi8;

	private String testName;


	private Set<String> cityName;
	
	/**
	 * 存放BOXID的集合,保存页面的传过来的boxID(cityid失效),保存页面传过来的cityid下的boxID(没有直接选中设备),
	 * 保存用户所有权限的boxID
	 */
	private Set<String> boxIdsSet = new HashSet<>();

	/**
	 * 存放解析后的boxid
	 * 
	 * @return the boxIdsSetboxIdsSet
	 */
	public Set<String> getBoxIdsSet() {
		return boxIdsSet;
	}

	/**
	 * @param boxIdsSet
	 *            the boxIdsSet to set
	 */
	public void setBoxIdsSet(Set<String> boxIdsSet) {
		this.boxIdsSet = boxIdsSet;
	}

	/**
	 * 获取请求参数boxids格式"a,b,c,d",按","解析成list(boxid)
	 * 
	 * @return
	 */
	public Set<String> getBoxIdsList() {
		Set<String> list = new HashSet<>();
		if (StringUtils.hasText(this.boxIds)) {
			String[] split = this.boxIds.trim().split(",");
			for (int i = 0; i < split.length; i++) {
				if (StringUtils.hasText(split[i])) {
					list.add(split[i].trim());
				}
			}
		}
		return list;
	}


	public Set<String> getCityName() {
		return cityName;
	}

	public void setCityName(Set<String> cityName) {
		this.cityName = cityName;
	}

	/**
	 * 获取请求参数cityIds
	 * 
	 * @return
	 */
	public List<Long> getCityIdsList() {
		List<Long> list = new ArrayList<>();
		if (StringUtils.hasText(this.cityIds)) {
			String[] split = this.cityIds.trim().split(",");
			for (int i = 0; i < split.length; i++) {
				if (StringUtils.hasText(split[i])) {
					try {
						list.add(Long.parseLong(split[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}
		return list;
	}

	public Set<Long> getIds() {
		return ids;
	}

	public void setIds(Set<Long> ids) {
		this.ids = ids;
	}

	/**
	 * @return the beginDatebeginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDateendDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the cityIdscityIds
	 */
	public String getCityIds() {
		return cityIds;
	}

	/**
	 * @param cityIds
	 *            the cityIds to set
	 */
	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	/**
	 * @return the prestorecityIdsprestorecityIds
	 */
	public String getPrestorecityIds() {
		return prestorecityIds;
	}

	/**
	 * @param prestorecityIds
	 *            the prestorecityIds to set
	 */
	public void setPrestorecityIds(String prestorecityIds) {
		this.prestorecityIds = prestorecityIds;
	}

	/**
	 * @return the operatorsoperators
	 */
	public List<String> getOperators() {
		if (StringUtils.hasText(operators)) {
			List<String> list = new ArrayList<>();
			String[] split = operators.split(",");
			for (String string : split) {
				list.add(string.trim());
			}
			return list;
		}
		return null;
	}

	/**
	 * @param operators
	 *            the operators to set
	 */
	public void setOperators(String operators) {
		this.operators = operators;
	}

	/**
	 * @return the boxIdsboxIds
	 */
	public String getBoxIds() {
		return boxIds;
	}

	/**
	 * @param boxIds
	 *            the boxIds to set
	 */
	public void setBoxIds(String boxIds) {
		this.boxIds = boxIds;
	}

	/**
	 * @return the fileNamefileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the testTargettestTarget
	 */
	public String getTestTarget() {
		return testTarget;
	}

	/**
	 * @param testTarget
	 *            the testTarget to set
	 */
	public void setTestTarget(String testTarget) {
		this.testTarget = testTarget;
	}

	/**
	 * @return the logSourcelogSource
	 */
	public List<Integer> getLogSource() {
		if (StringUtils.hasText(logSource)) {
			List<Integer> list = new ArrayList<>();
			String[] split = logSource.split(",");
			for (String string : split) {
				try {
					Integer parseInt = Integer.parseInt(string);
					list.add(parseInt);
				} catch (NumberFormatException e) {
					continue;
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * @param logSource
	 *            the logSource to set
	 */
	public void setLogSource(String logSource) {
		this.logSource = logSource;
	}

	/**
	 * @return the serviceTypeserviceType
	 */
	public List<Integer> getServiceType() {
		if (StringUtils.hasText(serviceType)) {
			List<Integer> list = new ArrayList<>();
			String[] split = serviceType.split(",");
			for (String string : split) {
				try {
					Integer parseInt = Integer.parseInt(string);
					list.add(parseInt);
				} catch (NumberFormatException e) {
					continue;
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * @param serviceType
	 *            the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return the isFinishedisFinished
	 */
	public Boolean getIsFinished() {
		return isFinished;
	}

	/**
	 * @param isFinished
	 *            the isFinished to set
	 */
	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}

	/**
	 * @return the testFileStatustestFileStatus
	 */
	public Integer getTestFileStatus() {
		return testFileStatus;
	}

	/**
	 * @param testFileStatus
	 *            the testFileStatus to set
	 */
	public void setTestFileStatus(Integer testFileStatus) {
		this.testFileStatus = testFileStatus;
	}

	/**
	 * @return the selectTestLogItemIdsselectTestLogItemIds
	 */
	public List<Long> getSelectTestLogItemIds() {
		List<Long> list = new ArrayList<>();
		if (StringUtils.hasText(this.selectTestLogItemIds)) {
			String[] split = this.selectTestLogItemIds.trim().split(",");
			for (int i = 0; i < split.length; i++) {
				if (StringUtils.hasText(split[i])) {
					try {
						list.add(Long.parseLong(split[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}
		return list;
	}

	/**
	 * @param selectTestLogItemIds
	 *            the selectTestLogItemIds to set
	 */
	public void setSelectTestLogItemIds(String selectTestLogItemIds) {
		this.selectTestLogItemIds = selectTestLogItemIds;
	}

	/**
	 * @return the isCompareisCompare
	 */
	public Boolean getIsCompare() {
		return isCompare;
	}

	/**
	 * @param isCompare
	 *            the isCompare to set
	 */
	public void setIsCompare(Boolean isCompare) {
		this.isCompare = isCompare;
	}

	/**
	 * @return the galleryNo
	 */
	public String getGalleryNo() {
		return galleryNo;
	}

	/**
	 *            galleryNo to set
	 */

	public void setGalleryNo(String galleryNo) {
		this.galleryNo = galleryNo;
	}

	/**
	 * 获取请求参数galleryNo格式"a,b,c,d",按","解析成list(galleryNo)
	 * 
	 * @return
	 */
	public Set<String> getGalleryNoList() {
		Set<String> list = new HashSet<>();
		if (StringUtils.hasText(this.galleryNo)) {
			String[] split = this.galleryNo.trim().split(",");
			for (int i = 0; i < split.length; i++) {
				if (StringUtils.hasText(split[i])) {
					list.add(split[i].trim());
				}
			}
		}
		return list;
	}

	public List<String> getMd5Check() {
		if (StringUtils.hasText(md5Check)) {
			List<String> list = new ArrayList<>();
			String[] split = md5Check.split(",");
			for (String string : split) {
				list.add(string.trim());
			}
			return list;
		}
		return null;
	}

	public void setMd5Check(String md5Check) {
		this.md5Check = md5Check;
	}

	public List<String> getTrafficCheck() {
		if (StringUtils.hasText(trafficCheck)) {
			List<String> list = new ArrayList<>();
			String[] split = trafficCheck.split(",");
			for (String string : split) {
				list.add(string.trim());
			}
			return list;
		}
		return null;
	}

	public void setTrafficCheck(String trafficCheck) {
		this.trafficCheck = trafficCheck;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}


	/**
	 * 问题索引
	 * **/

	private String questionIndex;
	private String latitude;
	private String longitude;

	public String getQuestionIndex() {
		return questionIndex;
	}

	public void setQuestionIndex(String questionIndex) {
		this.questionIndex = questionIndex;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * 获取请求参数cityIds
	 *
	 * @return
	 */
	public List<String> getQuestionIndexList() {
		List<String> list = new ArrayList<>();
		if (StringUtils.hasText(this.questionIndex)) {
			String[] split = this.questionIndex.trim().split(",");
			for (int i = 0; i < split.length; i++) {
				if (StringUtils.hasText(split[i])) {
					list.add(split[i].trim());
				}
			}
		}
		return list;
	}


	public List<String> getFileNameList() {
		return fileNameList;
	}

	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestLogItemPageQueryRequestBean [isFinished=" + isFinished
				+ ", testFileStatus=" + testFileStatus + ", isCompare="
				+ isCompare + ", selectTestLogItemIds=" + selectTestLogItemIds
				+ ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", cityIds=" + cityIds + ", prestorecityIds="
				+ prestorecityIds + ", logSource=" + logSource
				+ ", serviceType=" + serviceType + ", operators=" + operators
				+ ", boxIds=" + boxIds + ", fileName=" + fileName
				+ ", testTarget=" + testTarget + ", galleryNo=" + galleryNo
				+ ", boxIdsSet=" + boxIdsSet + "]";
	}
}
