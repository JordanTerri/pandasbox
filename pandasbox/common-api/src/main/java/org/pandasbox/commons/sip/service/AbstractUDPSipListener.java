package org.pandasbox.commons.sip.service;

import javax.sip.ListeningPoint;
import javax.sip.SipListener;

import org.pandasbox.commons.sip.config.SipProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Use to instanciate a Spring Service representing a UDP Sip Listener that will
 * be registered to the SIP stack.
 * 
 * Use: create a class as follows :
 * 
 * <code>
 *  
 *  &#64;Service
 *  class MyListener extends DefaultAbstractUDPSipListener
 *  {
 *  	&#64;PostConstruct
 *		&#64;Override protected void initialization() {
 *			super.initialization();
 *			logger.info("your bean has been correctly loaded.");
 *		}
 *	}
 * </code>
 * 
 * 
 * @author jordan.territorio
 *
 */
public abstract class AbstractUDPSipListener implements SipListener {

	@Autowired
	protected SipStackHandler sipStackHandler;

	@Autowired
	protected SipProperties sipProperties;

	/**
	 * Initialize correctly an UDP listening port for the instanciated Sip Stack
	 * based on defined SIP properties.
	 */
	public void initialization() {
		SipListenerHelper.registerSipListener(this, sipStackHandler.getSipStack(), sipStackHandler.getServerAddress(),
				sipProperties.getUdpPort().intValue(), ListeningPoint.UDP);
	}
}
