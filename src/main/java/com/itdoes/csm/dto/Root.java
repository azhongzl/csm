package com.itdoes.csm.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Jalen Zhong
 */
public class Root {
	private static final Root INSTANCE = new Root();

	public static Root getInstance() {
		return INSTANCE;
	}

	private final UUID id = UUID.fromString("1db1a22e-757a-4113-b2a3-8797d91fc457");
	private final String idString = id.toString();
	private final String name = "root";

	private Root() {
	}

	public UUID getId() {
		return id;
	}

	public String getIdString() {
		return idString;
	}

	public String getName() {
		return name;
	}

	public boolean isRootById(Serializable otherId) {
		return id.equals(otherId);
	}

	public boolean isRootById(String otherIdString) {
		return idString.equals(otherIdString);
	}

	public boolean isRootByName(String otherName) {
		return name.equalsIgnoreCase(otherName);
	}
}
