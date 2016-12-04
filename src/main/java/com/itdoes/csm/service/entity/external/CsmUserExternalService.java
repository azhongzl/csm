package com.itdoes.csm.service.entity.external;

import com.itdoes.common.business.service.entity.external.EntityExternalPermFieldService;
import com.itdoes.common.business.service.entity.external.EntityExternalService;
import com.itdoes.common.business.service.entity.internal.EntityInternalService;

/**
 * @author Jalen Zhong
 */
public class CsmUserExternalService extends EntityExternalService {
	public CsmUserExternalService(EntityInternalService internalService,
			EntityExternalPermFieldService permFieldService) {
		super(internalService, permFieldService);
	}
}
