package com.itdoes.csm.codegenerator.entity.entity;

/**
 * @author Jalen Zhong
 */
@SuppressWarnings("serial")
public class CsmUser extends com.itdoes.csm.entity.CsmUser {
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
