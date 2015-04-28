package org.pandasbox.sippbx;

import org.apache.log4j.Logger;
import org.pandasbox.sippbx.core.PandaRecorderApp;

public class Launcher {

	private static final Logger logger = Logger.getLogger(Launcher.class);

	public static void main(String[] args) {
		logger.info("----------");
		logger.info("Launching Recorder Implementation");

		//git test adding comment
		PandaRecorderApp app = new PandaRecorderApp("configuration.properties");
		app.start();
	}
}
