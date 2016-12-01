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
import com.itdoes.common.business.entity.FieldConstraint;
import com.itdoes.common.business.entity.FieldConstraintStrategy;

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
@Entity
@Table(name = "csm_chat_unhandled_customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityPerms({ @EntityPerm(command = EntityPermCommand.ALL, filter = EntityPermFilter.PERMS) })
public class CsmChatUnhandledCustomer extends BaseEntity {
	private static final long serialVersionUID = -2110794452L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@Column(name = "user_id")
	@FieldConstraint(entity = CsmUser.class, field = "id", updateStrategy = FieldConstraintStrategy.RESTRICT, deleteStrategy = FieldConstraintStrategy.RESTRICT)
	private java.util.UUID userId;
	@Column(name = "create_date_time")
	private java.time.LocalDateTime createDateTime;

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

	public java.time.LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(java.time.LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}
}