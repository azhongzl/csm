package com.itdoes.csm.service.init;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.service.BaseService;
import com.itdoes.csm.service.UserCacheService;

/**
 * Initialization space for all beans to avoid "circular reference"
 * 
 * @author Jalen Zhong
 */
@Service
public class InitService extends BaseService {
	@Autowired
	private UserCacheService userCacheService;

	@PostConstruct
	public void myInit() {
		userCacheService.init();
	}
}
