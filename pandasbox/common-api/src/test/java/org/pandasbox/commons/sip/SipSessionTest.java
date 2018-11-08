package org.pandasbox.commons.sip;

import javax.sip.Dialog;
import javax.sip.header.CallIdHeader;

import org.junit.Before;
import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;

public class SipSessionTest {

	private static final String CALL_ID = "myCallId";

	@Injectable
	Dialog dialog;
	
	@Injectable
	CallIdHeader callIdHeader;
	
	SipSession sipSession;
	
	@Before
	public void setup() {
		new Expectations() {
			{
				dialog.getCallId(); result = callIdHeader; minTimes = 0;
				callIdHeader.getCallId(); result = CALL_ID;
			}
		};
		 sipSession = new SipSession(dialog);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		SipSession bob = new SipSession(null);
	}

	@Test
	public void getTransaction() {
		sipSession.getTransaction();
	}

	@Test
	public void getDialog() {
		sipSession.getDialog();
	}

	@Test
	public void getCallId() {
		sipSession.getCallId();
	}

	@Test
	public void getLocalTag() {
		sipSession.getLocalTag();
	}

	@Test
	public void getRemoteTag() {
		sipSession.getRemoteTag();
	}
	
	
}
