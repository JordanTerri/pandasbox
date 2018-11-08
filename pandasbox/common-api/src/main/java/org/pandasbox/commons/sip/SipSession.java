package org.pandasbox.commons.sip;

import javax.sip.Dialog;
import javax.sip.ServerTransaction;

public class SipSession {

	/**
	 * Transaction represents an exchange of messages between server and client. It starts with a first request, and ends to its
	 * final response (non 1xxx) response. (IE.: from the INVITE to the OK).
	 */
	private ServerTransaction transaction;

	/**
	 * Dialog represents an exchange of messages between server and client which persist in time. It is identified by a Call-Id, a
	 * local tag and a remote tag. (IE.: from the first INVITE transaction to the final BYE transaction).
	 */
	private final Dialog dialog;

	private final String callId;

	private final String localTag;

	private final String remoteTag;

	public SipSession(Dialog dialog) {
		if (dialog == null) {
			throw new IllegalArgumentException("Can't create sip session without dialog");
		}
		this.dialog = dialog;
		this.callId = dialog.getCallId().getCallId();
		this.localTag = dialog.getLocalTag();
		this.remoteTag = dialog.getRemoteTag();
	}

	public ServerTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(ServerTransaction transaction) {
		this.transaction = transaction;
	}

	public Dialog getDialog() {
		return dialog;
	}

	public String getCallId() {
		return callId;
	}

	public String getLocalTag() {
		return localTag;
	}

	public String getRemoteTag() {
		return remoteTag;
	}

}
