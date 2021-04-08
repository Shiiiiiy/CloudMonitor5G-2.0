/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.paramError;

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

/**
 * 参数错误问题路段----优化建议
 * 
 * @author yinzhipeng
 * @date:2015年9月11日 下午4:22:05
 * @version
 */
@Entity
@Table(name = "IADS_QBR_PARA_ERR_OPT_ADVICE")
public class ParamErrorOptimizeAdvice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1410772550355944609L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(参数错误问题路段)
	 */
	private VolteQualityBadRoadParamError volteQualityBadRoadParamError;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * 参数名称
	 */
	private String paramName;
	/**
	 * 所处位置,参数对应的信令名称
	 */
	private String signallingName;
	/**
	 * 原始值,该参数在问题路段中的取值
	 */
	private String originalValue;
	/**
	 * 参数模板值,该参数在参数模板中的取值
	 */
	private String templateValue;

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
	 * @return the volteQualityBadRoadParamErrorvolteQualityBadRoadParamError
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteQualityBadRoadParamError getVolteQualityBadRoadParamError() {
		return volteQualityBadRoadParamError;
	}

	/**
	 * @param volteQualityBadRoadParamError
	 *            the volteQualityBadRoadParamError to set
	 */
	public void setVolteQualityBadRoadParamError(
			VolteQualityBadRoadParamError volteQualityBadRoadParamError) {
		this.volteQualityBadRoadParamError = volteQualityBadRoadParamError;
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
	 * @return the paramNameparamName
	 */
	@Column(name = "PARAM_NAME")
	public String getParamName() {
		return paramName;
	}

	/**
	 * @param paramName
	 *            the paramName to set
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * @return the signallingNamesignallingName
	 */
	@Column(name = "SIGNALLING_NAME")
	public String getSignallingName() {
		return signallingName;
	}

	/**
	 * @param signallingName
	 *            the signallingName to set
	 */
	public void setSignallingName(String signallingName) {
		this.signallingName = signallingName;
	}

	/**
	 * @return the originalValueoriginalValue
	 */
	@Column(name = "ORIGINAL_VALUE")
	public String getOriginalValue() {
		return originalValue;
	}

	/**
	 * @param originalValue
	 *            the originalValue to set
	 */
	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	/**
	 * @return the templateValuetemplateValue
	 */
	@Column(name = "TEMPLATE_VALUE")
	public String getTemplateValue() {
		return templateValue;
	}

	/**
	 * @param templateValue
	 *            the templateValue to set
	 */
	public void setTemplateValue(String templateValue) {
		this.templateValue = templateValue;
	}

}
