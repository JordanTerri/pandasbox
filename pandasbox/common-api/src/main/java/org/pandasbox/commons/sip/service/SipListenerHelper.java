package org.pandasbox.commons.sip.service;

import java.util.TooManyListenersException;

import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TransportNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SipListenerHelper {

	private SipListenerHelper() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(SipListenerHelper.class);

	/**
	 * Creates a listening point on the sip stack according to arguments and thenr
	 * egisters the sip listener on it.
	 * 
	 * @param listener       the listener to register
	 * @param sipStack       the stack to register to
	 * @param address        the address of the stack
	 * @param port           the port to listen to
	 * @param listeningPoint the type of listening point
	 */
	public static void registerSipListener(SipListener listener, SipStack sipStack, String address, int port,
			String listeningPoint) {
		ListeningPoint lp;
		try {
			lp = sipStack.createListeningPoint(address, port, listeningPoint);
			SipProvider sipProvider = sipStack.createSipProvider(lp);
			sipProvider.addSipListener(listener);
		} catch (TransportNotSupportedException e) {
			LOGGER.error("The following transport is not supported {}", listeningPoint, e);
		} catch (InvalidArgumentException e) {
			LOGGER.error("Can't createa listening point with following parameter {}:{} on {}", listeningPoint,
					Integer.toString(port), address, e);
		} catch (ObjectInUseException e) {
			LOGGER.error("You already have a provider for this listening point {}:{}", listeningPoint,
					Integer.toString(port), e);
		} catch (TooManyListenersException e) {
			LOGGER.error("You already have a listener for this provider listening on {}:{}", listeningPoint,
					Integer.toString(port), e);
		}
	}
}
