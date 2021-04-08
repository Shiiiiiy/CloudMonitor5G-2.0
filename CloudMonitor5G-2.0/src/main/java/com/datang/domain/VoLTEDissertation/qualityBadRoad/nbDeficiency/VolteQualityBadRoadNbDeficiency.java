/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;

/**
 * VoLTE质量专题----语音质差路段分析,邻区缺失路段指标
 * 
 * @author yinzhipeng
 * @date:2015年11月4日 下午1:14:35
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_QBR_NB_DEF")
public class VolteQualityBadRoadNbDeficiency extends VolteQualityBadRoad {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8930561992698951922L;
}
