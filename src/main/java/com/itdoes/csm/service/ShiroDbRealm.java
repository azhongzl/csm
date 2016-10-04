package com.itdoes.csm.service;

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
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Env;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.business.service.FacadeTransactionalService;
import com.itdoes.common.core.jpa.SearchFilter;
import com.itdoes.common.core.jpa.SearchFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.shiro.AbstractShiroRealm;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.util.Codecs;
import com.itdoes.csm.entity.Account;

/**
 * @author Jalen Zhong
 */
public class ShiroDbRealm extends AbstractShiroRealm {
	@Autowired
	private Env env;

	@Autowired
	private FacadeTransactionalService facadeService;

	private EntityPair<Account, UUID> pair;

	@PostConstruct
	public void myInit() {
		pair = env.getEntityPair(Account.class.getSimpleName());
	}

	@Override
	protected AuthenticationInfo doAuthentication(UsernamePasswordToken token) throws AuthenticationException {
		final String username = token.getUsername();
		final Account account = searchAccount(username);
		final byte[] salt = Codecs.hexDecode(account.getSalt());
		return new SimpleAuthenticationInfo(new ShiroUser(account.getId().toString(), username), account.getPassword(),
				ByteSource.Util.bytes(salt), getName());
	}

	@Override
	protected AuthorizationInfo doAuthorization(Object principal) {
		final ShiroUser shiroUser = (ShiroUser) principal;
		final Account account = getAccount(shiroUser.getId());
		final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(Permissions.getAllPermissions());
		return info;
	}

	@Override
	protected String getHashAlgrithm() {
		return "SHA-256";
	}

	private Account searchAccount(String username) {
		final Account account = facadeService.searchOne(pair, Specifications.build(Account.class,
				Lists.newArrayList(new SearchFilter("username", Operator.EQ, username))));
		return account;
	}

	private Account getAccount(String id) {
		final Account account = facadeService.get(pair, UUID.fromString(id));
		return account;
	}
}
