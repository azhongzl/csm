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
@Table(name = "csm_role_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CsmRolePermission extends BaseEntity {
	private static final long serialVersionUID = -865708510L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmRole.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@javax.validation.constraints.NotNull
	@Column(name = "role_id")
	private java.util.UUID roleId;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmPermission.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@javax.validation.constraints.NotNull
	@Column(name = "permission_id")
	private java.util.UUID permissionId;

	public java.util.UUID getId() {
		return id;
	}

	public void setId(java.util.UUID id) {
		this.id = id;
	}

	public java.util.UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(java.util.UUID roleId) {
		this.roleId = roleId;
	}

	public java.util.UUID getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(java.util.UUID permissionId) {
		this.permissionId = permissionId;
	}
}