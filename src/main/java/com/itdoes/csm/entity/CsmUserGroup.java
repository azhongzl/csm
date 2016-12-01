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
import com.itdoes.common.business.entity.EntityPermType;
import com.itdoes.common.business.entity.FieldConstraint;
import com.itdoes.common.business.entity.FieldConstraintStrategy;

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
@Entity
@Table(name = "csm_user_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityPerm({ @EntityPermType(command = EntityPermCommand.ALL, filter = EntityPermFilter.PERMS) })
public class CsmUserGroup extends BaseEntity {
	private static final long serialVersionUID = 1943773207L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@Column(name = "name")
	private String name;
	@Column(name = "admin")
	private Boolean admin;
	@Column(name = "chat")
	private Boolean chat;
	@Column(name = "super_id")
	@FieldConstraint(entity = CsmUserGroup.class, field = "id", updateStrategy = FieldConstraintStrategy.RESTRICT, deleteStrategy = FieldConstraintStrategy.RESTRICT)
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

	public Boolean isAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean isChat() {
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