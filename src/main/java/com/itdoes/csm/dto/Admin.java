package com.itdoes.csm.dto;

import java.io.Serializable;
import java.util.UUID;

import com.itdoes.common.business.Perms;

/**
 * @author Jalen Zhong
 */
public class Admin {
	private static final Admin INSTANCE = new Admin();

	public static Admin getInstance() {
		return INSTANCE;
	}

	private final UUID id = UUID.fromString("1c93b13d-d6ea-1034-a268-c6b53a0158b7");
	private final String idString = id.toString();
	private final String username = "admin";
	private final String password = "84c59723099cc04ec1772ebc455643a0d29ce6e38c945f9cc9f759a6c3b21136";
	private final String salt = "31471bc8bc333a18";
	private final String permission = Perms.getAllPerm();

	private Admin() {
	}

	public UUID getId() {
		return id;
	}

	public String getIdString() {
		return idString;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getSalt() {
		return salt;
	}

	public String getPermission() {
		return permission;
	}

	public boolean isAdminById(Serializable otherId) {
		return id.equals(otherId);
	}

	public boolean isAdminById(String otherId) {
		return idString.equals(otherId);
	}

	public boolean isAdminByUsername(String otherUsername) {
		return username.equalsIgnoreCase(otherUsername);
	}
}
