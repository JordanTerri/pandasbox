package org.pandasbox.commons.sip.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.pandasbox.commons.sip" })
public class SipConfiguration {
	// to load only sip component. SIP package might be extracted to its own artifact but later.
}
