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
@Table(name = "csm_chat_customer_user_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@com.itdoes.common.business.entity.EntityPerms({ @com.itdoes.common.business.entity.EntityPerm(command = com.itdoes.common.business.entity.EntityPermCommand.ALL, filter = com.itdoes.common.business.entity.EntityPermFilter.PERMS) })
public class CsmChatCustomerUserGroup extends BaseEntity {
	private static final long serialVersionUID = -1577114399L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmUser.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@Column(name = "customer_user_id")
	private java.util.UUID customerUserId;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmUserGroup.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@Column(name = "user_group_id")
	private java.util.UUID userGroupId;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmUser.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@Column(name = "operator_user_id")
	private java.util.UUID operatorUserId;

	public java.util.UUID getId() {
		return id;
	}

	public void setId(java.util.UUID id) {
		this.id = id;
	}

	public java.util.UUID getCustomerUserId() {
		return customerUserId;
	}

	public void setCustomerUserId(java.util.UUID customerUserId) {
		this.customerUserId = customerUserId;
	}

	public java.util.UUID getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(java.util.UUID userGroupId) {
		this.userGroupId = userGroupId;
	}

	public java.util.UUID getOperatorUserId() {
		return operatorUserId;
	}

	public void setOperatorUserId(java.util.UUID operatorUserId) {
		this.operatorUserId = operatorUserId;
	}
}