package org.pandasbox.commons.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.pandasbox.commons" })
public class CommonsConfiguration {
	//common beans that could be declared.
	//alternatively it also allows external to look for commons config
}
