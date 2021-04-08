package com.datang.domain.testPlan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.datang.util.DateUtil;
import com.datang.web.beans.testPlan.DateBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:41:46
 * @version
 */
@Entity
@Table(name = "IADS_TP_TEST_SCHEME")
@SuppressWarnings("all")
@XStreamAlias("TestScheme")
public class TestScheme implements Serializable {
	/**
	 * 名称
	 */
	@XStreamOmitField
	private String name = "缺省任务" + DateUtil.getCurDateStr("yyyyMMddHHmmss");
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	/**
	 * 是否执行
	 */
	@XStreamAlias("Enable")
	private String enable;
	/**
	 * 测试命令序列的执行类型
	 */
	@XStreamAlias("Etype")
	private String etype;
	/**
	 * 测试计划简单描述
	 */
	@XStreamAlias("DESC")
	private String desc;
	/**
	 * 说明执行本测试方案的模块号码
	 */
	@XStreamAlias("MSNO")
	private String msNo;

	@XStreamAlias("ModelLock")
	private String modeLock;
	/**
	 * 命令执行是否受时间限制
	 */
	@XStreamAlias("TimeCondition")
	private String timeCondition;
	/**
	 * 按照年月日选择执行的日期，中间用逗号隔开
	 */
	@XStreamAlias("ExecutiveDate")
	private String executiveDate;
	/**
	 * 执行的时间段
	 */
	@XStreamImplicit(itemFieldName = "Time")
	private List<Time> times = new ArrayList<Time>();
	@XStreamAlias("CommandList")
	private CommandList commandList = new CommandList();

	@XStreamOmitField
	private List<DateBean> dateBeans = new ArrayList<DateBean>();
	/**
	 * 执行序号
	 */
	@XStreamOmitField
	private Integer runOrder;
	
	/**
	 * 区域限制条件,测试方案是否受GPS条件限制,0 不受GPS条件限制,1 受GPS条件限制
	 */
	@XStreamOmitField
	private Integer gpsCondition;
	
	/**
	 * 左上角经度
	 */
	@XStreamOmitField
	private Float leftTopLon;
	
	/**
	 * 左上角纬度
	 */
	@XStreamOmitField
	private Float leftTopLat;
	
	/**
	 * 右下角经度
	 */
	@XStreamOmitField
	private Float rightBottomLon;
	
	/**
	 * 右下角纬度
	 */
	@XStreamOmitField
	private Float rightBottomLat;

