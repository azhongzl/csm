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
@Table(name = "csm_chat_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityPerm({ @EntityPermType(command = EntityPermCommand.ALL, filter = EntityPermFilter.USER) })
public class CsmChatMessage extends BaseEntity {
	private static final long serialVersionUID = -1705651342L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@Column(name = "room_id")
	@FieldConstraint(entity = CsmUser.class, field = "id", updateStrategy = FieldConstraintStrategy.RESTRICT, deleteStrategy = FieldConstraintStrategy.RESTRICT)
	private java.util.UUID roomId;
	@Column(name = "sender_id")
	@FieldConstraint(entity = CsmUser.class, field = "id", updateStrategy = FieldConstraintStrategy.RESTRICT, deleteStrategy = FieldConstraintStrategy.RESTRICT)
	private java.util.UUID senderId;
	@Column(name = "create_date_time")
	private java.time.LocalDateTime createDateTime;
	@Column(name = "from_admin")
	private Boolean fromAdmin;
	@Column(name = "message")
	private String message;

	public java.util.UUID getId() {
		return id;
	}

	public void setId(java.util.UUID id) {
		this.id = id;
	}

	public java.util.UUID getRoomId() {
		return roomId;
	}

	public void setRoomId(java.util.UUID roomId) {
		this.roomId = roomId;
	}

	public java.util.UUID getSenderId() {
		return senderId;
	}

	public void setSenderId(java.util.UUID senderId) {
		this.senderId = senderId;
	}

	public java.time.LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(java.time.LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Boolean isFromAdmin() {
		return fromAdmin;
	}

	public void setFromAdmin(Boolean fromAdmin) {
		this.fromAdmin = fromAdmin;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@javax.persistence.Transient
	private String senderName;

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
}