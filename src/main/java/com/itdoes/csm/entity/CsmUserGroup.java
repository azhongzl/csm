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
@Table(name = "csm_user_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CsmUserGroup extends BaseEntity {
	private static final long serialVersionUID = 1943773207L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@javax.validation.constraints.NotNull
	@Column(name = "name")
	private String name;
	@javax.validation.constraints.NotNull
	@Column(name = "admin")
	private Boolean admin;
	@javax.validation.constraints.NotNull
	@Column(name = "chat")
	private Boolean chat;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmUserGroup.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@Column(name = "super_id")
	private java.util.UUID superId;

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

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getChat() {
		return chat;
	}

	public void setChat(Boolean chat) {
		this.chat = chat;
	}

	public java.util.UUID getSuperId() {
		return superId;
	}

	public void setSuperId(java.util.UUID superId) {
		this.superId = superId;
	}
}