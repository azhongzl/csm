package com.itdoes.csm.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Jalen Zhong
 */
public class Root {
	private static final UUID ID = UUID.fromString("1db1a22e-757a-4113-b2a3-8797d91fc457");
	private static final String ID_STRING = ID.toString();
	private static final String NAME = "root";

	public static UUID getId() {
		return ID;
	}

	public static String getIdString() {
		return ID_STRING;
	}

	public static String getName() {
		return NAME;
	}

	public static boolean isRootById(Serializable otherId) {
		return ID.equals(otherId);
	}

	public static boolean isRootById(String otherIdString) {
		return ID_STRING.equals(otherIdString);
	}

	public static boolean isRootByName(String otherName) {
		return NAME.equalsIgnoreCase(otherName);
	}

	private Root() {
	}
}