	/**
	 * @return the enable
	 */
	@Column(name = "P_ENABLE")
	public String getEnable() {
		return enable;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(String enable) {
		this.enable = enable;
	}

	/**
	 * @return the desc
	 */
	@Column(name = "P_DESC")
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the msNo
	 */
	@Column(name = "MS_NO")
	public String getMsNo() {
		return msNo;
	}

	/**
	 * @param msNo
	 *            the msNo to set
	 */
	public void setMsNo(String msNo) {
		this.msNo = msNo;
	}

	/**
	 * @return the timeCondition
	 */
	@Column(name = "TIME_CONDITION")
	public String getTimeCondition() {
		return timeCondition;
	}

	/**
	 * @param timeCondition
	 *            the timeCondition to set
	 */
	public void setTimeCondition(String timeCondition) {
		this.timeCondition = timeCondition;
	}

	/**
	 * @return the executiveDate
	 */
	@Column(name = "EXECUTIVE_DATE", length = 4000)
	public String getExecutiveDate() {
		return executiveDate;
	}

	/**
	 * @param executiveDate
	 *            the executiveDate to set
	 */
	public void setExecutiveDate(String executiveDate) {
		this.executiveDate = executiveDate;
	}

	/**
	 * @return the time
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "TEST_SCHEME_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@Fetch(FetchMode.SUBSELECT)
	public List<Time> getTimes() {
		return times;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	private void setTimes(List<Time> times) {
		this.times = times;
	}

	public void initTimes(List<Time> times) {
		if (times == null && this.getTimes() != null) {
			this.getTimes().clear();
		}
		if (times != null) {
			this.getTimes().clear();
			for (Time time : times) {
				this.addTime(time);
			}
		}
	}

	@Transient
	public void addTime(Time time) {
		this.getTimes().add(time);
	}

	/**
	 * @return the commandList
	 */
	@OneToOne
	@JoinColumn(name = "COMMAND_LIST_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	public CommandList getCommandList() {
		return commandList;
	}

	/**
	 * @param commandList
	 *            the commandList to set
	 */
	public void setCommandList(CommandList commandList) {
		this.commandList = commandList;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public List<DateBean> getDateBeans() {
		return dateBeans;
	}

	public void setDateBeans(List<DateBean> dateBeans) {
		this.dateBeans = dateBeans;
	}

	/**
	 * 查找命令对象 怎么查找的???
	 * 
	 * @param name
	 *            String
	 * @return Command
	 */
	public Command findCommand(String name) {
		for (Command tmpCommand : this.commandList.getSynchronize()
				.getCommands()) {// 命令列表破,,,好像没有去查找,而是得到这个集合,拿着这个集合进行对比,是这样查找的
			if (tmpCommand.getName().equals(name)) {// 拿着这个集合里的一个值也传过来的这个值进行对比,
				return tmpCommand;// 返回相等的这个值

			}
		}
		return null;// 如果这个集合中没有值返回空
	}

	/**
	 * 添加command命令
	 * 
	 * @param command
	 *            Command
	 */
	public void addCommand(Command command) {
		this.commandList.getSynchronize().getCommands().add(command);
	}

	/**
	 * @return the modeLock
	 */
	@Column(name = "MODE_LOCK")
	public String getModeLock() {
		return modeLock;
	}

	/**
	 * @param modeLock
	 *            the modeLock to set
	 */
	public void setModeLock(String modeLock) {
		this.modeLock = modeLock;
	}

	@Column(name = "ETYPE")
	public String getEtype() {
		return etype;
	}

	public void setEtype(String etype) {
		// if(null == this.etype) this.etype = "0";
		// else
		this.etype = etype;
	}

	/**
	 * @return the runOrderrunOrder
	 */
	@Column(name = "RUN_ORDER")
	public Integer getRunOrder() {
		return runOrder;
	}

	/**
	 * @param runOrder
	 *            the runOrder to set
	 */
	public void setRunOrder(Integer runOrder) {
		this.runOrder = runOrder;
	}

	/**
	 * @return the gpsCondition
	 */
	@Column(name = "GPS_CONDITION")
	public Integer getGpsCondition() {
		return gpsCondition;
	}

	/**
	 * @param gpsCondition the gpsCondition to set
	 */
	public void setGpsCondition(Integer gpsCondition) {
		this.gpsCondition = gpsCondition;
	}

	/**
	 * @return the leftTopLon
	 */
	@Column(name = "LEFT_TOP_LON")
	public Float getLeftTopLon() {
		return leftTopLon;
	}

	/**
	 * @param leftTopLon the leftTopLon to set
	 */
	public void setLeftTopLon(Float leftTopLon) {
		this.leftTopLon = leftTopLon;
	}

	/**
	 * @return the leftTopLat
	 */
	@Column(name = "LEFT_TOP_LAT")
	public Float getLeftTopLat() {
		return leftTopLat;
	}

	/**
	 * @param leftTopLat the leftTopLat to set
	 */
	public void setLeftTopLat(Float leftTopLat) {
		this.leftTopLat = leftTopLat;
	}

	/**
	 * @return the rightBottomLon
	 */
	@Column(name = "RIGHT_BOTTOM_LON")
	public Float getRightBottomLon() {
		return rightBottomLon;
	}

	/**
	 * @param rightBottomLon the rightBottomLon to set
	 */
	public void setRightBottomLon(Float rightBottomLon) {
		this.rightBottomLon = rightBottomLon;
	}

	/**
	 * @return the rightBottomLat
	 */
	@Column(name = "RIGHT_BOTTOM_LAT")
	public Float getRightBottomLat() {
		return rightBottomLat;
	}

	/**
	 * @param rightBottomLat the rightBottomLat to set
	 */
	public void setRightBottomLat(Float rightBottomLat) {
		this.rightBottomLat = rightBottomLat;
	}
	
	

}
