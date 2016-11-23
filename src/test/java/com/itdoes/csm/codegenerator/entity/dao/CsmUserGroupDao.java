package com.itdoes.csm.codegenerator.entity.dao;

/**
 * @author Jalen Zhong
 */
public interface CsmUserGroupDao {
	@org.springframework.data.jpa.repository.Query(value = "SELECT p.permission"
			+ " FROM csm_user_group ug, csm_user_group_role ugr, csm_role r, csm_role_permission rp, csm_permission p"
			+ " WHERE ugr.user_group_id = ug.id AND ugr.role_id = r.id AND rp.role_id = r.id AND rp.permission_id = p.id"
			+ " AND (" + " ug.id = ?1" + " OR FIND_IN_SET(ug.id," + " (SELECT GROUP_CONCAT(Level SEPARATOR ',') FROM ("
			+ " SELECT @Ids \\:= (" + " SELECT GROUP_CONCAT(id SEPARATOR ',')" + " FROM csm_user_group"
			+ " WHERE FIND_IN_SET(super_id, @Ids)" + " ) Level" + " FROM csm_user_group"
			+ " JOIN (SELECT @Ids \\:= ?1) temp1" + " WHERE FIND_IN_SET(super_id, @Ids)" + " ) temp2)"
			+ " ))", nativeQuery = true)
	java.util.Set<String> findPermission(java.util.UUID userGroupId);
}
