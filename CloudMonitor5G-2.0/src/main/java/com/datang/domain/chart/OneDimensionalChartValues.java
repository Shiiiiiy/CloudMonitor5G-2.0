/**
 * 
 */
package com.datang.domain.chart;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 一维图结果集
 * 
 * @author yinzhipeng
 * @date:2015年12月31日 上午9:07:34
 * @version
 */
@Entity
@IdClass(OneDimensionalChartValues.class)
@Table(name = "IADS_ONE_DIMENSIONAL_RESULT")
public class OneDimensionalChartValues implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1257330253506120681L;
	private Long qbrId;
	private OneDimensionalChartConfig oneDimensionalChartConfig;
	private Float seq0;
	private Float seq1;
	private Float seq2;
	private Float seq3;
	private Float seq4;
	private Float seq5;
	private Float seq6;
	private Float seq7;
	private Float seq8;
	private Float seq9;
	private Float seq10;
	private Float seq11;
	private Float seq12;
	private Float seq13;
	private Float seq14;
	private Float seq15;
	private Float seq16;
	private Float seq17;
	private Float seq18;
	private Float seq19;
	private Float seq20;
	private Float seq21;
	private Float seq22;
	private Float seq23;

	/**
	 * @return the qbrIdqbrId
	 */
	@Id
	@Column(name = "RID")
	public Long getQbrId() {
		return qbrId;
	}

	/**
	 * @param qbrId
	 *            the qbrId to set
	 */
	public void setQbrId(Long qbrId) {
		this.qbrId = qbrId;
	}

	/**
	 * @return the oneDimensionalChartConfigoneDimensionalChartConfig
	 */
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TYPEID")
	public OneDimensionalChartConfig getOneDimensionalChartConfig() {
		return oneDimensionalChartConfig;
	}

	/**
	 * @param oneDimensionalChartConfig
	 *            the oneDimensionalChartConfig to set
	 */
	public void setOneDimensionalChartConfig(
			OneDimensionalChartConfig oneDimensionalChartConfig) {
		this.oneDimensionalChartConfig = oneDimensionalChartConfig;
	}

	/**
	 * @return the seq0seq0
	 */
	@Column(name = "SEQ0")
	public Float getSeq0() {
		return seq0;
	}

	/**
	 * @param seq0
	 *            the seq0 to set
	 */
	public void setSeq0(Float seq0) {
		this.seq0 = seq0;
	}

	/**
	 * @return the seq1seq1
	 */
	@Column(name = "SEQ1")
	public Float getSeq1() {
		return seq1;
	}

	/**
	 * @param seq1
	 *            the seq1 to set
	 */
	public void setSeq1(Float seq1) {
		this.seq1 = seq1;
	}

	/**
	 * @return the seq2seq2
	 */
	@Column(name = "SEQ2")
	public Float getSeq2() {
		return seq2;
	}

	/**
	 * @param seq2
	 *            the seq2 to set
	 */
	public void setSeq2(Float seq2) {
		this.seq2 = seq2;
	}

	/**
	 * @return the seq3seq3
	 */
	@Column(name = "SEQ3")
	public Float getSeq3() {
		return seq3;
	}

	/**
	 * @param seq3
	 *            the seq3 to set
	 */
	public void setSeq3(Float seq3) {
		this.seq3 = seq3;
	}

	/**
	 * @return the seq4seq4
	 */
	@Column(name = "SEQ4")
	public Float getSeq4() {
		return seq4;
	}

	/**
	 * @param seq4
	 *            the seq4 to set
	 */
	public void setSeq4(Float seq4) {
		this.seq4 = seq4;
	}

	/**
	 * @return the seq5seq5
	 */
	@Column(name = "SEQ5")
	public Float getSeq5() {
		return seq5;
	}

	/**
	 * @param seq5
	 *            the seq5 to set
	 */
	public void setSeq5(Float seq5) {
		this.seq5 = seq5;
	}

	/**
	 * @return the seq6seq6
	 */
	@Column(name = "SEQ6")
	public Float getSeq6() {
		return seq6;
	}

	/**
	 * @param seq6
	 *            the seq6 to set
	 */
	public void setSeq6(Float seq6) {
		this.seq6 = seq6;
	}

	/**
	 * @return the seq7seq7
	 */
	@Column(name = "SEQ7")
	public Float getSeq7() {
		return seq7;
	}

	/**
	 * @param seq7
	 *            the seq7 to set
	 */
	public void setSeq7(Float seq7) {
		this.seq7 = seq7;
	}

	/**
	 * @return the seq8seq8
	 */
	@Column(name = "SEQ8")
	public Float getSeq8() {
		return seq8;
	}

	/**
	 * @param seq8
	 *            the seq8 to set
	 */
	public void setSeq8(Float seq8) {
		this.seq8 = seq8;
	}

	/**
	 * @return the seq9seq9
	 */
	@Column(name = "SEQ9")
	public Float getSeq9() {
		return seq9;
	}

	/**
	 * @param seq9
	 *            the seq9 to set
	 */
	public void setSeq9(Float seq9) {
		this.seq9 = seq9;
	}

	/**
	 * @return the seq10seq10
	 */
	@Column(name = "SEQ10")
	public Float getSeq10() {
		return seq10;
	}

	/**
	 * @param seq10
	 *            the seq10 to set
	 */
	public void setSeq10(Float seq10) {
		this.seq10 = seq10;
	}

	/**
	 * @return the seq11seq11
	 */
	@Column(name = "SEQ11")
	public Float getSeq11() {
		return seq11;
	}

	/**
	 * @param seq11
	 *            the seq11 to set
	 */
	public void setSeq11(Float seq11) {
		this.seq11 = seq11;
	}

	/**
	 * @return the seq12seq12
	 */
	@Column(name = "SEQ12")
	public Float getSeq12() {
		return seq12;
	}

	/**
	 * @param seq12
	 *            the seq12 to set
	 */
	public void setSeq12(Float seq12) {
		this.seq12 = seq12;
	}

	/**
	 * @return the seq13seq13
	 */
	@Column(name = "SEQ13")
	public Float getSeq13() {
		return seq13;
	}

	/**
	 * @param seq13
	 *            the seq13 to set
	 */
	public void setSeq13(Float seq13) {
		this.seq13 = seq13;
	}

	/**
	 * @return the seq14seq14
	 */
	@Column(name = "SEQ14")
	public Float getSeq14() {
		return seq14;
	}

	/**
	 * @param seq14
	 *            the seq14 to set
	 */
	public void setSeq14(Float seq14) {
		this.seq14 = seq14;
	}

	/**
	 * @return the seq15seq15
	 */
	@Column(name = "SEQ15")
	public Float getSeq15() {
		return seq15;
	}

	/**
	 * @param seq15
	 *            the seq15 to set
	 */
	public void setSeq15(Float seq15) {
		this.seq15 = seq15;
	}

	/**
	 * @return the seq16seq16
	 */
	@Column(name = "SEQ16")
	public Float getSeq16() {
		return seq16;
	}

	/**
	 * @param seq16
	 *            the seq16 to set
	 */
	public void setSeq16(Float seq16) {
		this.seq16 = seq16;
	}

	/**
	 * @return the seq17seq17
	 */
	@Column(name = "SEQ17")
	public Float getSeq17() {
		return seq17;
	}

	/**
	 * @param seq17
	 *            the seq17 to set
	 */
	public void setSeq17(Float seq17) {
		this.seq17 = seq17;
	}

	/**
	 * @return the seq18seq18
	 */
	@Column(name = "SEQ18")
	public Float getSeq18() {
		return seq18;
	}

	/**
	 * @param seq18
	 *            the seq18 to set
	 */
	public void setSeq18(Float seq18) {
		this.seq18 = seq18;
	}

	/**
	 * @return the seq19seq19
	 */
	@Column(name = "SEQ19")
	public Float getSeq19() {
		return seq19;
	}

	/**
	 * @param seq19
	 *            the seq19 to set
	 */
	public void setSeq19(Float seq19) {
		this.seq19 = seq19;
	}

	/**
	 * @return the seq20seq20
	 */
	@Column(name = "SEQ20")
	public Float getSeq20() {
		return seq20;
	}

	/**
	 * @param seq20
	 *            the seq20 to set
	 */
	public void setSeq20(Float seq20) {
		this.seq20 = seq20;
	}

	/**
	 * @return the seq21seq21
	 */
	@Column(name = "SEQ21")
	public Float getSeq21() {
		return seq21;
	}

	/**
	 * @param seq21
	 *            the seq21 to set
	 */
	public void setSeq21(Float seq21) {
		this.seq21 = seq21;
	}

	/**
	 * @return the seq22seq22
	 */
	@Column(name = "SEQ22")
	public Float getSeq22() {
		return seq22;
	}

	/**
	 * @param seq22
	 *            the seq22 to set
	 */
	public void setSeq22(Float seq22) {
		this.seq22 = seq22;
	}

	/**
	 * @return the seq23seq23
	 */
	@Column(name = "SEQ23")
	public Float getSeq23() {
		return seq23;
	}

	/**
	 * @param seq23
	 *            the seq23 to set
	 */
	public void setSeq23(Float seq23) {
		this.seq23 = seq23;
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
		result = prime
				* result
				+ ((oneDimensionalChartConfig == null) ? 0
						: oneDimensionalChartConfig.hashCode());
		result = prime * result + ((qbrId == null) ? 0 : qbrId.hashCode());
		result = prime * result + ((seq0 == null) ? 0 : seq0.hashCode());
		result = prime * result + ((seq1 == null) ? 0 : seq1.hashCode());
		result = prime * result + ((seq10 == null) ? 0 : seq10.hashCode());
		result = prime * result + ((seq11 == null) ? 0 : seq11.hashCode());
		result = prime * result + ((seq12 == null) ? 0 : seq12.hashCode());
		result = prime * result + ((seq13 == null) ? 0 : seq13.hashCode());
		result = prime * result + ((seq14 == null) ? 0 : seq14.hashCode());
		result = prime * result + ((seq15 == null) ? 0 : seq15.hashCode());
		result = prime * result + ((seq16 == null) ? 0 : seq16.hashCode());
		result = prime * result + ((seq17 == null) ? 0 : seq17.hashCode());
		result = prime * result + ((seq18 == null) ? 0 : seq18.hashCode());
		result = prime * result + ((seq19 == null) ? 0 : seq19.hashCode());
		result = prime * result + ((seq2 == null) ? 0 : seq2.hashCode());
		result = prime * result + ((seq20 == null) ? 0 : seq20.hashCode());
		result = prime * result + ((seq21 == null) ? 0 : seq21.hashCode());
		result = prime * result + ((seq22 == null) ? 0 : seq22.hashCode());
		result = prime * result + ((seq23 == null) ? 0 : seq23.hashCode());
		result = prime * result + ((seq3 == null) ? 0 : seq3.hashCode());
		result = prime * result + ((seq4 == null) ? 0 : seq4.hashCode());
		result = prime * result + ((seq5 == null) ? 0 : seq5.hashCode());
		result = prime * result + ((seq6 == null) ? 0 : seq6.hashCode());
		result = prime * result + ((seq7 == null) ? 0 : seq7.hashCode());
		result = prime * result + ((seq8 == null) ? 0 : seq8.hashCode());
		result = prime * result + ((seq9 == null) ? 0 : seq9.hashCode());
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
		OneDimensionalChartValues other = (OneDimensionalChartValues) obj;
		if (oneDimensionalChartConfig == null) {
			if (other.oneDimensionalChartConfig != null)
				return false;
		} else if (!oneDimensionalChartConfig
				.equals(other.oneDimensionalChartConfig))
			return false;
		if (qbrId == null) {
			if (other.qbrId != null)
				return false;
		} else if (!qbrId.equals(other.qbrId))
			return false;
		if (seq0 == null) {
			if (other.seq0 != null)
				return false;
		} else if (!seq0.equals(other.seq0))
			return false;
		if (seq1 == null) {
			if (other.seq1 != null)
				return false;
		} else if (!seq1.equals(other.seq1))
			return false;
		if (seq10 == null) {
			if (other.seq10 != null)
				return false;
		} else if (!seq10.equals(other.seq10))
			return false;
		if (seq11 == null) {
			if (other.seq11 != null)
				return false;
		} else if (!seq11.equals(other.seq11))
			return false;
		if (seq12 == null) {
			if (other.seq12 != null)
				return false;
		} else if (!seq12.equals(other.seq12))
			return false;
		if (seq13 == null) {
			if (other.seq13 != null)
				return false;
		} else if (!seq13.equals(other.seq13))
			return false;
		if (seq14 == null) {
			if (other.seq14 != null)
				return false;
		} else if (!seq14.equals(other.seq14))
			return false;
		if (seq15 == null) {
			if (other.seq15 != null)
				return false;
		} else if (!seq15.equals(other.seq15))
			return false;
		if (seq16 == null) {
			if (other.seq16 != null)
				return false;
		} else if (!seq16.equals(other.seq16))
			return false;
		if (seq17 == null) {
			if (other.seq17 != null)
				return false;
		} else if (!seq17.equals(other.seq17))
			return false;
		if (seq18 == null) {
			if (other.seq18 != null)
				return false;
		} else if (!seq18.equals(other.seq18))
			return false;
		if (seq19 == null) {
			if (other.seq19 != null)
				return false;
		} else if (!seq19.equals(other.seq19))
			return false;
		if (seq2 == null) {
			if (other.seq2 != null)
				return false;
		} else if (!seq2.equals(other.seq2))
			return false;
		if (seq20 == null) {
			if (other.seq20 != null)
				return false;
		} else if (!seq20.equals(other.seq20))
			return false;
		if (seq21 == null) {
			if (other.seq21 != null)
				return false;
		} else if (!seq21.equals(other.seq21))
			return false;
		if (seq22 == null) {
			if (other.seq22 != null)
				return false;
		} else if (!seq22.equals(other.seq22))
			return false;
		if (seq23 == null) {
			if (other.seq23 != null)
				return false;
		} else if (!seq23.equals(other.seq23))
			return false;
		if (seq3 == null) {
			if (other.seq3 != null)
				return false;
		} else if (!seq3.equals(other.seq3))
			return false;
		if (seq4 == null) {
			if (other.seq4 != null)
				return false;
		} else if (!seq4.equals(other.seq4))
			return false;
		if (seq5 == null) {
			if (other.seq5 != null)
				return false;
		} else if (!seq5.equals(other.seq5))
			return false;
		if (seq6 == null) {
			if (other.seq6 != null)
				return false;
		} else if (!seq6.equals(other.seq6))
			return false;
		if (seq7 == null) {
			if (other.seq7 != null)
				return false;
		} else if (!seq7.equals(other.seq7))
			return false;
		if (seq8 == null) {
			if (other.seq8 != null)
				return false;
		} else if (!seq8.equals(other.seq8))
			return false;
		if (seq9 == null) {
			if (other.seq9 != null)
				return false;
		} else if (!seq9.equals(other.seq9))
			return false;
		return true;
	}

}
