package com.itdoes.csm.service.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.spring.SpringDatas;
import com.itdoes.csm.dto.Root;
import com.itdoes.csm.entity.CsmUser;
import com.itdoes.csm.entity.CsmUserGroup;
import com.itdoes.csm.service.UserCacheService;

/**
 * @author Jalen Zhong
 */
@Service
public class AdminUserUiService extends BaseService {
	private static class UserGroupComparator implements Comparator<CsmUserGroup> {
		private static final UserGroupComparator INSTANCE = new UserGroupComparator();

		@Override
		public int compare(CsmUserGroup o1, CsmUserGroup o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	}

	private static final Root ROOT = Root.getInstance();

	@Autowired
	private EntityEnv env;

	private EntityPair<CsmUser, UUID> userPair;

	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		userPair = env.getPair(CsmUser.class.getSimpleName());
	}

	public Result listForm(int pageNo, int pageSize) {
		return Result.success().addData("userList",
				userPair.db().findPage(userPair,
						Specifications.build(CsmUser.class,
								Lists.newArrayList(new FindFilter("id", Operator.NEQ, ROOT.getIdString()))),
						SpringDatas.newPageRequest(pageNo, pageSize, DEFAULT_MAX_PAGE_SIZE,
								SpringDatas.newSort("username", true))));
	}

	public Result postForm() {
		return Result.success().addData("userGroupList", getUserGroupList());
	}

	public Result post(CsmUser user) {
		Validate.isTrue(StringUtils.isNotBlank(user.getUsername()), "Username should not be blank");
		Validate.isTrue(StringUtils.isNotBlank(user.getPlainPassword()), "Password should not be blank");
		Validate.isTrue(userCacheService.getUserId(user.getUsername()) == null, "User [%s] exists", user.getUsername());
		Validate.isTrue(!ROOT.isRootByName(user.getUsername()), "Cannot create root User");
		Validate.isTrue(!ROOT.isRootById(user.getUserGroupId()), "Cannot assign user to root UserGroup");

		user.populatePassword();
		user = userPair.db().post(userPair, user);
		userCacheService.addUser(user);
		return Result.success().addData("id", user.getId());
	}

	public Result putForm(String id) {
		return Result.success().addData("user", userCacheService.getUser(id)).addData("userGroupList",
				getUserGroupList());
	}

	public CsmUser getEntity(String id) {
		return userCacheService.getUser(id);
	}

	public Result put(CsmUser user, CsmUser oldUser) {
		Validate.isTrue(user.getUsername().equals(oldUser.getUsername()), "Cannot modify username");
		Validate.isTrue(!ROOT.isRootByName(user.getUsername()) && !ROOT.isRootById(user.getId()),
				"Cannot modify root User");
		Validate.isTrue(!ROOT.isRootById(user.getUserGroupId()), "Cannot assign user to root UserGroup");

		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.populatePassword();
		}
		userPair.db().put(userPair, user, oldUser);
		userCacheService.modifyUser(user);
		return Result.success();
	}

	public Result delete(String id) {
		Validate.isTrue(!ROOT.isRootById(id), "Cannot remove root User");

		userPair.db().delete(userPair, UUID.fromString(id));
		userCacheService.removeUser(id);
		return Result.success();
	}

	private List<CsmUserGroup> getUserGroupList() {
		final List<CsmUserGroup> userGroupList = Lists
				.newArrayListWithCapacity(userCacheService.getUserGroupMap().size() - 1);
		for (CsmUserGroup userGroup : userCacheService.getUserGroupMap().values()) {
			if (!ROOT.isRootById(userGroup.getId())) {
				userGroupList.add(userGroup);
			}
		}
		Collections.sort(userGroupList, UserGroupComparator.INSTANCE);
		return userGroupList;
	}
}
