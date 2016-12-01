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
@Table(name = "csm_user_group_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@com.itdoes.common.business.entity.EntityPerms({ @com.itdoes.common.business.entity.EntityPerm(command = com.itdoes.common.business.entity.EntityPermCommand.ALL, filter = com.itdoes.common.business.entity.EntityPermFilter.PERMS) })
public class CsmUserGroupRole extends BaseEntity {
	private static final long serialVersionUID = -562646739L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmUserGroup.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@Column(name = "user_group_id")
	private java.util.UUID userGroupId;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmRole.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@Column(name = "role_id")
	private java.util.UUID roleId;

	public java.util.UUID getId() {
		return id;
	}

	public void setId(java.util.UUID id) {
		this.id = id;
	}

	public java.util.UUID getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(java.util.UUID userGroupId) {
		this.userGroupId = userGroupId;
	}

	public java.util.UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(java.util.UUID roleId) {
		this.roleId = roleId;
	}
}