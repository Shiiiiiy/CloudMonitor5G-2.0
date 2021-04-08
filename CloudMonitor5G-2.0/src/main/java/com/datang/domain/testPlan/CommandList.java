package com.datang.domain.testPlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 命令列表
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:36:58
 * @version
 */
@SuppressWarnings("all")
@XStreamAlias("CommandList")
@Entity
@Table(name = "IADS_TP_COMMAND_LIST")
public class CommandList implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	/**
	 * 重复次数
	 */
	@XStreamAlias("Repeat")
	@XStreamAsAttribute
	private int repeat = 999;
	/**
	 * 执行方式：并行/串行
	 */
	@XStreamAlias("Synchronize")
	private Synchronize synchronize = new Synchronize();

	/**
	 * @return the repeat
	 */
	@Column(name = "REPEAT")
	public int getRepeat() {
		return repeat;
	}

	/**
	 * @param repeat
	 *            the repeat to set
	 */
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	/**
	 * @return the synchronize
	 */
	@OneToOne
	@JoinColumn(name = "SYNCHRONIZE_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	public Synchronize getSynchronize() {
		return synchronize;
	}

	/**
	 * @param synchronize
	 *            the synchronize to set
	 */
	public void setSynchronize(Synchronize synchronize) {
		this.synchronize = synchronize;
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
}
