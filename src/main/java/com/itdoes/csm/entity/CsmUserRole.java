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
@Table(name = "csm_user_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityPerm(types = { EntityPermType.ALL })
public class CsmUserRole extends BaseEntity {
	private static final long serialVersionUID = -906804258L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@Column(name = "user_id")
	private java.util.UUID userId;
	@Column(name = "role_id")
	private java.util.UUID roleId;

	public java.util.UUID getId() {
		return id;
	}

	public void setId(java.util.UUID id) {
		this.id = id;
	}

	public java.util.UUID getUserId() {
		return userId;
	}

	public void setUserId(java.util.UUID userId) {
		this.userId = userId;
	}

	public java.util.UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(java.util.UUID roleId) {
		this.roleId = roleId;
	}
}