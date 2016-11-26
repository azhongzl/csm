package com.itdoes.csm.service;

import java.util.Set;
import java.util.UUID;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.shiro.AbstractShiroRealm;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.util.Codecs;
import com.itdoes.csm.dto.Admin;
import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
public class ShiroDbRealm extends AbstractShiroRealm {
	private static final Admin ADMIN = Admin.getInstance();

	@Autowired
	private EntityEnv entityEnv;

	@Autowired
	private EntityDbService entityDbService;

	@Autowired
	private PermissionService permissionService;

	@Override
	protected AuthenticationInfo doAuthentication(UsernamePasswordToken token) throws AuthenticationException {
		final String username = token.getUsername();

		if (ADMIN.isAdminByUsername(username)) {
			return newAuthenticationInfo(ADMIN.getIdString(), ADMIN.getUsername(), ADMIN.getPassword(),
					ADMIN.getSalt());
		} else {
			final CsmUser user = entityDbService.findOne(getUserPair(), Specifications.build(CsmUser.class,
					Lists.newArrayList(new FindFilter("username", Operator.EQ, username))));
			if (user == null || !user.isActive()) {
				return null;
			}
			return newAuthenticationInfo(user.getId().toString(), username, user.getPassword(), user.getSalt());
		}
	}

	@Override
	protected AuthorizationInfo doAuthorization(Object principal) {
		final ShiroUser shiroUser = (ShiroUser) principal;

		final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if (ADMIN.isAdminById(shiroUser.getId())) {
			info.addStringPermission(ADMIN.getPermission());
		} else {
			final CsmUser user = entityDbService.get(getUserPair(), UUID.fromString(shiroUser.getId()));
			final Set<String> permissionSet = permissionService.findPermissionSetByUserGroup(user.getUserGroupId());
			info.addStringPermissions(permissionSet);
		}
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

	private EntityPair<CsmUser, UUID> getUserPair() {
		return entityEnv.getPair(CsmUser.class.getSimpleName());
	}
}
