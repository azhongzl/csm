package com.itdoes.csm.service;

import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

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
import com.itdoes.csm.entity.CsmUser;

/**
 * @author Jalen Zhong
 */
public class ShiroDbRealm extends AbstractShiroRealm {
	@Autowired
	private EntityEnv entityEnv;

	@Autowired
	private EntityDbService entityDbService;

	@Autowired
	private UserDbService userDbService;

	private EntityPair<CsmUser, UUID> pair;

	@PostConstruct
	public void myInit() {
		pair = entityEnv.getPair(CsmUser.class.getSimpleName());
	}

	@Override
	protected AuthenticationInfo doAuthentication(UsernamePasswordToken token) throws AuthenticationException {
		final String username = token.getUsername();
		final CsmUser user = findUser(username);
		if (user == null) {
			return null;
		}

		final byte[] salt = Codecs.hexDecode(user.getSalt());
		return new SimpleAuthenticationInfo(new ShiroUser(user.getId().toString(), username), user.getPassword(),
				ByteSource.Util.bytes(salt), getName());
	}

	@Override
	protected AuthorizationInfo doAuthorization(Object principal) {
		final ShiroUser shiroUser = (ShiroUser) principal;
		final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		populatePermission(info, shiroUser.getId());
		return info;
	}

	@Override
	protected String getHashAlgrithm() {
		return "SHA-256";
	}

	private CsmUser findUser(String username) {
		final CsmUser user = entityDbService.findOne(pair, Specifications.build(CsmUser.class,
				Lists.newArrayList(new FindFilter("username", Operator.EQ, username))));
		return user;
	}

	private CsmUser getUser(String id) {
		final CsmUser user = entityDbService.get(pair, UUID.fromString(id));
		return user;
	}

	private void populatePermission(SimpleAuthorizationInfo info, String id) {
		final CsmUser user = getUser(id);
		final Set<String> permissionSet = userDbService.findPermissionSetByUserGroup(user.getUserGroupId());
		info.addStringPermissions(permissionSet);
	}
}
