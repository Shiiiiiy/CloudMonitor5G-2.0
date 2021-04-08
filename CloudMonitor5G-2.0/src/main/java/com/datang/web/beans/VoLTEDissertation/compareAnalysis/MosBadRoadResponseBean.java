/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.compareAnalysis;

import java.io.Serializable;

/**
 * 对比分析----MOS差黑点路段列表返回bean
 * 
 * @author yinzhipeng
 * @date:2016年6月1日 上午9:16:26
 * @version
 */
public class MosBadRoadResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7766747564145368215L;

	/**
	 * 原始质差路段的测试日志主键
	 */
	private Long recSeqNo;

	/**
	 * 原始质差路段的测试日志文件名
	 */
	private String fileName;

	/**
	 * 原始质差路段的测试日志的BOXID
	 */
	private String boxId;

	/**
	 * 原始质差路段id
	 */
	private Long id;

	/**
	 * 原始质差路段名称,根据以下3对经纬度信息获取
	 */
	private String m_stRoadName;
	private Float beginLatitude;
	private Float courseLatitude;
	private Float endLatitude;
	private Float beginLongitude;
	private Float courseLongitude;
	private Float endLongitude;

	/**
	 * 原始质差路段里程:单位m
	 */
	private Float m_dbDistance;
	/**
	 * 原始质差路段持续测试时间:ms
	 */
	private Long m_dbContinueTime;

	/**
	 * 对比质差路段的测试日志主键
	 */
	private Long compareRecSeqNo;

	/**
	 * 对比质差路段的测试日志文件名
	 */
	private String compareFileName;

	/**
	 * 对比质差路段的测试日志的BOXID
	 */
	private String compareBoxId;

	/**
	 * 对比质差路段id
	 */
	private Long compareId;

	/**
	 * 对比质差路段名称,根据以下3对经纬度信息获取
	 */
	private String compareM_stRoadName;
	private Float compareBeginLatitude;
	private Float compareCourseLatitude;
	private Float compareEndLatitude;
	private Float compareBeginLongitude;
	private Float compareCourseLongitude;
	private Float compareEndLongitude;

	/**
	 * 对比质差路段里程:单位m
	 */
	private Float compareM_dbDistance;
	/**
	 * 对比质差路段持续测试时间:ms
	 */
	private Long compareM_dbContinueTime;

	/**
	 * 存储未合并的对比质差路段的id按","分隔
	 */
	private String compareIds;

	/**
	 * 存储MOS差黑点的中心点
	 */
	private Float mosBadLatitude;
	private Float mosBadLongitude;

	/**
	 * MOS差黑点路段类型
	 */
	private Integer qbrType;

	/**
	 * @return the recSeqNorecSeqNo
	 */
	public Long getRecSeqNo() {
		return recSeqNo;
	}

	/**
	 * @param recSeqNo
	 *            the recSeqNo to set
	 */
	public void setRecSeqNo(Long recSeqNo) {
		this.recSeqNo = recSeqNo;
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
	 * @return the boxIdboxId
	 */
	public String getBoxId() {
		return boxId;
	}

	/**
	 * @param boxId
	 *            the boxId to set
	 */
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * @return the idid
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the m_stRoadNamem_stRoadName
	 */
	public String getM_stRoadName() {
		return m_stRoadName;
	}

	/**
	 * @param m_stRoadName
	 *            the m_stRoadName to set
	 */
	public void setM_stRoadName(String m_stRoadName) {
		this.m_stRoadName = m_stRoadName;
	}

	/**
	 * @return the beginLatitudebeginLatitude
	 */
	public Float getBeginLatitude() {
		return beginLatitude;
	}

	/**
	 * @param beginLatitude
	 *            the beginLatitude to set
	 */
	public void setBeginLatitude(Float beginLatitude) {
		this.beginLatitude = beginLatitude;
	}

	/**
	 * @return the courseLatitudecourseLatitude
	 */
	public Float getCourseLatitude() {
		return courseLatitude;
	}

	/**
	 * @param courseLatitude
	 *            the courseLatitude to set
	 */
	public void setCourseLatitude(Float courseLatitude) {
		this.courseLatitude = courseLatitude;
	}

	/**
	 * @return the endLatitudeendLatitude
	 */
	public Float getEndLatitude() {
		return endLatitude;
	}

	/**
	 * @param endLatitude
	 *            the endLatitude to set
	 */
	public void setEndLatitude(Float endLatitude) {
		this.endLatitude = endLatitude;
	}

	/**
	 * @return the beginLongitudebeginLongitude
	 */
	public Float getBeginLongitude() {
		return beginLongitude;
	}

	/**
	 * @param beginLongitude
	 *            the beginLongitude to set
	 */
	public void setBeginLongitude(Float beginLongitude) {
		this.beginLongitude = beginLongitude;
	}

	/**
	 * @return the courseLongitudecourseLongitude
	 */
	public Float getCourseLongitude() {
		return courseLongitude;
	}

	/**
	 * @param courseLongitude
	 *            the courseLongitude to set
	 */
	public void setCourseLongitude(Float courseLongitude) {
		this.courseLongitude = courseLongitude;
	}

	/**
	 * @return the endLongitudeendLongitude
	 */
	public Float getEndLongitude() {
		return endLongitude;
	}

	/**
	 * @param endLongitude
	 *            the endLongitude to set
	 */
	public void setEndLongitude(Float endLongitude) {
		this.endLongitude = endLongitude;
	}

	/**
	 * @return the m_dbDistancem_dbDistance
	 */
	public Float getM_dbDistance() {
		return m_dbDistance;
	}

	/**
	 * @param m_dbDistance
	 *            the m_dbDistance to set
	 */
	public void setM_dbDistance(Float m_dbDistance) {
		this.m_dbDistance = m_dbDistance;
	}

	/**
	 * @return the m_dbContinueTimem_dbContinueTime
	 */
	public Long getM_dbContinueTime() {
		return m_dbContinueTime;
	}

	/**
	 * @param m_dbContinueTime
	 *            the m_dbContinueTime to set
	 */
	public void setM_dbContinueTime(Long m_dbContinueTime) {
		this.m_dbContinueTime = m_dbContinueTime;
	}

	/**
	 * @return the compareRecSeqNocompareRecSeqNo
	 */
	public Long getCompareRecSeqNo() {
		return compareRecSeqNo;
	}

	/**
	 * @param compareRecSeqNo
	 *            the compareRecSeqNo to set
	 */
	public void setCompareRecSeqNo(Long compareRecSeqNo) {
		this.compareRecSeqNo = compareRecSeqNo;
	}

	/**
	 * @return the compareFileNamecompareFileName
	 */
	public String getCompareFileName() {
		return compareFileName;
	}

	/**
	 * @param compareFileName
	 *            the compareFileName to set
	 */
	public void setCompareFileName(String compareFileName) {
		this.compareFileName = compareFileName;
	}

	/**
	 * @return the compareBoxIdcompareBoxId
	 */
	public String getCompareBoxId() {
		return compareBoxId;
	}

	/**
	 * @param compareBoxId
	 *            the compareBoxId to set
	 */
	public void setCompareBoxId(String compareBoxId) {
		this.compareBoxId = compareBoxId;
	}

	/**
	 * @return the compareIdcompareId
	 */
	public Long getCompareId() {
		return compareId;
	}

	/**
	 * @param compareId
	 *            the compareId to set
	 */
	public void setCompareId(Long compareId) {
		this.compareId = compareId;
	}

	/**
	 * @return the compareM_stRoadNamecompareM_stRoadName
	 */
	public String getCompareM_stRoadName() {
		return compareM_stRoadName;
	}

	/**
	 * @param compareM_stRoadName
	 *            the compareM_stRoadName to set
	 */
	public void setCompareM_stRoadName(String compareM_stRoadName) {
		this.compareM_stRoadName = compareM_stRoadName;
	}

	/**
	 * @return the compareBeginLatitudecompareBeginLatitude
	 */
	public Float getCompareBeginLatitude() {
		return compareBeginLatitude;
	}

	/**
	 * @param compareBeginLatitude
	 *            the compareBeginLatitude to set
	 */
	public void setCompareBeginLatitude(Float compareBeginLatitude) {
		this.compareBeginLatitude = compareBeginLatitude;
	}

	/**
	 * @return the compareCourseLatitudecompareCourseLatitude
	 */
	public Float getCompareCourseLatitude() {
		return compareCourseLatitude;
	}

	/**
	 * @param compareCourseLatitude
	 *            the compareCourseLatitude to set
	 */
	public void setCompareCourseLatitude(Float compareCourseLatitude) {
		this.compareCourseLatitude = compareCourseLatitude;
	}

	/**
	 * @return the compareEndLatitudecompareEndLatitude
	 */
	public Float getCompareEndLatitude() {
		return compareEndLatitude;
	}

	/**
	 * @param compareEndLatitude
	 *            the compareEndLatitude to set
	 */
	public void setCompareEndLatitude(Float compareEndLatitude) {
		this.compareEndLatitude = compareEndLatitude;
	}

	/**
	 * @return the compareBeginLongitudecompareBeginLongitude
	 */
	public Float getCompareBeginLongitude() {
		return compareBeginLongitude;
	}

	/**
	 * @param compareBeginLongitude
	 *            the compareBeginLongitude to set
	 */
	public void setCompareBeginLongitude(Float compareBeginLongitude) {
		this.compareBeginLongitude = compareBeginLongitude;
	}

	/**
	 * @return the compareCourseLongitudecompareCourseLongitude
	 */
	public Float getCompareCourseLongitude() {
		return compareCourseLongitude;
	}

	/**
	 * @param compareCourseLongitude
	 *            the compareCourseLongitude to set
	 */
	public void setCompareCourseLongitude(Float compareCourseLongitude) {
		this.compareCourseLongitude = compareCourseLongitude;
	}

	/**
	 * @return the compareEndLongitudecompareEndLongitude
	 */
	public Float getCompareEndLongitude() {
		return compareEndLongitude;
	}

	/**
	 * @param compareEndLongitude
	 *            the compareEndLongitude to set
	 */
	public void setCompareEndLongitude(Float compareEndLongitude) {
		this.compareEndLongitude = compareEndLongitude;
	}

	/**
	 * @return the compareM_dbDistancecompareM_dbDistance
	 */
	public Float getCompareM_dbDistance() {
		return compareM_dbDistance;
	}

	/**
	 * @param compareM_dbDistance
	 *            the compareM_dbDistance to set
	 */
	public void setCompareM_dbDistance(Float compareM_dbDistance) {
		this.compareM_dbDistance = compareM_dbDistance;
	}

	/**
	 * @return the compareM_dbContinueTimecompareM_dbContinueTime
	 */
	public Long getCompareM_dbContinueTime() {
		return compareM_dbContinueTime;
	}

	/**
	 * @param compareM_dbContinueTime
	 *            the compareM_dbContinueTime to set
	 */
	public void setCompareM_dbContinueTime(Long compareM_dbContinueTime) {
		this.compareM_dbContinueTime = compareM_dbContinueTime;
	}

	/**
	 * @return the compareIdscompareIds
	 */
	public String getCompareIds() {
		return compareIds;
	}

	/**
	 * @param compareIds
	 *            the compareIds to set
	 */
	public void setCompareIds(String compareIds) {
		this.compareIds = compareIds;
	}

	/**
	 * @return the mosBadLatitudemosBadLatitude
	 */
	public Float getMosBadLatitude() {
		return mosBadLatitude;
	}

	/**
	 * @param mosBadLatitude
	 *            the mosBadLatitude to set
	 */
	public void setMosBadLatitude(Float mosBadLatitude) {
		this.mosBadLatitude = mosBadLatitude;
	}

	/**
	 * @return the mosBadLongitudemosBadLongitude
	 */
	public Float getMosBadLongitude() {
		return mosBadLongitude;
	}

	/**
	 * @param mosBadLongitude
	 *            the mosBadLongitude to set
	 */
	public void setMosBadLongitude(Float mosBadLongitude) {
		this.mosBadLongitude = mosBadLongitude;
	}

	/**
	 * @return the qbrTypeqbrType
	 */
	public Integer getQbrType() {
		return qbrType;
	}

	/**
	 * @param qbrType
	 *            the qbrType to set
	 */
	public void setQbrType(Integer qbrType) {
		this.qbrType = qbrType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((beginLatitude == null) ? 0 : beginLatitude.hashCode());
		result = prime * result
				+ ((beginLongitude == null) ? 0 : beginLongitude.hashCode());
		result = prime * result + ((boxId == null) ? 0 : boxId.hashCode());
		result = prime
				* result
				+ ((compareBeginLatitude == null) ? 0 : compareBeginLatitude
						.hashCode());
		result = prime
				* result
				+ ((compareBeginLongitude == null) ? 0 : compareBeginLongitude
						.hashCode());
		result = prime * result
				+ ((compareBoxId == null) ? 0 : compareBoxId.hashCode());
		result = prime
				* result
				+ ((compareCourseLatitude == null) ? 0 : compareCourseLatitude
						.hashCode());
		result = prime
				* result
				+ ((compareCourseLongitude == null) ? 0
						: compareCourseLongitude.hashCode());
		result = prime
				* result
				+ ((compareEndLatitude == null) ? 0 : compareEndLatitude
						.hashCode());
		result = prime
				* result
				+ ((compareEndLongitude == null) ? 0 : compareEndLongitude
						.hashCode());
		result = prime * result
				+ ((compareFileName == null) ? 0 : compareFileName.hashCode());
		result = prime * result
				+ ((compareId == null) ? 0 : compareId.hashCode());
		result = prime * result
				+ ((compareIds == null) ? 0 : compareIds.hashCode());
		result = prime
				* result
				+ ((compareM_dbContinueTime == null) ? 0
						: compareM_dbContinueTime.hashCode());
		result = prime
				* result
				+ ((compareM_dbDistance == null) ? 0 : compareM_dbDistance
						.hashCode());
		result = prime
				* result
				+ ((compareM_stRoadName == null) ? 0 : compareM_stRoadName
						.hashCode());
		result = prime * result
				+ ((compareRecSeqNo == null) ? 0 : compareRecSeqNo.hashCode());
		result = prime * result
				+ ((courseLatitude == null) ? 0 : courseLatitude.hashCode());
		result = prime * result
				+ ((courseLongitude == null) ? 0 : courseLongitude.hashCode());
		result = prime * result
				+ ((endLatitude == null) ? 0 : endLatitude.hashCode());
		result = prime * result
				+ ((endLongitude == null) ? 0 : endLongitude.hashCode());
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((m_dbContinueTime == null) ? 0 : m_dbContinueTime.hashCode());
		result = prime * result
				+ ((m_dbDistance == null) ? 0 : m_dbDistance.hashCode());
		result = prime * result
				+ ((m_stRoadName == null) ? 0 : m_stRoadName.hashCode());
		result = prime * result
				+ ((mosBadLatitude == null) ? 0 : mosBadLatitude.hashCode());
		result = prime * result
				+ ((mosBadLongitude == null) ? 0 : mosBadLongitude.hashCode());
		result = prime * result + ((qbrType == null) ? 0 : qbrType.hashCode());
		result = prime * result
				+ ((recSeqNo == null) ? 0 : recSeqNo.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MosBadRoadResponseBean other = (MosBadRoadResponseBean) obj;
		if (beginLatitude == null) {
			if (other.beginLatitude != null)
				return false;
		} else if (!beginLatitude.equals(other.beginLatitude))
			return false;
		if (beginLongitude == null) {
			if (other.beginLongitude != null)
				return false;
		} else if (!beginLongitude.equals(other.beginLongitude))
			return false;
		if (boxId == null) {
			if (other.boxId != null)
				return false;
		} else if (!boxId.equals(other.boxId))
			return false;
		if (compareBeginLatitude == null) {
			if (other.compareBeginLatitude != null)
				return false;
		} else if (!compareBeginLatitude.equals(other.compareBeginLatitude))
			return false;
		if (compareBeginLongitude == null) {
			if (other.compareBeginLongitude != null)
				return false;
		} else if (!compareBeginLongitude.equals(other.compareBeginLongitude))
			return false;
		if (compareBoxId == null) {
			if (other.compareBoxId != null)
				return false;
		} else if (!compareBoxId.equals(other.compareBoxId))
			return false;
		if (compareCourseLatitude == null) {
			if (other.compareCourseLatitude != null)
				return false;
		} else if (!compareCourseLatitude.equals(other.compareCourseLatitude))
			return false;
		if (compareCourseLongitude == null) {
			if (other.compareCourseLongitude != null)
				return false;
		} else if (!compareCourseLongitude.equals(other.compareCourseLongitude))
			return false;
		if (compareEndLatitude == null) {
			if (other.compareEndLatitude != null)
				return false;
		} else if (!compareEndLatitude.equals(other.compareEndLatitude))
			return false;
		if (compareEndLongitude == null) {
			if (other.compareEndLongitude != null)
				return false;
		} else if (!compareEndLongitude.equals(other.compareEndLongitude))
			return false;
		if (compareFileName == null) {
			if (other.compareFileName != null)
				return false;
		} else if (!compareFileName.equals(other.compareFileName))
			return false;
		if (compareId == null) {
			if (other.compareId != null)
				return false;
		} else if (!compareId.equals(other.compareId))
			return false;
		if (compareIds == null) {
			if (other.compareIds != null)
				return false;
		} else if (!compareIds.equals(other.compareIds))
			return false;
		if (compareM_dbContinueTime == null) {
			if (other.compareM_dbContinueTime != null)
				return false;
		} else if (!compareM_dbContinueTime
				.equals(other.compareM_dbContinueTime))
			return false;
		if (compareM_dbDistance == null) {
			if (other.compareM_dbDistance != null)
				return false;
		} else if (!compareM_dbDistance.equals(other.compareM_dbDistance))
			return false;
		if (compareM_stRoadName == null) {
			if (other.compareM_stRoadName != null)
				return false;
		} else if (!compareM_stRoadName.equals(other.compareM_stRoadName))
			return false;
		if (compareRecSeqNo == null) {
			if (other.compareRecSeqNo != null)
				return false;
		} else if (!compareRecSeqNo.equals(other.compareRecSeqNo))
			return false;
		if (courseLatitude == null) {
			if (other.courseLatitude != null)
				return false;
		} else if (!courseLatitude.equals(other.courseLatitude))
			return false;
		if (courseLongitude == null) {
			if (other.courseLongitude != null)
				return false;
		} else if (!courseLongitude.equals(other.courseLongitude))
			return false;
		if (endLatitude == null) {
			if (other.endLatitude != null)
				return false;
		} else if (!endLatitude.equals(other.endLatitude))
			return false;
		if (endLongitude == null) {
			if (other.endLongitude != null)
				return false;
		} else if (!endLongitude.equals(other.endLongitude))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (m_dbContinueTime == null) {
			if (other.m_dbContinueTime != null)
				return false;
		} else if (!m_dbContinueTime.equals(other.m_dbContinueTime))
			return false;
		if (m_dbDistance == null) {
			if (other.m_dbDistance != null)
				return false;
		} else if (!m_dbDistance.equals(other.m_dbDistance))
			return false;
		if (m_stRoadName == null) {
			if (other.m_stRoadName != null)
				return false;
		} else if (!m_stRoadName.equals(other.m_stRoadName))
			return false;
		if (mosBadLatitude == null) {
			if (other.mosBadLatitude != null)
				return false;
		} else if (!mosBadLatitude.equals(other.mosBadLatitude))
			return false;
		if (mosBadLongitude == null) {
			if (other.mosBadLongitude != null)
				return false;
		} else if (!mosBadLongitude.equals(other.mosBadLongitude))
			return false;
		if (qbrType == null) {
			if (other.qbrType != null)
				return false;
		} else if (!qbrType.equals(other.qbrType))
			return false;
		if (recSeqNo == null) {
			if (other.recSeqNo != null)
				return false;
		} else if (!recSeqNo.equals(other.recSeqNo))
			return false;
		return true;
	}

}
