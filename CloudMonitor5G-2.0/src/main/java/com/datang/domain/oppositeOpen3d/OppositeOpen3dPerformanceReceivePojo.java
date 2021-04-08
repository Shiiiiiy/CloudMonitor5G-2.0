package com.datang.domain.oppositeOpen3d;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datang.domain.platform.projectParam.Plan4GParam;

import lombok.Data;

/**
 * 反开3d小区性能验收实体类
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_OPPOSITE_OPEN3D_PERREC")
@Data
public class OppositeOpen3dPerformanceReceivePojo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	/**
	 * 基站名
	 */
	@Column(name = "SITE_NAME")
	private String siteName;
	
	/**
	 * CellName必填:小区友好名
	 */
	@Column(name = "CELL_NAME")
	private String cellName;
	
	/**
	 * LocalCellId
	 * 必填:eNB内的小区ID，唯一标识在一个eNB内的小区。在eNB内统一分配，同一个eNB中的LocalCellId配置值要求不能重复
	 */
	@Column(name = "LOCAL_CELL_ID")
	private Integer localCellId;
	
	/**
	 * 说明localcellid是1小区或2小区或3小区
	 */
	@Column(name = "LOCALCELLID_TAG")
	private Integer localCellIdTag;
	
	/**
	 * CELL ID必填:小区标识
	 */
	@Column(name = "CELL_ID")
	private Long cellId;

	/**
	 * rsrp
	 */
	@Column(name = "RSRP")
	private String rsrp;

	/**
	 * sinr
	 */
	@Column(name = "SINR")
	private String sinr;

	/**
	 * TCP下载吞吐量(60s均值)/平均下载速率(Mbps)
	 */
	@Column(name = "TCP_DOWNLOAD")
	private String tcpDownload;

	/**
	 * TCP上传吞吐量(60s均值)/平均上载速率(Mbps)
	 */
	@Column(name = "TCP_UPLOAD")
	private String tcpUpload;
	
	/**
	 * 49:峰值上传速率(Mbps)
	 */
	@Column(name = "MAX_UPLOAD")
	private String maxUpload;
	
	/**
	 * 48:峰值下载速率(Mbps)
	 */
	@Column(name = "MAX_DOWNLOAD")
	private String maxDownload;

	/**
	 * RRC Setup 
	 */
	@Column(name = "RRC_SETUP")
	private Integer rrcSetup;
	
	@Transient 
	private Integer rrcSetupOpposite;
	
	@Transient 
	private String rrcSetupStr;

	/**
	 * ERAB Setup
	 */
	@Column(name = "ERAB_SETUP")
	private Integer erabSetup;
	
	@Transient 
	private Integer erabSetupOpposite;
	
	@Transient 
	private String erabSetupStr;

	/**
	 * Access
	 */
	@Column(name = "ACCESS1")
	private Integer access;
	
	@Transient 
	private Integer accessOpposite;
	
	@Transient 
	private String accessStr;

	/**
	 * ping时延
	 */
	@Column(name = "PING")
	private String ping;

	/**
	 * VOLTE功能测试/volte成功次数/主叫volte成功次数和被叫volte成功次数
	 */
	@Column(name = "VOLTE")
	private Integer volte;
	
	@Transient 
	private Integer volteOpposite;
	
	@Transient 
	private String volteStr;
	
	/**
	 * 37：主叫业务：VoLTE相关网络质量测试/主叫volte成功次数
	 */
	@Column(name = "DIAL_NET_QUALIY")
	private Integer dialingVolteNetQuality;
	
	/**
	 * 40：被叫业务：VoLTE相关网络质量测试/被叫volte成功次数
	 */
	@Column(name = "CALLED_NET_QUALIY")
	private Integer calledVolteNetQuality;
	
	/**
	 * 42：主叫业务：VoLTE视频通话业务质量/主叫volte视频业务成功次数
	 */
	@Column(name = "DIAL_VIDEO_QUALIY")
	private Integer dialingVolteVideoQuality;
	
	/**
	 * 43：被叫业务：VoLTE视频通话业务质量/被叫volte视频业务成功次数
	 */
	@Column(name = "CALLED_VIDEO_QUALIY")
	private Integer calledVolteVideoQuality;

	/**
	 * CSFB功能测试
	 */
	@Column(name = "CSFB_FUNCTION")
	private Integer csfbFunction;
	
	@Transient 
	private Integer csfbFunctionOpposite;
	
	@Transient 
	private String csfbFunctionStr;

	/**
	 * CSFB接通时延（单端）
	 */
	@Column(name = "CSFB_CONNECT")
	private String csfbConnect;

	/**
	 * 切换测试（基站内小区1）  1："切换成功"; 2："切换2小区失败";  3："切换3小区失败";  4："无法切换";
	 * 切换测试（基站内小区2） 1："切换成功"; 2："切换1小区失败";  3："切换3小区失败";  4："无法切换";
	 * 切换测试（基站内小区3） 1："切换成功"; 2："切换1小区失败";  3："切换2小区失败";  4："无法切换";
	 * 
	 */
	@Column(name = "CHANGE_TEST")
	private Integer changeTest;
	
	@Transient 
	private String changeTestStr;

	
	/**
	 * 第一个切换结果
	 */
	@Transient 
	private Integer changeTest1;
	
	/**
	 * 第二个切换结果
	 */
	@Transient 
	private Integer changeTest2;
	
	/**
	 * 工参
	 */
	@OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "plan4GParam")
	private Plan4GParam plan4GParam;

	public Integer getRrcSetupOpposite() {
		if(rrcSetup==null){
			return null;
		}else if(rrcSetup==1){
			return 0;
		}else{
			return 1;
		}
	}


	public Integer getErabSetupOpposite() {
		if(erabSetup==null){
			return null;
		}else if(erabSetup==1){
			return 0;
		}else{
			return 1;
		}
	}

	public Integer getAccessOpposite() {
		if(access==null){
			return null;
		}else if(access==1){
			return 0;
		}else{
			return 1;
		}
	}


	public Integer getVolteOpposite() {
		if(volte==null){
			return null;
		}else if(volte==1){
			return 0;
		}else{
			return 1;
		}
	}


	public Integer getCsfbFunctionOpposite() {
		if(csfbFunction==null){
			return null;
		}else if(csfbFunction==1){
			return 0;
		}else{
			return 1;
		}
	}
	
	public Integer getChangeTest1() {
		if(changeTest==null){
			return null;
		}else if(changeTest==1){
			return 1;
		}else if(changeTest==2){
			return 0;
		}else if(changeTest==3){
			return 1;
		}else{
			return 0;
		}
	}
	
	public Integer getChangeTest2() {
		if(changeTest==null){
			return null;
		}else if(changeTest==1){
			return 1;
		}else if(changeTest==2){
			return 1;
		}else if(changeTest==3){
			return 0;
		}else{
			return 0;
		}
	}
	
	
	public String getChangeTestStr() {
		if(localCellIdTag == null){
			return null;
		}else if(localCellIdTag == 0){
			if(changeTest==null){
				return null;
			}else if(changeTest==1){
				return "切换成功";
			}else if(changeTest==2){
				return "切换2小区失败";
			}else if(changeTest==3){
				return "切换3小区失败";
			}else{
				return "无法切换";
			}
		}else if(localCellIdTag == 1){
			if(changeTest==null){
				return null;
			}else if(changeTest==1){
				return "切换成功";
			}else if(changeTest==2){
				return "切换1小区失败";
			}else if(changeTest==3){
				return "切换3小区失败";
			}else{
				return "无法切换";
			}
		}else if(localCellIdTag == 2){
			if(changeTest==null){
				return null;
			}else if(changeTest==1){
				return "切换成功";
			}else if(changeTest==2){
				return "切换1小区失败";
			}else if(changeTest==3){
				return "切换2小区失败";
			}else{
				return "无法切换";
			}
		}
		return null;
		
	}


	public String getRrcSetupStr() {
		if(rrcSetup==null){
			return null;
		}else if(rrcSetup==1){
			return "成功";
		}else{
			return "不成功";
		}
	}


	public String getErabSetupStr() {
		if(erabSetup==null){
			return null;
		}else if(erabSetup==1){
			return "成功";
		}else{
			return "不成功";
		}
	}


	public String getAccessStr() {
		if(access==null){
			return null;
		}else if(access==1){
			return "成功";
		}else{
			return "不成功";
		}
	}


	public String getVolteStr() {
		if(volte==null){
			return null;
		}else if(volte==1){
			return "成功";
		}else{
			return "不成功";
		}
	}


	public String getCsfbFunctionStr() {
		if(csfbFunction==null){
			return null;
		}else if(csfbFunction==1){
			return "成功";
		}else{
			return "不成功";
		}
	}



}
