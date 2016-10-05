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
@Table(name = "faq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// @Indexed
public class Faq extends BaseEntity {
	private static final long serialVersionUID = 70390L;

	@Id
	@javax.persistence.GeneratedValue
	@Column(name = "id")
	private java.util.UUID id;
	@Column(name = "category_id")
	private java.util.UUID categoryId;
	@Column(name = "question")
	// @Field
	private String question;
	@Column(name = "answer")
	// @Field
	private String answer;
	@Column(name = "keywords")
	private String keywords;
	@Column(name = "attachments")
	private String attachments;
	@Column(name = "active")
	private Boolean active;
	@Column(name = "create_account_id")
	private java.util.UUID createAccountId;
	@Column(name = "create_date")
	private java.time.LocalDateTime createDate;
	@Column(name = "modify_account_id")
	private java.util.UUID modifyAccountId;
	@Column(name = "modify_date")
	private java.time.LocalDateTime modifyDate;

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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
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

	public java.time.LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.time.LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public java.util.UUID getModifyAccountId() {
		return modifyAccountId;
	}

	public void setModifyAccountId(java.util.UUID modifyAccountId) {
		this.modifyAccountId = modifyAccountId;
	}

	public java.time.LocalDateTime getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(java.time.LocalDateTime modifyDate) {
		this.modifyDate = modifyDate;
	}
}