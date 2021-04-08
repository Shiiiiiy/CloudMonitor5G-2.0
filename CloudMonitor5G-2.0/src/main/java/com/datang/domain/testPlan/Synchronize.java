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
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:40:55
 * @version
 */
@SuppressWarnings("all")
@XStreamAlias("Synchronize")
@Entity
@Table(name = "IADS_TP_SYNCHRONIZE")
public class Synchronize implements Serializable {
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer id;
	/**
	 * 类型
	 */
	@XStreamAlias("Type")
	@XStreamAsAttribute
	private String type;
	/**
	 * 命令列表破
	 */
	@XStreamImplicit(itemFieldName = "Command")
	private List<Command> commands = new ArrayList<Command>();

	/**
	 * @return the type
	 */
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the commands
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "SYNCHRONIZE_ID", nullable = true)
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@Fetch(FetchMode.SUBSELECT)
	public List<Command> getCommands() {
		return commands;
	}

	/**
	 * @param commands
	 *            the commands to set
	 */
	public void setCommands(List<Command> commands) {
		this.commands = commands;
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
