package com.datang.domain.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * 功能权限BEAN
 * 
 * @author yinzhipeng
 * @date:2015年10月8日 下午5:41:38
 * @version
 */
@Entity
@Table(name = "IADS_PERMISSION")
public class Permission implements Serializable, Comparable<Permission> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2770924671988832448L;

	/**
	 * 功能权限ID
	 */
	private Long id;
	/**
	 * 功能权限名
	 */
	private String text;
	/**
	 * 功能权限唯一标识String字段
	 */
	private String wildcardpermission;
	/**
	 * 父级功能权限
	 */
	private Permission parent;
	/**
	 * 子级功能权限
	 */
	private Set<Permission> children = new HashSet<Permission>();

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
	 * @return the namename
	 */
	@Column(name = "NAME")
	public String getText() {
		return text;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the wildcardpermissionwildcardpermission
	 */
	@Column(name = "WILDCARD_PERMISSION")
	public String getWildcardpermission() {
		return wildcardpermission;
	}

	/**
	 * @param wildcardpermission
	 *            the wildcardpermission to set
	 */
	public void setWildcardpermission(String wildcardpermission) {
		this.wildcardpermission = wildcardpermission;
	}

	/**
	 * @return the parentparent
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "PID")
	@JSON(serialize = false)
	public Permission getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Permission parent) {
		this.parent = parent;
	}

	/**
	 * @return the childrenchildren
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	public Set<Permission> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(Set<Permission> children) {
		this.children = children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Permission o) {
		return (int) (this.getId() - o.getId());
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Permission other = (Permission) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
