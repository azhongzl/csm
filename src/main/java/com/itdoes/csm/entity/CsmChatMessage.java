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
@Table(name = "csm_chat_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CsmChatMessage extends BaseEntity {
	private static final long serialVersionUID = -1705651342L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmUser.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@javax.validation.constraints.NotNull
	@Column(name = "room_id")
	private java.util.UUID roomId;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmUser.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@javax.validation.constraints.NotNull
	@Column(name = "sender_id")
	private java.util.UUID senderId;
	@Column(name = "create_date_time")
	private java.time.LocalDateTime createDateTime;
	@javax.validation.constraints.NotNull
	@Column(name = "from_admin")
	private Boolean fromAdmin;
	@javax.validation.constraints.NotNull
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