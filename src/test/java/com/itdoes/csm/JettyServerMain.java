package com.itdoes.csm;

import com.itdoes.common.core.test.jetty.JettyServerHelper;
import com.itdoes.common.core.test.spring.Profiles;

/**
 * @author Jalen Zhong
 */
public class JettyServerMain {
	private static final int PORT = 8080;
	private static final String CONTEXT_PATH = "/csm";
	private static final String[] TLD_JAR_NAMES = new String[] { "shiro-web" };

	public static void main(String[] args) {
		Profiles.activeProfile(Profiles.DEVELOPMENT);
		JettyServerHelper.createAndStart(PORT, CONTEXT_PATH, TLD_JAR_NAMES);
	}

	private JettyServerMain() {
	}
}
