package org.pandasbox;

import org.apache.log4j.Logger;
import org.pandasbox.core.PandaRecorderApp;

public class Launcher {

	private static final Logger logger = Logger.getLogger(Launcher.class);

	public static void main(String[] args) {
		logger.info("----------");
		logger.info("Launching Recorder Impl");

		PandaRecorderApp app = new PandaRecorderApp("configuration.properties");
		app.start();
	}
}
