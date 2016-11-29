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

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
@Entity
@Table(name = "csm_role_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityPerm({ @EntityPermType(command = EntityPermCommand.ALL, filter = EntityPermFilter.PERMS) })
public class CsmRolePermission extends BaseEntity {
	private static final long serialVersionUID = -865708510L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@Column(name = "role_id")
	private java.util.UUID roleId;
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