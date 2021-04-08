package com.datang.domain.security.menu;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 设备权限
 * 
 * @author yinzhipeng
 * @date:2015年10月9日 上午10:42:23
 * @version
 */
@Entity
@DiscriminatorValue("TERMINAL_MENU")
public class TerminalMenu extends Menu implements Comparable<TerminalMenu> {

	private RuleBasedCollator collator = (RuleBasedCollator) Collator
			.getInstance(Locale.CHINA);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单对应实体的数据库ID,用于查询对应实体信息
	 */
	private Long refId;

	/**
	 * 菜单的类型，用于显示不同的菜单信息
	 */
	private String type;

	/**
	 * @return the refId
	 */
	@Column(name = "REF_ID")
	public Long getRefId() {
		return refId;
	}

	/**
	 * @param refId
	 *            the refId to set
	 */
	public void setRefId(Long refId) {
		this.refId = refId;
	}

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

	@Override
	public int compareTo(TerminalMenu o) {
		if (o != null) {
			if (this.getName() != null) {
				return collator.compare(getName(), o.getName());
			}
		}
		return 0;
	}

}
