package com.itdoes.csm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.business.entity.EntityPerm;
import com.itdoes.common.business.entity.EntityPermCommand;
import com.itdoes.common.business.entity.EntityPermFilter;
import com.itdoes.common.business.entity.EntityPerms;

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
@Entity
@Table(name = "csm_faq_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityPerms({ @EntityPerm(command = EntityPermCommand.WRITE, filter = EntityPermFilter.PERMS),
		@EntityPerm(command = EntityPermCommand.READ, filter = EntityPermFilter.ANON) })
public class CsmFaqCategory extends BaseEntity {
	private static final long serialVersionUID = 1207564311L;

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
	@Column(name = "create_date_time")
	private java.time.LocalDateTime createDateTime;
	@Column(name = "modify_account_id")
	private java.util.UUID modifyAccountId;
	@Column(name = "modify_date_time")
	private java.time.LocalDateTime modifyDateTime;

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

	public Boolean isActive() {
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

	public java.time.LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(java.time.LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public java.util.UUID getModifyAccountId() {
		return modifyAccountId;
	}

	public void setModifyAccountId(java.util.UUID modifyAccountId) {
		this.modifyAccountId = modifyAccountId;
	}

	public java.time.LocalDateTime getModifyDateTime() {
		return modifyDateTime;
	}

	public void setModifyDateTime(java.time.LocalDateTime modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}
}