package org.pandasbox.commons.sip;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkHelper.class);

	private NetworkHelper(){}
	
	public static String getLocalIpAddress() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LOGGER.error("error getting host address, returning loopback.", e);
			ip = InetAddress.getLoopbackAddress().getHostAddress();
		}
		return ip;
	}
}
