package com.itdoes.csm.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category extends BaseEntity {
	private static final long serialVersionUID = 115155230L;

	@Id
	@Column(name = "id")
	@GeneratedValue
	private UUID id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "create_date")
	private java.time.LocalDateTime createDate;
	@Column(name = "modify_date")
	private java.time.LocalDateTime modifyDate;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public java.time.LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.time.LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public java.time.LocalDateTime getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(java.time.LocalDateTime modifyDate) {
		this.modifyDate = modifyDate;
	}
}