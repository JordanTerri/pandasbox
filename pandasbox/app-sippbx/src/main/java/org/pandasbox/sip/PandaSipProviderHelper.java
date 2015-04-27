package org.pandasbox.sip;

import java.util.Properties;

import javax.sip.PeerUnavailableException;


public class PandaSipProviderHelper {

	public static PandaSipProvider createSipProvider(Properties properties) throws PeerUnavailableException {
		PandaSipProvider provider = new PandaSipProvider(properties);
		return provider;
	}
}
