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
@Table(name = "csm_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CsmUser extends BaseEntity {
	private static final long serialVersionUID = -1566100664L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@javax.validation.constraints.NotNull
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@javax.validation.constraints.NotNull
	@Column(name = "salt")
	private String salt;
	@javax.validation.constraints.NotNull
	@Column(name = "active")
	private Boolean active;
	@com.itdoes.common.business.entity.FieldConstraint(entity = CsmUserGroup.class, field = "id", updateStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.CASCADE, deleteStrategy = com.itdoes.common.business.entity.FieldConstraintStrategy.RESTRICT)
	@javax.validation.constraints.NotNull
	@Column(name = "user_group_id")
	private java.util.UUID userGroupId;

	public java.util.UUID getId() {
		return id;
	}

	public void setId(java.util.UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public java.util.UUID getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(java.util.UUID userGroupId) {
		this.userGroupId = userGroupId;
	}

	@javax.persistence.Transient
	private String plainPassword;

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public void populatePassword() {
		final com.itdoes.common.extension.security.DigestsHelper.HexDigestSalt hexResult = com.itdoes.common.extension.security.DigestsHelper
				.digestByRandomSalt(com.itdoes.common.core.security.Digests.SHA256, getPlainPassword());
		setPassword(hexResult.getHexDigest());
		setSalt(hexResult.getHexSalt());
	}
}