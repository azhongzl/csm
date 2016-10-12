package com.itdoes.csm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.business.entity.EntityPerm;
import com.itdoes.common.business.entity.EntityPermType;

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityPerm(types = { EntityPermType.WRITE })
public class Category extends BaseEntity {
	private static final long serialVersionUID = 115155230L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "active")
	private Boolean active;
	@Column(name = "create_account_id")
	private java.util.UUID createAccountId;
	@Column(name = "create_date")
	private java.time.LocalDateTime createDate;
	@Column(name = "modify_account_id")
	private java.util.UUID modifyAccountId;
	@Column(name = "modify_date")
	private java.time.LocalDateTime modifyDate;

	public java.util.UUID getId() {
		return id;
	}

	public void setId(java.util.UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public java.util.UUID getCreateAccountId() {
		return createAccountId;
	}

	public void setCreateAccountId(java.util.UUID createAccountId) {
		this.createAccountId = createAccountId;
	}

	public java.time.LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.time.LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public java.util.UUID getModifyAccountId() {
		return modifyAccountId;
	}

	public void setModifyAccountId(java.util.UUID modifyAccountId) {
		this.modifyAccountId = modifyAccountId;
	}

	public java.time.LocalDateTime getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(java.time.LocalDateTime modifyDate) {
		this.modifyDate = modifyDate;
	}
}