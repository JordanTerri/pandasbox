package org.pandasbox.commons.sip.service;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

import org.junit.Before;
import org.junit.Test;
import org.pandasbox.commons.sip.config.SipProperties;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;

public class SipStackHandlerTest {

	SipStackHandler handler;
	
	@Injectable
	SipProperties sipProperties;

	@Injectable
	private AddressFactory addressFactory;

	@Injectable
	private MessageFactory messageFactory;

	@Injectable
	private HeaderFactory headerFactory;

	@Injectable
	private SipStack sipStack;
	
	@Injectable
	private SipProvider sipProvider;
	
	@Injectable
	SipFactory sipFactory;

	@Before
	public void setup() {
		handler = new SipStackHandler();
		Deencapsulation.setField(handler, sipProperties);
		//Deencapsulation.setField(handler, sipStack);
		//Deencapsulation.setField(handler, addressFactory);
		//Deencapsulation.setField(handler, messageFactory);
		//Deencapsulation.setField(handler, headerFactory);
		//Deencapsulation.setField(handler, sipProvider);
		
		new Expectations(SipFactory.class) {
			{
				sipProperties.getStackName(); result = "stackname"; minTimes = 0;
				sipProperties.getUdpPort(); result = 5060; minTimes=0;
				SipFactory.getInstance(); result = sipFactory; minTimes = 0;
			}
		};
	}
	
	@Test
	public void init_PeerUnavailbleException() throws PeerUnavailableException {
		
		new Expectations() {
			{
				sipFactory.createSipStack((Properties)any); result = new PeerUnavailableException();
				
				sipFactory.createHeaderFactory(); times = 0;
				addressFactory = sipFactory.createAddressFactory();times = 0;
				messageFactory = sipFactory.createMessageFactory();times = 0;
			}
		};
		handler.init();
	}
	
	@Test
	public void init_Success() throws ObjectInUseException, TransportNotSupportedException, InvalidArgumentException {
		new Expectations() {
			{
				sipStack.createListeningPoint(anyString, 5060, ListeningPoint.UDP);
				sipStack.createSipProvider((ListeningPoint)any); times = 1; result = sipProvider;
			}
		};
		handler.init();
		
		assertEquals(sipProvider, handler.getSipProvider());
	}

	@Test
	public void getServerAddress() {
		handler.getServerAddress();
	}

	@Test
	public void getAddressFactory() {
		handler.getAddressFactory();
	}

	@Test
	public void getMessageFactory() {
		handler.getMessageFactory();
	}

	@Test
	public void getHeaderFactory() {
		handler.getHeaderFactory();
	}

	@Test
	public void getSipProvider() {
		handler.getSipProvider();
	}

	@Test
	public void getSipStack() {
		handler.getSipStack();
	}
	
}
