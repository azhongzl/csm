package com.itdoes.csm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.business.entity.EntityPerm;
import com.itdoes.common.business.entity.EntityPermType;

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
@Entity
@Table(name = "csm_faq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityPerm(types = { EntityPermType.WRITE })
@org.hibernate.search.annotations.Indexed
public class CsmFaq extends BaseEntity {
	private static final long serialVersionUID = 2027675641L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@Column(name = "category_id")
	private java.util.UUID categoryId;
	@org.hibernate.search.annotations.Field
	@Column(name = "question")
	private String question;
	@org.hibernate.search.annotations.Field
	@Column(name = "answer")
	private String answer;
	@com.itdoes.common.business.entity.UploadField
	@Column(name = "attachments")
	private String attachments;
	@Column(name = "active")
	private Boolean active;
	@Column(name = "create_account_id")
	private java.util.UUID createAccountId;
	@Column(name = "create_date_time")
	private java.time.LocalDateTime createDateTime;
	@Column(name = "modify_account_id")
	private java.util.UUID modifyAccountId;
	@Column(name = "modify_date_time")
	private java.time.LocalDateTime modifyDateTime;

	public java.util.UUID getId() {
		return id;
	}

	public void setId(java.util.UUID id) {
		this.id = id;
	}

	public java.util.UUID getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(java.util.UUID categoryId) {
		this.categoryId = categoryId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public java.util.UUID getCreateAccountId() {
		return createAccountId;
	}

	public void setCreateAccountId(java.util.UUID createAccountId) {
		this.createAccountId = createAccountId;
	}

	public java.time.LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(java.time.LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public java.util.UUID getModifyAccountId() {
		return modifyAccountId;
	}

	public void setModifyAccountId(java.util.UUID modifyAccountId) {
		this.modifyAccountId = modifyAccountId;
	}

	public java.time.LocalDateTime getModifyDateTime() {
		return modifyDateTime;
	}

	public void setModifyDateTime(java.time.LocalDateTime modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}
}