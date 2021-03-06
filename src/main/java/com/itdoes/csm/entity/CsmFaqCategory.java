package com.itdoes.csm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.itdoes.common.business.entity.BaseEntity;

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
@Entity
@Table(name = "csm_faq_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CsmFaqCategory extends BaseEntity {
	private static final long serialVersionUID = 1207564311L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@javax.validation.constraints.NotNull
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "create_user_id")
	private java.util.UUID createUserId;
	@Column(name = "create_date_time")
	private java.time.LocalDateTime createDateTime;
	@Column(name = "modify_user_id")
	private java.util.UUID modifyUserId;
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

	public java.util.UUID getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(java.util.UUID createUserId) {
		this.createUserId = createUserId;
	}

	public java.time.LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(java.time.LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public java.util.UUID getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(java.util.UUID modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public java.time.LocalDateTime getModifyDateTime() {
		return modifyDateTime;
	}

	public void setModifyDateTime(java.time.LocalDateTime modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}
}