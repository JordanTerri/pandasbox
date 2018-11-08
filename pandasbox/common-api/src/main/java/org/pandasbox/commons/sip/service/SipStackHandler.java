package org.pandasbox.commons.sip.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sip.ListeningPoint;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

import org.pandasbox.commons.sip.NetworkHelper;
import org.pandasbox.commons.sip.config.SipProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SipStackHandler {

	private static final String DEFAULT_SERVER_ADDRESS = NetworkHelper.getLocalIpAddress();

	private static final Logger LOGGER = LoggerFactory.getLogger(SipStackHandler.class);

	@Autowired
	SipProperties sipProperties;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;

	private SipStack sipStack;
	
	private SipProvider sipProvider;

	private String serverAddress;

	@PostConstruct
	public void init() {
		LOGGER.info("init in progress.....");
		SipFactory sipFactory = null;
		sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist");
		Properties properties = new Properties();
		properties.setProperty("javax.sip.STACK_NAME", sipProperties.getStackName());
		
		//TODO: following should be moved to xml configuration log file.
		// You need 16 for logging traces. 32 for debug + traces.
		// Your code will limp at 32 but it is best for debugging.
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "INFO");
		properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "sipstack-debug.log");
		properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "sipstack.log");

		serverAddress = getLocalAddress();
		
		try {
			// Create SipStack object
			sipStack = sipFactory.createSipStack(properties);
			LOGGER.info("sipStack = {}", sipStack);
		} catch (PeerUnavailableException e) {
			// could not find
			// gov.nist.jain.protocol.ip.sip.SipStackImpl
			// in the classpath
			LOGGER.error(e.getMessage(), e);
			
			if (e.getCause() != null) {
				LOGGER.error("ALARM !! ALARM !! {}", e.getCause());
			}

			return;
		}

		try {
			headerFactory = sipFactory.createHeaderFactory();
			addressFactory = sipFactory.createAddressFactory();
			messageFactory = sipFactory.createMessageFactory();
			ListeningPoint lp = sipStack.createListeningPoint(serverAddress, sipProperties.getUdpPort().intValue(), ListeningPoint.UDP);

			sipProvider = sipStack.createSipProvider(lp);
		} catch (Exception ex) {
			LOGGER.info("Error initiating Sip Stack: {}", ex.getMessage(), ex);
		}
	}

	private String getLocalAddress() {
		String address = DEFAULT_SERVER_ADDRESS;
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			address = localHost.getHostAddress();
		} catch (UnknownHostException e) {
			LOGGER.error("Error trying to get local address, will use following loopback instead {}.", address, e);
		}
		return address;
	}
	
	public String getServerAddress() {
		return serverAddress;
	}

	public AddressFactory getAddressFactory() {
		return addressFactory;
	}

	public MessageFactory getMessageFactory() {
		return messageFactory;
	}

	public HeaderFactory getHeaderFactory() {
		return headerFactory;
	}

	public SipProvider getSipProvider() {
		return sipProvider;
	}
	
	public SipStack getSipStack() {
		return sipStack;
	}
}
