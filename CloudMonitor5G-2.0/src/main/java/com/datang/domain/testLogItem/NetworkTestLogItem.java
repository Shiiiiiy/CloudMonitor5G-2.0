package com.datang.domain.testLogItem;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;

/**
 * 网络侧日志文件信息
 * 
 * @author yinzhipeng
 * @date:2017年2月6日 上午11:20:13
 * @version
 */
@Entity
@Table(name = "IADS_NETWORK_TESTLOG_ITEM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class NetworkTestLogItem implements Serializable,
		Comparable<NetworkTestLogItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 205481782337069503L;
	/**
	 * 主键
	 */
	private Long recSeqNo;

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 网络侧文件类型（S1，SGI等）<br>
	 * 0:S1<br>
	 * 1:SGI<br>
	 */
	private Integer testFileType;

	/**
	 * box-id
	 */
	private String boxId;

	/**
	 * 模块号
	 */
	private String moduleNo;

	/**
	 * 日志开始和结束时间
	 */
	private Date startDate;
	private Date endDate;
	private Long startDateLong;
	private Long endDateLong;

	/**
	 * 上传文件链接
	 */
	private String filelink;

	/**
	 * 该字段存储总明文字节数
	 */
	private Long uploadedSize;

	/**
	 * 终端
	 */
	private Terminal terminal;

	/**
	 * 测试分组，地级域
	 */
	private String terminalGroup;

	/**
	 * 路测文件状态（已解析，未解析等）<br>
	 * 0:上传中,未解析 <br>
	 * 1:上传成功,解析中 <br>
	 * 2:上传成功,已解析成功 <br>
	 * 其他:上传成功,解析失败
	 */
	private Integer testFileStatus = 0;

	/**
	 * 已经上传文件的总大小
	 */
	private Long ftpByteSize;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name = "RECSEQNO")
	public Long getRecSeqNo() {
		return recSeqNo;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setRecSeqNo(Long recSeqNo) {
		this.recSeqNo = recSeqNo;
	}

	/**
	 * @return the boxId
	 */
	@Column(name = "BOX_ID")
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
	 * @return the endDate
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
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
	 * @return the fileName
	 */
	@Column(name = "FILE_NAME")
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
	 * @return the filelink
	 */
	@Column(name = "FILE_LINK")
	public String getFilelink() {
		return filelink;
	}

	/**
	 * @param filelink
	 *            the filelink to set
	 */
	public void setFilelink(String filelink) {
		this.filelink = filelink;
	}

	/**
	 * @return the moduleNo
	 */
	@Column(name = "MODULE_NO")
	public String getModuleNo() {
		return moduleNo;
	}

	/**
	 * @param moduleNo
	 *            the moduleNo to set
	 */
	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}

	/**
	 * @return the startDate
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the terminalGroup
	 */
	// @Column(name = "TERMINAL_GROUP")
	@Transient
	public String getTerminalGroup() {
		if (null != terminal) {
			TerminalGroup terminalGroup2 = terminal.getTerminalGroup();
			if (null != terminalGroup2) {
				this.terminalGroup = terminalGroup2.getName();
			}
		}
		return terminalGroup;
	}

	/**
	 * @param terminalGroup
	 *            the terminalGroup to set
	 */
	public void setTerminalGroup(String terminalGroup) {
		this.terminalGroup = terminalGroup;
	}

	/**
	 * @return the testFileStatus
	 */
	@Column(name = "TESTFILE_STATUS")
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
	 * @return the uploadSize
	 */
	@Column(name = "UPLOADED_SIZE")
	public Long getUploadedSize() {
		return uploadedSize;
	}

	/**
	 * @param uploadSize
	 *            the uploadSize to set
	 */
	public void setUploadedSize(Long uploadedSize) {
		this.uploadedSize = uploadedSize;
	}

	/**
	 * @return the terminal
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BOX_ID", referencedColumnName = "BOX_ID", insertable = false, updatable = false)
	@JSON(serialize = false)
	public Terminal getTerminal() {
		return terminal;
	}

	/**
	 * @param terminal
	 *            the terminal to set
	 */
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	/**
	 * @return the ftpByteSize
	 */
	@Column(name = "FTP_BYTESIZE")
	public Long getFtpByteSize() {
		return ftpByteSize;
	}

	/**
	 * @param ftpByteSize
	 *            the ftpByteSize to set
	 */
	public void setFtpByteSize(Long ftpByteSize) {
		this.ftpByteSize = ftpByteSize;
	}

	/**
	 * @return the startDateLongstartDateLong
	 */
	@Column(name = "START_DATE")
	public Long getStartDateLong() {
		return startDateLong;
	}

	/**
	 * @param startDateLong
	 *            the startDateLong to set
	 */
	public void setStartDateLong(Long startDateLong) {
		this.startDateLong = startDateLong;
		if (null != startDateLong) {
			this.startDate = new Date(startDateLong);
		}

	}

	/**
	 * @return the endDateLongendDateLong
	 */
	@Column(name = "END_DATE")
	public Long getEndDateLong() {
		return endDateLong;
	}

	/**
	 * @param endDateLong
	 *            the endDateLong to set
	 */
	public void setEndDateLong(Long endDateLong) {
		this.endDateLong = endDateLong;
		if (null != endDateLong && 0l != endDateLong) {
			this.endDate = new Date(endDateLong);
		}
	}

	/**
	 * @return the testFileTypetestFileType
	 */
	@Column(name = "FILE_TYPE")
	public Integer getTestFileType() {
		return testFileType;
	}

	/**
	 * @param testFileType
	 *            the testFileType to set
	 */
	public void setTestFileType(Integer testFileType) {
		this.testFileType = testFileType;
	}

	@Override
	public int compareTo(NetworkTestLogItem o) {
		if (o.getStartDate() != null && this.getStartDate() != null) {
			return Math.round((o.getStartDate().getTime() - this.getStartDate()
					.getTime()) / 86400000f) + 1;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NetworkTestLogItem [recSeqNo=" + recSeqNo + ", fileName="
				+ fileName + ", testFileType=" + testFileType + ", boxId="
				+ boxId + ", moduleNo=" + moduleNo + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", startDateLong=" + startDateLong
				+ ", endDateLong=" + endDateLong + ", filelink=" + filelink
				+ ", uploadedSize=" + uploadedSize + ", terminal=" + terminal
				+ ", terminalGroup=" + terminalGroup + ", testFileStatus="
				+ testFileStatus + ", ftpByteSize=" + ftpByteSize + "]";
	}

}
