package com.itdoes.csm.dao;

import com.itdoes.csm.entity.CsmUserGroup;

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
public interface CsmUserGroupDao extends com.itdoes.common.business.dao.BaseDao<CsmUserGroup, java.util.UUID> {
	@org.springframework.data.jpa.repository.Query(value = "select p.permission"
			+ " from csm_user_group ug, csm_user_group_role ugr, csm_role r, csm_role_permission rp, csm_permission p"
			+ " where ug.id = ugr.user_group_id and ugr.role_id = r.id and rp.role_id = r.id and rp.permission_id = p.id and ug.id = :userGroupId", nativeQuery = true)
	java.util.Set<String> findPermissionById(
			@org.springframework.data.repository.query.Param("userGroupId") java.util.UUID userGroupId);
}