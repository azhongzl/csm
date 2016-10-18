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
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
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
	private EntityEnv env;

	@Autowired
	private EntityDbService dbService;

	private EntityPair<Account, UUID> pair;

	@PostConstruct
	public void myInit() {
		pair = env.getPair(Account.class.getSimpleName());
	}

	@Override
	protected AuthenticationInfo doAuthentication(UsernamePasswordToken token) throws AuthenticationException {
		final String username = token.getUsername();
		final Account account = findAccount(username);
		if (account == null) {
			return null;
		}

		final byte[] salt = Codecs.hexDecode(account.getSalt());
		return new SimpleAuthenticationInfo(new ShiroUser(account.getId().toString(), username), account.getPassword(),
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

	private Account findAccount(String username) {
		final Account account = dbService.findOne(pair, Specifications.build(Account.class,
				Lists.newArrayList(new FindFilter("username", Operator.EQ, username))));
		return account;
	}

	private Account getAccount(String id) {
		final Account account = dbService.get(pair, UUID.fromString(id));
		return account;
	}

	private void populatePermission(SimpleAuthorizationInfo info, String id) {
		final Account account = getAccount(id);
		if (account.getUsername().equals("admin")) {
			info.addStringPermission(Perms.getAllPerm());
		}
	}
}
