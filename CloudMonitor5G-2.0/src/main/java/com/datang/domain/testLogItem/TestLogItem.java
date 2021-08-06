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
 * @author fengxueyong
 * @version 2011-3-14
 */
@Entity
@Table(name = "IADS_TESTLOG_ITEM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TestLogItem implements Serializable, Comparable<TestLogItem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键
	private Long recSeqNo;
	/**
	 * 对端日志主键
	 */
	private Long peerRecSeqNo;

	// 文件名称
	private String fileName;

	// box-id
	private String boxId;

	// 路测UEID
	private Long ueId;

	// UE的IMSI
	private Long iMSI;

	// 模块号
	private String moduleNo;

	// 文件类型(loc, log等）
	private String fileType;

	// 运营商
	private String operatorName;

	// 日志下发时间（文件名中获取）
	private Date beginDate;

	// 日志开始和结束时间
	private Date startDate;
	private Date endDate;
	private Long startDateLong;
	private Long endDateLong;

	// 0:主叫，1：被叫，2：都包含
	private Integer callType;

	// 日志路程，单位米
	private Float distance;

	// 业务类型，预留:0空闲测试,1语音测试,2VoLTE视频电话,3PDP/Attach,4Ping测试,5FTP上传/下载,6流媒体测试,7Http测试
	private String serviceType;

	// 网络制式(CDMA,WCDMA,TD等)
	private String networkStandard;

	// 上传文件链接
	private String filelink;

	/**
	 * 是否上传成功<br>
	 * 删,请参照testFileStatus
	 */
	private boolean uploadSuccess = false;

	/**
	 * 如果上传不成功，该字段存储已上传明文字节数，如果后台从没有收到eof消息， 那么该字段还是为-1，如果上传成功，那么该字节为-1；
	 */
	private Float uploadedSize;

	/**
	 * 已上传的数据包数目 <br>
	 * 删,前台无用字段
	 */
	private Integer packageNumber;

	// 终端
	private Terminal terminal;

	// 测试分组，地级域
	private String terminalGroup;

	// 测试分组，省级域
	private String provinceGroup;

	// 测试计划名
	private String testPlanName;

	/**
	 * 路测文件状态（已解析，未解析等）<br>
	 * 0:上传中,未解析 <br>
	 * 1:上传成功,解析中 <br>
	 * 2:上传成功,已解析成功 <br>
	 * 其他:上传成功,解析失败
	 */
	private Integer testFileStatus;

	// 传输密钥
	private String triDESKey;

	// 上传到FTP文件的总大小
	private Float ftpByteSize;

	private Long dtcqt;

	// 测试级别 考核测试、日常优化、设备调试
	private String testLevel;
	
	// 测试目标
	private String testTarget;
	
	// 版本号
	private String logVersion;

	// 是否拥有图片
	private Integer image;

	// 测试站点名称
	private String testNodeName;

	// 测试类型（定点=1、路测=2、空闲态=3,否则填空值或0）
	private Integer testTypeSingleenb;

	// 测试评估（1=极好点、2=好点、3=中点、4=差点）
	private Integer testEstimate;

	// 测试标示（1=S1、2=S2、3=S3）
	private Integer testSign;

	// 经度
	private Long longItude;

	// 纬度
	private Long latItude;

	// 正向反向
	private Integer forz;

	// （上传=1、下载=2、ping=3、语音=4）
	private Integer dlorul;

	// 图片路径
	private String imagePath;

	// 测试站点名
	private String testNodeName_Index;

	/**
	 * 文件来源
	 */
	private Integer logSource;

	// mos采样点数量
	private Long mosPointNum;

	// 测试日志作为服务小区的小区个数
	private Long cellSumNum;
	// VoLTE呼叫呼叫建立尝试次数
	private Long volteCallEstablishRequestNum;
	// 楼宇名称
	private String floorName;
	// 楼宇经度
	private Float floorLongItude;

	// 楼宇纬度
	private Float floorLatItude;
	
	
	// 删除标记 1：已删除 0：未删除
	private Integer deleteTag;
	
	
	//下面为后台补充增加字段
	private Long firstStpTime;
	
	private String fromMsisdn;
	
	private String toMsisdn;
	private String FILE_MD_VALUE;

	private Integer volteStatus;

	private String url;
	private String dbName;

	@Column(name = "urlname")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name = "influxtbname")
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

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
	 * @return the iMSIiMSI
	 */
	// @Column(name = "IMSI")
	@Transient
	public Long getiMSI() {
		return iMSI;
	}

	/**
	 * @param iMSI
	 *            the iMSI to set
	 */
	public void setiMSI(Long iMSI) {
		this.iMSI = iMSI;
	}

	/**
	 * @return the beginDate
	 */
	// @Column(name = "BEGIN_DATE")
	@Transient
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
	 * @return the callType
	 */
	@Column(name = "CALL_TYPE")
	public Integer getCallType() {
		return callType;
	}

	/**
	 * @param callType
	 *            the callType to set
	 */
	public void setCallType(Integer callType) {
		this.callType = callType;
	}

	/**
	 * @return the distance
	 */
	@Column(name = "DISTANCE")
	public Float getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(Float distance) {
		this.distance = distance;
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
	 * @return the fileType
	 */
	@Column(name = "FILE_TYPE")
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
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
	 * @return the networkStandard
	 */
	@Column(name = "NETWORK_STANDARD")
	public String getNetworkStandard() {
		return networkStandard;
	}

	/**
	 * @param networkStandard
	 *            the networkStandard to set
	 */
	public void setNetworkStandard(String networkStandard) {
		this.networkStandard = networkStandard;
	}

	/**
	 * @return the operatorName
	 */
	@Column(name = "OPERATOR_NAME")
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * @param operatorName
	 *            the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * @return the packageNumber
	 */
	// @Column(name = "PACKAGE_NUMBER")
	@Transient
	public Integer getPackageNumber() {
		return packageNumber;
	}

	/**
	 * @param packageNumber
	 *            the packageNumber to set
	 */
	public void setPackageNumber(Integer packageNumber) {
		this.packageNumber = packageNumber;
	}

	/**
	 * @return the provinceGroup
	 */
	// @Column(name = "PROVINCE_GROUP")
	@Transient
	public String getProvinceGroup() {
		return provinceGroup;
	}

	/**
	 * @param provinceGroup
	 *            the provinceGroup to set
	 */
	public void setProvinceGroup(String provinceGroup) {
		this.provinceGroup = provinceGroup;
	}

	/**
	 * @return the serviceType
	 */
	@Column(name = "SERVICE_TYPE")
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType
	 *            the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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
		this.beginDate = startDate;
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
				// this.provinceGroup = terminalGroup2.getName();
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
	 * @return the testPlanName
	 */
	// @Column(name = "TESTPLAN_NAME")
	@Transient
	public String getTestPlanName() {
		return testPlanName;
	}

	/**
	 * @param testPlanName
	 *            the testPlanName to set
	 */
	public void setTestPlanName(String testPlanName) {
		this.testPlanName = testPlanName;
	}

	/**
	 * @return the ueId
	 */
	// @Column(name = "UEID")
	@Transient
	public Long getUeId() {
		return ueId;
	}

	/**
	 * @param ueId
	 *            the ueId to set
	 */

	public void setUeId(Long ueId) {
		this.ueId = ueId;
	}

	/**
	 * @return the uploadSuccess
	 */
	// @Column(name = "UPLOAD_SUCCESS", columnDefinition = "boolean")
	@Transient
	public boolean isUploadSuccess() {
		return uploadSuccess;
	}

	/**
	 * @param uploadSuccess
	 *            the uploadSuccess to set
	 */
	public void setUploadSuccess(boolean uploadSuccess) {
		this.uploadSuccess = uploadSuccess;
	}

	/**
	 * @return the uploadSize
	 */
	@Column(name = "UPLOADED_SIZE")
	public Float getUploadedSize() {
		return uploadedSize;
	}

	/**
	 * @param uploadSize
	 *            the uploadSize to set
	 */
	public void setUploadedSize(Float uploadedSize) {
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
	public Float getFtpByteSize() {
		return ftpByteSize;
	}

	/**
	 * @param testLevel
	 *            the testLevel to set
	 */
	public void setTestLevel(String testLevel) {
		this.testLevel = testLevel;
	}

	/**
	 * @return the triDESKey
	 */
	@Column(name = "TRIDES_KEY")
	public String getTriDESKey() {
		return triDESKey;
	}

	/**
	 * @param triDESKey
	 *            the triDESKey to set
	 */
	public void setTriDESKey(String triDESKey) {
		this.triDESKey = triDESKey;
	}

	/**
	 * @return the testLevel
	 */
	@Column(name = "TEST_LEVEL")
	public String getTestLevel() {
		return testLevel;
	}

	/**
	 * @param ftpByteSize
	 *            the ftpByteSize to set
	 */
	public void setFtpByteSize(Float ftpByteSize) {
		this.ftpByteSize = ftpByteSize;
	}

	/**
	 * @return the testLevel
	 */
	// @Column(name = "DTCQT_TYPE")
	@Transient
	public Long getDtcqt() {
		return dtcqt;
	}

	/**
	 * @param ftpByteSize
	 *            the ftpByteSize to set
	 */
	public void setDtcqt(Long dtcqt) {
		this.dtcqt = dtcqt;
	}

	// @Column(name = "TEST_NODE_NAME")
	@Transient
	public String getTestNodeName() {
		return testNodeName;
	}

	public void setTestNodeName(String testNodeName) {
		this.testNodeName = testNodeName;
	}

	// @Column(name = "TEST_TYPE_SINGLEENB")
	@Transient
	public Integer getTestTypeSingleenb() {
		return testTypeSingleenb;
	}

	public void setTestTypeSingleenb(Integer testTypeSingleenb) {
		this.testTypeSingleenb = testTypeSingleenb;
	}

	// @Column(name = "TEST_SIGN")
	@Transient
	public Integer getTestSign() {
		return testSign;
	}

	public void setTestSign(Integer testSign) {
		this.testSign = testSign;
	}

	// @Column(name = "TEST_ESTIMATE")
	@Transient
	public Integer getTestEstimate() {
		return testEstimate;
	}

	public void setTestEstimate(Integer testEstimate) {
		this.testEstimate = testEstimate;
	}

	// @Column(name = "LONGITUDE")
	@Transient
	public Long getLongItude() {
		return longItude;
	}

	public void setLongItude(Long longItude) {
		this.longItude = longItude;
	}

	// @Column(name = "LATITUDE")
	@Transient
	public Long getLatItude() {
		return latItude;
	}

	public void setLatItude(Long latItude) {
		this.latItude = latItude;
	}

	// @Column(name = "F_OR_Z")
	@Transient
	public Integer getForz() {
		return forz;
	}

	public void setForz(Integer forz) {
		this.forz = forz;
	}

	// @Column(name = "UL_OR_DL")
	@Transient
	public Integer getDlorul() {
		return dlorul;
	}

	public void setDlorul(Integer dlorul) {
		this.dlorul = dlorul;
	}

	/**
	 * @return the image
	 */
	// @Column(name = "IS_IMAGE")
	@Transient
	public Integer getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(Integer image) {
		this.image = image;
	}

	/**
	 * @return the imagePath
	 */
	// @Column(name = "IMAGE_PATH")
	@Transient
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath
	 *            the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * @return the testNodeName_Index
	 */
	// @Column(name = "TEST_NODENAME_INDEX")
	@Transient
	public String getTestNodeName_Index() {
		return testNodeName_Index;
	}

	/**
	 * @param testNodeName_Index
	 *            the testNodeName_Index to set
	 */
	public void setTestNodeName_Index(String testNodeName_Index) {
		this.testNodeName_Index = testNodeName_Index;
	}

	/**
	 * @return the mosPointNummosPointNum
	 */
	@Column(name = "MOS_POINT_NUM")
	public Long getMosPointNum() {
		return mosPointNum;
	}

	/**
	 * @param mosPointNum
	 *            the mosPointNum to set
	 */
	public void setMosPointNum(Long mosPointNum) {
		this.mosPointNum = mosPointNum;
	}

	/**
	 * @return the cellSumNumcellSumNum
	 */
	@Column(name = "CELL_SUM_NUM")
	public Long getCellSumNum() {
		return cellSumNum;
	}

	/**
	 * @param cellSumNum
	 *            the cellSumNum to set
	 */
	public void setCellSumNum(Long cellSumNum) {
		this.cellSumNum = cellSumNum;
	}

	/**
	 * @return the logSourcelogSource
	 */
	@Column(name = "LOG_SOURCE")
	public Integer getLogSource() {
		return logSource;
	}

	/**
	 * @param logSource
	 *            the logSource to set
	 */
	public void setLogSource(Integer logSource) {
		this.logSource = logSource;
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
		if (null != startDateLong && 0l != startDateLong) {
			this.startDate = new Date(startDateLong);
			this.beginDate = new Date(startDateLong);
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

	@Override
	public int compareTo(TestLogItem o) {
		if (o.getStartDate() != null && this.getStartDate() != null) {
			return Math.round((o.getStartDate().getTime() - this.getStartDate()
					.getTime()) / 86400000f) + 1;
		}
		return 0;
	}

	/**
	 * @return the peerRecSeqNopeerRecSeqNo
	 */
	@Column(name = "PEER_RECSEQNO")
	public Long getPeerRecSeqNo() {
		return peerRecSeqNo;
	}

	/**
	 * @param peerRecSeqNo
	 *            the peerRecSeqNo to set
	 */
	public void setPeerRecSeqNo(Long peerRecSeqNo) {
		this.peerRecSeqNo = peerRecSeqNo;
	}

	/**
	 * @return the volteCallEstablishRequestNumvolteCallEstablishRequestNum
	 */
	@Column(name = "VOLTE_CALL_REQ_NUM")
	public Long getVolteCallEstablishRequestNum() {
		return volteCallEstablishRequestNum;
	}

	/**
	 * @param volteCallEstablishRequestNum
	 *            the volteCallEstablishRequestNum to set
	 */
	public void setVolteCallEstablishRequestNum(
			Long volteCallEstablishRequestNum) {
		this.volteCallEstablishRequestNum = volteCallEstablishRequestNum;
	}

	/**
	 * @return the floorName
	 */
	@Column(name = "FLOOR_NAME")
	public String getFloorName() {
		return floorName;
	}

	/**
	 * @param the
	 *            floorName to set
	 */

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	/**
	 * @return the floorLongItude
	 */
	@Column(name = "FLOOR_LONGITUDE")
	public Float getFloorLongItude() {
		return floorLongItude;
	}

	/**
	 * @param the
	 *            floorLongItude to set
	 */

	public void setFloorLongItude(Float floorLongItude) {
		this.floorLongItude = floorLongItude;
	}

	/**
	 * @return the floorLatItude
	 */
	@Column(name = "FLOOR_LATITUDE")
	public Float getFloorLatItude() {
		return floorLatItude;
	}

	/**
	 * @param the
	 *            floorLatItude to set
	 */

	public void setFloorLatItude(Float floorLatItude) {
		this.floorLatItude = floorLatItude;
	}

	/**
	 * @return deleteTag
	 */
	@Column(name = "DELETE_TAG")
	public Integer getDeleteTag() {
		return deleteTag;
	}

	public void setDeleteTag(Integer deleteTag) {
		this.deleteTag = deleteTag;
	}
	
	/**
	 * @return the logVersion
	 */
	@Column(name = "LOG_VERSION")
	public String getLogVersion() {
		return logVersion;
	}

	/**
	 * @param logVersion the logVersion to set
	 */
	public void setLogVersion(String logVersion) {
		this.logVersion = logVersion;
	}
	
	/**
	 * @return the firstStpTime
	 */
	@Column(name = "FIRST_SIP_TIME")
	public Long getFirstStpTime() {
		return firstStpTime;
	}

	/**
	 * @param firstStpTime the firstStpTime to set
	 */
	public void setFirstStpTime(Long firstStpTime) {
		this.firstStpTime = firstStpTime;
	}

	/**
	 * @return the fromMsisdn
	 */
	@Column(name = "FROM_MSISDN")
	public String getFromMsisdn() {
		return fromMsisdn;
	}

	/**
	 * @param fromMsisdn the fromMsisdn to set
	 */
	public void setFromMsisdn(String fromMsisdn) {
		this.fromMsisdn = fromMsisdn;
	}

	/**
	 * @return the toMsisdn
	 */
	@Column(name = "TO_MSISDN")
	public String getToMsisdn() {
		return toMsisdn;
	}

	/**
	 * @param toMsisdn the toMsisdn to set
	 */
	public void setToMsisdn(String toMsisdn) {
		this.toMsisdn = toMsisdn;
	}

	/**
	 * @return the volteStatus
	 */
	@Column(name = "VOLTE_STATUS")
	public Integer getVolteStatus() {
		return volteStatus;
	}




	/**
	 * @param volteStatus the volteStatus to set
	 */
	public void setVolteStatus(Integer volteStatus) {
		this.volteStatus = volteStatus;
	}

	/**
	 * @return the testTarget
	 */
	@Transient
	public String getTestTarget() {
		return testTarget;
	}

	/**
	 * @param testTarget the testTarget to set
	 */
	public void setTestTarget(String testTarget) {
		this.testTarget = testTarget;
	}

	@Column(name = "FILE_MD_VALUE")
	public String getFILE_MD_VALUE() {
		return FILE_MD_VALUE;
	}

	public void setFILE_MD_VALUE(String FILE_MD_VALUE) {
		this.FILE_MD_VALUE = FILE_MD_VALUE;
	}

	private String city;
	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	private String prov;
	@Column(name = "PROV")
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}

	public void setCity(String city) {
		this.city = city;
	}

	private String builder;
	private String contractor;
	@Column(name = "BUILDER")
	public String getBuilder() {
		return this.builder;
	}

	public void setBuilder(String builder) {
		this.builder = builder;
	}
	@Column(name = "CONTRACTOR")
	public String getContractor() {
		return this.contractor;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor;
	}

	private String testType;

	//MD5校验
	private String md5Check;

	//业务事件校验
	private String bizEventCheck;

	private String testName;

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}
	@Column(name = "MD5_CHECK")
	public String getMd5Check() {
		return md5Check;
	}

	public void setMd5Check(String md5Check) {
		this.md5Check = md5Check;
	}
	@Column(name = "BIZ_EVENT_CHECK")
	public String getBizEventCheck() {
		return bizEventCheck;
	}

	public void setBizEventCheck(String bizEventCheck) {
		this.bizEventCheck = bizEventCheck;
	}
	@Column(name="TEST_NAME")
	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
}
