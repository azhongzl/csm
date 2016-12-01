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
@Table(name = "csm_chat_customer_user_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityPerm({ @EntityPermType(command = EntityPermCommand.ALL, filter = EntityPermFilter.PERMS) })
public class CsmChatCustomerUserGroup extends BaseEntity {
	private static final long serialVersionUID = -1577114399L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@Column(name = "customer_user_id")
	@FieldConstraint(entity = CsmUser.class, field = "id", updateStrategy = FieldConstraintStrategy.RESTRICT, deleteStrategy = FieldConstraintStrategy.RESTRICT)
	private java.util.UUID customerUserId;
	@Column(name = "user_group_id")
	@FieldConstraint(entity = CsmUserGroup.class, field = "id", updateStrategy = FieldConstraintStrategy.RESTRICT, deleteStrategy = FieldConstraintStrategy.RESTRICT)
	private java.util.UUID userGroupId;
	@Column(name = "operator_user_id")
	@FieldConstraint(entity = CsmUser.class, field = "id", updateStrategy = FieldConstraintStrategy.RESTRICT, deleteStrategy = FieldConstraintStrategy.RESTRICT)
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