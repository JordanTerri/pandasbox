package org.pandasbox.commons.sip.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "org.pandasbox.commons.sip")
public class SipProperties {

	private boolean callerSenderBye;

	private String stackName;

	private Integer udpPort;

	private Integer tcpPort;

	private Integer tlsPort;

	public Integer getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(Integer udpPort) {
		this.udpPort = udpPort;
	}

	public Integer getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(Integer tcpPort) {
		this.tcpPort = tcpPort;
	}

	public Integer getTlsPort() {
		return tlsPort;
	}

	public void setTlsPort(Integer tlsPort) {
		this.tlsPort = tlsPort;
	}

	public boolean isCallerSenderBye() {
		return callerSenderBye;
	}

	public void setCallerSenderBye(boolean callerSenderBye) {
		this.callerSenderBye = callerSenderBye;
	}

	public String getStackName() {
		return stackName;
	}

	public void setStackName(String stackName) {
		this.stackName = stackName;
	}

	@Override
	public String toString() {
		return "SipProperties [callerSenderBye=" + callerSenderBye + ", stackName=" + stackName + ", udpPort=" + udpPort
				+ ", tcpPort=" + tcpPort + ", tlsPort=" + tlsPort + "]";
	}

}
