package org.pandasbox.sippbx.sip;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionState;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.pandasbox.commons.sip.SipSession;
import org.pandasbox.commons.sip.config.SipProperties;
import org.pandasbox.commons.sip.service.SipStackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is a UAS template.
 *
 */
@Service
public class EchoServerSipListener implements SipListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerSipListener.class);

	@Autowired
	SipProperties sipProperties;
	
	@Autowired
	SipStackHandler sipStack;

	private final ConcurrentHashMap<String, SipSession> sessions = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		LOGGER.info("Initiating Sip Stack");
		try {
			LOGGER.info("{}", sipProperties);
			
			SipProvider sipProvider = sipStack.getSipProvider();
			sipProvider.addSipListener(this);
		} catch (Exception ex) {
			LOGGER.error("Error initiating sip listener: {}", ex.getMessage(), ex);
		}
	}

	@Override
	public void processRequest(RequestEvent requestEvent) {
		Request request = requestEvent.getRequest();
		ServerTransaction serverTransactionId = requestEvent.getServerTransaction();
		LOGGER.info("\n\nRequest {} received at {} with server transaction id {}", request.getMethod(),
				sipProperties.getStackName(), serverTransactionId);

		if (request.getMethod().equals(Request.INVITE)) {
			processInvite(requestEvent, serverTransactionId);
		} else if (request.getMethod().equals(Request.ACK)) {
			processAck(requestEvent, serverTransactionId);
		} else if (request.getMethod().equals(Request.BYE)) {
			processBye(requestEvent, serverTransactionId);
		} else {
			// we should not be here
			LOGGER.error("the following request is not supported {}", request);
		}
	}

	@Override
	public void processResponse(ResponseEvent responseEvent) {
		// nothing to do
		LOGGER.info("Received response {}", responseEvent.getResponse());
	}

	/**
	 * Process the ACK request. Send the bye and complete the call flow.
	 * 
	 * @param requestEvent
	 * @param serverTransaction
	 */
	public void processAck(final RequestEvent requestEvent, ServerTransaction serverTransaction) {
		LOGGER.info("Echo Server: got an ACK! ");
		SipProvider provider = (SipProvider) requestEvent.getSource();

		CallIdHeader header = (CallIdHeader) requestEvent.getRequest().getHeader(CallIdHeader.NAME);
		SipSession sipSession = sessions.get(header.getCallId());

		if (sipSession == null) {
			throw new IllegalStateException("Received an ACK for an unknown Call-Id");
		}

		// If the callee (so the echoserver) is supposed to send the BYE.
		Dialog dialog = sipSession.getDialog();
		try {
			if (!sipProperties.isCallerSenderBye()) {
				Request byeRequest = dialog.createRequest(Request.BYE);
				ClientTransaction ct = provider.getNewClientTransaction(byeRequest);
				dialog.sendRequest(ct);
			}
		} catch (Exception ex) {
			LOGGER.error("error occured processing Acknoledgement for {}", dialog.getCallId(), ex);
		}
	}

	/**
	 * Process the invite request.
	 * 
	 * @param requestEvent
	 * @param serverTransaction
	 */
	public void processInvite(RequestEvent requestEvent, ServerTransaction serverTransaction) {
		SipProvider sipProvider = (SipProvider) requestEvent.getSource();
		Request request = requestEvent.getRequest();
		try {
			LOGGER.info("Echo Server: got an Invite sending Trying {}", request);
			MessageFactory messageFactory = sipStack.getMessageFactory();
			Response response = messageFactory.createResponse(Response.RINGING, request);

			ServerTransaction st = requestEvent.getServerTransaction();
			if (st == null) {
				st = sipProvider.getNewServerTransaction(request);
			}
			SipSession session = new SipSession(st.getDialog());

			st.sendResponse(response);

			Response okResponse = messageFactory.createResponse(Response.OK, request);

			AddressFactory addressFactory = sipStack.getAddressFactory();
			Address address = addressFactory
					.createAddress("EchoServer <sip:" + sipStack.getServerAddress() + ":" + sipProperties.getUdpPort() + ">");
			HeaderFactory headerFactory = sipStack.getHeaderFactory();
			ContactHeader contactHeader = headerFactory.createContactHeader(address);
			response.addHeader(contactHeader);
			ToHeader toHeader = (ToHeader) okResponse.getHeader(ToHeader.NAME);
			toHeader.setTag("4321"); // Application is supposed to set.
			okResponse.addHeader(contactHeader);

			// storing session
			sessions.put(session.getCallId(), session);

			// sending response
			session.setTransaction(st);
			if (st.getState() != TransactionState.COMPLETED) {
				LOGGER.info("Echo Server: Dialog state before {}: {}", Integer.valueOf(response.getStatusCode()),
						st.getDialog().getState());
				st.sendResponse(okResponse);
				LOGGER.info("Echo Server: Dialog state after {}: {}", Integer.valueOf(response.getStatusCode()),
						st.getDialog().getState());
			}
		} catch (Exception ex) {
			LOGGER.error("error occured processing invite", ex);
		}
	}

	/**
	 * Process the bye request.
	 * 
	 * @param requestEvent
	 * @param serverTransactionId
	 */
	public void processBye(RequestEvent requestEvent, ServerTransaction serverTransactionId) {
		// SipProvider sipProvider = (SipProvider) requestEvent.getSource()
		Request request = requestEvent.getRequest();
		// Dialog dialog = requestEvent.getDialog()

		CallIdHeader header = (CallIdHeader) requestEvent.getRequest().getHeader(CallIdHeader.NAME);
		SipSession sipSession = sessions.get(header.getCallId());

		if (sipSession == null) {
			throw new IllegalStateException("Received an ACK for an unknown Call-Id");
		}
		
		
		Dialog dialog = sipSession.getDialog();
		LOGGER.info("local party = {}", dialog.getLocalParty());
		try {
			LOGGER.info("Echo Server:  got a bye sending OK.");
			MessageFactory messageFactory = sipStack.getMessageFactory();
			Response response = messageFactory.createResponse(200, request);
			serverTransactionId.sendResponse(response);
			LOGGER.info("Dialog State is " + serverTransactionId.getDialog().getState());

			sessions.remove(sipSession.getCallId());
		} catch (Exception ex) {
			LOGGER.error("Error occured while ending session", ex);
		}
	}

	@Override
	public void processTimeout(TimeoutEvent timeoutEvent) {
		Transaction transaction;
		if (timeoutEvent.isServerTransaction()) {
			transaction = timeoutEvent.getServerTransaction();
		} else {
			transaction = timeoutEvent.getClientTransaction();
		}

		LOGGER.info("state = " + transaction.getState());
		LOGGER.info("dialog = " + transaction.getDialog());
		LOGGER.info("dialogState = " + transaction.getDialog().getState());
		LOGGER.info("Transaction Time out");
	}

	@Override
	public void processIOException(IOExceptionEvent exceptionEvent) {
		LOGGER.error("IOException with source:{} host:{} port:{} protocol:{}", exceptionEvent.getSource(), exceptionEvent.getHost(),
				Integer.valueOf(exceptionEvent.getPort()), exceptionEvent.getTransport());
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
		if (transactionTerminatedEvent.isServerTransaction())
			LOGGER.warn("Transaction terminated event recieved" + transactionTerminatedEvent.getServerTransaction());
		else
			LOGGER.warn("Transaction terminated " + transactionTerminatedEvent.getClientTransaction());
	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
		Dialog d = dialogTerminatedEvent.getDialog();
		LOGGER.info("Dialog terminated event recieved. Local party= {}", d.getLocalParty());
	}

}
