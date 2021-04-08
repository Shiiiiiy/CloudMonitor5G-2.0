/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.testLogItem.TestLogItem;

/**
 * 邻区缺失问题路段----邻区缺失的服务小区切换性能
 * 
 * @author yinzhipeng
 * @date:2015年9月11日 下午4:06:38
 * @version
 */
@Entity
@Table(name = "IADS_TESTLOG_NB_DEF_CO_PERF")
public class NbCellDeficiencyCoPerf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2342848020250117294L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(邻区缺失问题路段),所属的测试日志
	 */
	private TestLogItem testLogItem;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * 切换目标小区友好名
	 */
	private String co_cellName;
	/**
	 * 切换目标小区CELLID
	 */
	private Long co_cellId;
	/**
	 * 距离(m),服务小区与切换目标小区的距离
	 */
	private Float co_distance;
	/**
	 * 切换尝试次数
	 */
	private Long co_requestNum;
	/**
	 * 切换成功次数
	 */
	private Long co_successNum;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
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
	 * @return the testLogItemtestLogItem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECSEQNO")
	@JSON(serialize = false)
	public TestLogItem getTestLogItem() {
		return testLogItem;
	}

	/**
	 * @param testLogItem
	 *            the testLogItem to set
	 */
	public void setTestLogItem(TestLogItem testLogItem) {
		this.testLogItem = testLogItem;
	}

	/**
	 * @return the cellNamecellName
	 */
	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param cellName
	 *            the cellName to set
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * @return the cellIdcellId
	 */
	@Column(name = "CELL_ID")
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the co_cellNameco_cellName
	 */
	@Column(name = "CO_CELL_NAME")
	public String getCo_cellName() {
		return co_cellName;
	}

	/**
	 * @param co_cellName
	 *            the co_cellName to set
	 */
	public void setCo_cellName(String co_cellName) {
		this.co_cellName = co_cellName;
	}

	/**
	 * @return the co_cellIdco_cellId
	 */
	@Column(name = "CO_CELL_ID")
	public Long getCo_cellId() {
		return co_cellId;
	}

	/**
	 * @param co_cellId
	 *            the co_cellId to set
	 */
	public void setCo_cellId(Long co_cellId) {
		this.co_cellId = co_cellId;
	}

	/**
	 * @return the co_distanceco_distance
	 */
	@Column(name = "CO_DISTANCE")
	public Float getCo_distance() {
		return co_distance;
	}

	/**
	 * @param co_distance
	 *            the co_distance to set
	 */
	public void setCo_distance(Float co_distance) {
		this.co_distance = co_distance;
	}

	/**
	 * @return the co_requestNumco_requestNum
	 */
	@Column(name = "CO_REQUEST_NUM")
	public Long getCo_requestNum() {
		return co_requestNum;
	}

	/**
	 * @param co_requestNum
	 *            the co_requestNum to set
	 */
	public void setCo_requestNum(Long co_requestNum) {
		this.co_requestNum = co_requestNum;
	}

	/**
	 * @return the co_successNumco_successNum
	 */
	@Column(name = "CO_SUCCESS_NUM")
	public Long getCo_successNum() {
		return co_successNum;
	}

	/**
	 * @param co_successNum
	 *            the co_successNum to set
	 */
	public void setCo_successNum(Long co_successNum) {
		this.co_successNum = co_successNum;
	}

}
