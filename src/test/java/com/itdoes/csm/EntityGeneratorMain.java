package com.itdoes.csm;

import com.itdoes.common.extension.codegenerator.entity.EntityGeneratorHelper;

/**
 * @author Jalen Zhong
 */
public class EntityGeneratorMain {
	private static final String BASE_PACKAGE_NAME = EntityGeneratorMain.class.getPackage().getName();

	public static void main(String[] args) {
		EntityGeneratorHelper.generateEntities(BASE_PACKAGE_NAME, null);
	}

	private EntityGeneratorMain() {
	}
}
