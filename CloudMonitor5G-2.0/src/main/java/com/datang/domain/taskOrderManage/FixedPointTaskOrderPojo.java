package com.datang.domain.taskOrderManage;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.testManage.terminal.Terminal;
/**
 * 定点测试任务工单列表
 * @author lucheng
 *
 */
@Entity
@Table(name = "IADS_FIXEDPODIN_TASK")
public class FixedPointTaskOrderPojo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	/**
	 * 工单编号
	 */
	private String workOrderId;
	
	/**
	 * 要求占用NR小区名称
	 */
	private String occupyNrCellName;
	
	/**
	 * 要求占用LTE小区名称
	 */
	private String occupyLteCellName;
	
	/**
	 * 测试点经度
	 */
	private String testPointLon;
	
	/**
	 * 测试点纬度
	 */
	private String testPointLat;
	
	/**
	 * 测试位置偏离（米）
	 */
	private Long testLocationSkewing;
	
	/**
	 * 测试时段
	 */
	private String testDate;
	
	/**
	 * 终端ID
	 */
	private String boxId;
	
	/**
	 * 区域
	 */
	private String region;
	
	/**
	 * 任务发起人
	 */
	private String taskInitiator;
	
	/**
	 * 任务发起时间
	 */
	private Date taskCreatTime;
	
	/**
	 * 任务时限(小时)
	 */
	private String taskTimeLimit;
	
	/**
	 * 工单状态
	 */
	private String workOrderState;
	
	/**
	 * 备注
	 */
	private String comment;
	
	/**
	 * 操作类型
	 */
	private String operateType;
	
	/**
	 * 文件保存路径
	 */
	private String filePath;
	
	/**
	 * 设备
	 */
	private Terminal terminal;
	
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the workOrderId
	 */
	@Column(name = "WPRK_ORDER_ID")
	public String getWorkOrderId() {
		return workOrderId;
	}
	
	/**
	 * @param workOrderId the workOrderId to set
	 */
	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
	
	/**
	 * @return the occupyNrCellName
	 */
	@Column(name = "OCCUPY_NR_CELL_NAME")
	public String getOccupyNrCellName() {
		return occupyNrCellName;
	}
	
	/**
	 * @param occupyNrCellName the occupyNrCellName to set
	 */
	public void setOccupyNrCellName(String occupyNrCellName) {
		this.occupyNrCellName = occupyNrCellName;
	}
	
	/**
	 * @return the occupyLteCellName
	 */
	@Column(name = "OCCUPY_LTE_CELL_NAME")
	public String getOccupyLteCellName() {
		return occupyLteCellName;
	}
	
	/**
	 * @param occupyLteCellName the occupyLteCellName to set
	 */
	public void setOccupyLteCellName(String occupyLteCellName) {
		this.occupyLteCellName = occupyLteCellName;
	}
	
	/**
	 * @return the testPointLon
	 */
	@Column(name = "TEST_POINT_LON")
	public String getTestPointLon() {
		return testPointLon;
	}
	
	/**
	 * @param testPointLon the testPointLon to set
	 */
	public void setTestPointLon(String testPointLon) {
		this.testPointLon = testPointLon;
	}
	
	/**
	 * @return the testPointLat
	 */
	@Column(name = "TEST_POINT_LAT")
	public String getTestPointLat() {
		return testPointLat;
	}
	
	/**
	 * @param testPointLat the testPointLat to set
	 */
	public void setTestPointLat(String testPointLat) {
		this.testPointLat = testPointLat;
	}
	
	/**
	 * @return the testLocationSkewing
	 */
	@Column(name = "TEST_LOCATION_SKEWING")
	public Long getTestLocationSkewing() {
		return testLocationSkewing;
	}
	
	/**
	 * @param testLocationSkewing the testLocationSkewing to set
	 */
	public void setTestLocationSkewing(Long testLocationSkewing) {
		this.testLocationSkewing = testLocationSkewing;
	}
	
	/**
	 * @return the testTime
	 */
	@Column(name = "TEST_DATE")
	public String getTestDate() {
		return testDate;
	}
	
	/**
	 * @param testTime the testTime to set
	 */
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	
	@Column(name = "BOX_ID")
	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}
	
	/**
	 * @return the region
	 */
	@Column(name = "REGION")
	public String getRegion() {
		return region;
	}
	
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @return the taskInitiator
	 */
	@Column(name = "TASK_INITIATOR")
	public String getTaskInitiator() {
		return taskInitiator;
	}
	
	/**
	 * @param taskInitiator the taskInitiator to set
	 */
	public void setTaskInitiator(String taskInitiator) {
		this.taskInitiator = taskInitiator;
	}
	
	/**
	 * @return the taskCreatTime
	 */
	@Column(name = "TASK_CREAT_TIME")
	@Temporal(TemporalType.TIMESTAMP) 
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getTaskCreatTime() {
		return taskCreatTime;
	}
	
	/**
	 * @param taskCreatTime the taskCreatTime to set
	 */
	public void setTaskCreatTime(Date taskCreatTime) {
		this.taskCreatTime = taskCreatTime;
	}
	
	/**
	 * @return the taskTimeLimit
	 */
	@Column(name = "TASK_TIME_LIMIT")
	public String getTaskTimeLimit() {
		return taskTimeLimit;
	}
	
	/**
	 * @param taskTimeLimit the taskTimeLimit to set
	 */
	public void setTaskTimeLimit(String taskTimeLimit) {
		this.taskTimeLimit = taskTimeLimit;
	}
	
	/**
	 * @return the workOrderState
	 */
	@Column(name = "WORK_ORDER_STATE")
	public String getWorkOrderState() {
		return workOrderState;
	}
	
	/**
	 * @param workOrderState the workOrderState to set
	 */
	public void setWorkOrderState(String workOrderState) {
		this.workOrderState = workOrderState;
	}
	
	/**
	 * @return the comment
	 */
	@Column(name = "COMMENTS")
	public String getComment() {
		return comment;
	}
	
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * @return the filePath
	 */
	@Column(name = "FILE_PATH")
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BOX_ID", referencedColumnName = "BOX_ID", insertable = false, updatable = false)
	@JSON(serialize = false)
	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	
	/**
	 * @return the operateType
	 */
	@Transient
	public String getOperateType() {
		return operateType;
	}
	
	/**
	 * @param operateType the operateType to set
	 */
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	
	
	
}
