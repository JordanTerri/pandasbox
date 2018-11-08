package org.pandasbox.sippbx.sip;

import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipProvider;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;

import org.junit.Before;
import org.junit.Test;
import org.pandasbox.commons.sip.config.SipProperties;
import org.pandasbox.commons.sip.service.SipStackHandler;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

public class SimpleUDPServerTest {

	@Tested SimpleUDPServer listener;

	@Injectable
	RequestEvent requestEvent;

	@Injectable
	ResponseEvent responseEvent;

	@Injectable
	ServerTransaction serverTransaction;

	@Injectable
	IOExceptionEvent exceptionEvent;

	@Injectable
	TimeoutEvent timeoutEvent;

	@Injectable
	TransactionTerminatedEvent transactionTerminatedEvent;

	@Injectable
	DialogTerminatedEvent dialogTerminatedEvent;
	
	@Injectable
	Request request;
	
	@Injectable
	CallIdHeader callIdHeader;
	
	@Injectable
	ToHeader toHeader;
	
	@Injectable
	SipProvider sipProvider;
	
	@Injectable
	SipStackHandler sipStackHandler;
	
	@Injectable
	SipProperties sipProperties;
	
	private final static String CALL_ID = "myCallIdForTest";

	@Before
	public void setup() {
		listener = new SimpleUDPServer();
		
		Deencapsulation.setField(listener, "sipStackHandler", sipStackHandler);
		Deencapsulation.setField(listener, "sipProperties", sipProperties);

		
		new Expectations() {
			{
				requestEvent.getRequest(); result = request; minTimes = 0;
				requestEvent.getServerTransaction(); result = serverTransaction; minTimes = 0;
				request.getHeader(CallIdHeader.NAME); result = callIdHeader; minTimes = 0;
				request.getHeader(ToHeader.NAME); result = toHeader; minTimes = 0;
				callIdHeader.getCallId(); result = CALL_ID; minTimes = 0;
			}
		};
	}

	@Test
	public void init() {
		listener.initialization();
	}

	@Test
	public void processRequest_processInvite() {
		new Expectations(listener) {
			{
				request.getMethod(); result = Request.INVITE;
				listener.processInvite(requestEvent, serverTransaction); times = 1;
			}
		};
		listener.processRequest(requestEvent);
	}
	
	@Test
	public void processRequest_processBye() {
		new Expectations(listener) {
			{
				request.getMethod(); result = Request.BYE;
				listener.processBye(requestEvent, serverTransaction); times = 1;
			}
		};
		listener.processRequest(requestEvent);
	}
	
	@Test
	public void processRequest_processAck() {
		new Expectations(listener) {
			{
				request.getMethod(); result = Request.ACK;
				listener.processAck(requestEvent, serverTransaction); times = 1;
			}
		};
		listener.processRequest(requestEvent);
	}
	
	@Test
	public void processRequest_nothingToDo() {
		new Expectations(listener) {
			{
				request.getMethod(); result = Request.MESSAGE;
				listener.processInvite(requestEvent, serverTransaction); times = 0;
				listener.processBye(requestEvent, serverTransaction); times = 0;
				listener.processAck(requestEvent, serverTransaction); times = 0;
			}
		};
		listener.processRequest(requestEvent);
	}

	@Test(expected=IllegalStateException.class)
	public void processBye_noSessionFound() {
		listener.processBye(requestEvent, serverTransaction);
	}
	
	@Test(expected=IllegalStateException.class)
	public void processAck_noSessionFound() {
		listener.processAck(requestEvent, serverTransaction);
	}
	
	@Test
	public void processResponse() {
		listener.processResponse(responseEvent);
	}

	@Test
	public void processInvite() {
		listener.processInvite(requestEvent, serverTransaction);
	}

	

	@Test
	public void processTimeout() {
		listener.processTimeout(timeoutEvent);
	}

	@Test
	public void processIOException() {
		listener.processIOException(exceptionEvent);
	}

	@Test
	public void processTransactionTerminated() {
		listener.processTransactionTerminated(transactionTerminatedEvent);
	}

	@Test
	public void processDialogTerminated() {
		listener.processDialogTerminated(dialogTerminatedEvent);
	}

}
