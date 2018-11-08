package org.pandasbox.commons.sip.service;

import javax.sip.ListeningPoint;
import javax.sip.SipListener;

import org.pandasbox.commons.sip.config.SipProperties;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractTCPSipListener implements SipListener {

	@Autowired
	protected SipStackHandler sipStackHandler;
	
	@Autowired
	protected SipProperties sipProperties;
		
	protected void initialization() {
		SipListenerHelper.registerSipListener(this, sipStackHandler.getSipStack(), sipStackHandler.getServerAddress(),
				sipProperties.getTcpPort().intValue(), ListeningPoint.TCP);
	}
}
