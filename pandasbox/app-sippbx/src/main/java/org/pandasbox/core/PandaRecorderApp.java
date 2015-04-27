package org.pandasbox.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sip.PeerUnavailableException;

import org.apache.log4j.Logger;
import org.pandasbox.sip.PandaSipProvider;
import org.pandasbox.sip.PandaSipProviderHelper;

public class PandaRecorderApp {

	private static final Logger logger = Logger.getLogger(PandaRecorderApp.class);

	private final String fileConfig;

	private PandaSipProvider sipProvider;

	public PandaRecorderApp(String configFile) {
		fileConfig = configFile;
	}

	public void start() {
		init();
		logger.info("recorder impl successfully initialized.");

		startRecording();
		logger.info("recorder impl successfully started.");
	}

	private void startRecording() {
		logger.info("starting recorder impl.");
	}

	private void init() {
		logger.info("initializing recorder impl.");
		//TODO
		Properties properties;
		try {
			properties = getProperties();

			sipProvider = PandaSipProviderHelper.createSipProvider(properties);

			sipProvider.init();
		} catch (IOException e) {
			logger.error("Error recovering configuration.", e);
			System.exit(0);
		} catch (PeerUnavailableException e) {
			logger.error("Error creating sip provider.", e);
			System.exit(0);
		} catch (Exception e) {
			logger.error("Error initializing sip provider.", e);
			System.exit(0);
		}


	}

	private Properties getProperties() throws IOException {
		Properties prop = new Properties();
		try (InputStream iStream = getClass().getClassLoader().getResourceAsStream(fileConfig)) {
			prop.load(iStream);
			return prop;
		} catch (IOException e) {
			logger.error("Error occured trying to get properties from " + fileConfig);
			throw e;
		}
	}
}
