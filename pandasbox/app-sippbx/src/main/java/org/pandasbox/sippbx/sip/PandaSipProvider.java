package org.pandasbox.sippbx.sip;

import java.util.Properties;

import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

import org.apache.log4j.Logger;

public class PandaSipProvider {

	private static final Logger logger = Logger.getLogger(PandaSipProvider.class);

	private final Properties properties;

	private SipStack sipStack;

	private HeaderFactory headerFactory;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private SipListener listener;

	public PandaSipProvider(Properties properties) {
		this.properties = properties;
	}

	public void init() throws Exception {
		SipFactory sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist");

		try {
			sipStack = sipFactory.createSipStack(properties);
		} catch (PeerUnavailableException e) {
			//TODO log
			throw e;
		}

		headerFactory = sipFactory.createHeaderFactory();
		addressFactory = sipFactory.createAddressFactory();
		messageFactory = sipFactory.createMessageFactory();

		ListeningPoint lp = null;
		try {
			lp = sipStack.createListeningPoint("127.0.0.1", ListeningPoint.PORT_5060, ListeningPoint.UDP);
		} catch (TransportNotSupportedException | InvalidArgumentException e) {
			// TODO Auto-generated catch block
			throw e;
		}

		SipProvider sipProvider = sipStack.createSipProvider(lp);

		listener = createSipListener();
		sipProvider.addSipListener(listener);
	}

	private SipListener createSipListener() {
		return new PandaSipListener();
	}

	/**
	 * @return the headerFactory
	 */
	public HeaderFactory getHeaderFactory() {
		return headerFactory;
	}

	/**
	 * @param headerFactory the headerFactory to set
	 */
	public void setHeaderFactory(HeaderFactory headerFactory) {
		this.headerFactory = headerFactory;
	}

	/**
	 * @return the addressFactory
	 */
	public AddressFactory getAddressFactory() {
		return addressFactory;
	}

	/**
	 * @param addressFactory the addressFactory to set
	 */
	public void setAddressFactory(AddressFactory addressFactory) {
		this.addressFactory = addressFactory;
	}

	/**
	 * @return the messageFactory
	 */
	public MessageFactory getMessageFactory() {
		return messageFactory;
	}

	/**
	 * @param messageFactory the messageFactory to set
	 */
	public void setMessageFactory(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}

	/**
	 * @return the listener
	 */
	public SipListener getListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(SipListener listener) {
		this.listener = listener;
	}

}
