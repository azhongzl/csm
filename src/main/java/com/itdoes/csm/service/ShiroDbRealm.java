package com.itdoes.csm.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.util.ByteSource;

import com.itdoes.common.business.Permissions;
import com.itdoes.common.core.security.Digests;
import com.itdoes.common.core.shiro.AbstractShiroRealm;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.util.Codecs;

/**
 * @author Jalen Zhong
 */
public class ShiroDbRealm extends AbstractShiroRealm {
	@Override
	protected AuthenticationInfo doAuthentication(UsernamePasswordToken token) throws AuthenticationException {
		String username = token.getUsername();
		User user = new User();
		user.setPlainPassword(username);
		encryptUser(user);
		final byte[] salt = Codecs.hexDecode(user.getSalt());
		return new SimpleAuthenticationInfo(new ShiroUser(username), user.getPassword(), ByteSource.Util.bytes(salt),
				getName());
	}

	@Override
	protected AuthorizationInfo doAuthorization(Object principal) {
		final ShiroUser shiroUser = (ShiroUser) principal;
		final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(Permissions.getAllPermissions());
		return info;
	}

	@Override
	protected String getHashAlgrithm() {
		return "SHA-256";
	}

	private static void encryptUser(User user) {
		final byte[] salt = Digests.generateSalt();
		user.setSalt(Codecs.hexEncode(salt));
		final byte[] hashedPassword = Digests.sha256(user.getPlainPassword().getBytes(), salt);
		user.setPassword(Codecs.hexEncode(hashedPassword));
	}

	private static class User {
		public String salt;
		public String plainPassword;
		public String password;

		public String getSalt() {
			return salt;
		}

		public void setSalt(String salt) {
			this.salt = salt;
		}

		public String getPlainPassword() {
			return plainPassword;
		}

		public void setPlainPassword(String plainPassword) {
			this.plainPassword = plainPassword;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
