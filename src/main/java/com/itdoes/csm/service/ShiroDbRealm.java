package com.itdoes.csm.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.core.shiro.AbstractShiroRealm;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.util.Codecs;
import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
public class ShiroDbRealm extends AbstractShiroRealm {
	@Autowired
	private UserCacheService userCacheService;

	@Override
	protected AuthenticationInfo doAuthentication(UsernamePasswordToken token) throws AuthenticationException {
		final String username = token.getUsername();

		final String userIdString = userCacheService.getUserId(username);
		if (userIdString == null) {
			return null;
		}
		final CsmUser user = userCacheService.getUser(userIdString);
		if (user == null || !user.getActive()) {
			return null;
		}
		return newAuthenticationInfo(user.getId().toString(), username, user.getPassword(), user.getSalt());
	}

	@Override
	protected AuthorizationInfo doAuthorization(Object principal) {
		final ShiroUser shiroUser = (ShiroUser) principal;

		final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addObjectPermissions(userCacheService.getPermissionSetByUser(shiroUser.getId()));
		return info;
	}

	@Override
	protected String getHashAlgrithm() {
		return "SHA-256";
	}

	private AuthenticationInfo newAuthenticationInfo(String id, String username, String password, String salt) {
		return new SimpleAuthenticationInfo(new ShiroUser(id, username), password,
				ByteSource.Util.bytes(Codecs.hexDecode(salt)), getName());
	}
}
