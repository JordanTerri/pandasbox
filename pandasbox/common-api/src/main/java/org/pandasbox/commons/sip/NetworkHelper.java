package org.pandasbox.commons.sip;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to make some network interaction such as get IP address.
 * 
 * @author jordan.territorio
 *
 */
public class NetworkHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkHelper.class);

	private NetworkHelper() {
	}

	/**
	 * Get local address IP which should not be localhost, unless some error occured
	 * 
	 * @return the configured IP address. localhost IP address (probably 127.0.0.1
	 *         if error occured)
	 */
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
