package org.pandasbox.sippbx.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.pandasbox.commons.sip.config", "org.pandasbox.sippbx.sip"})
public class RootConfiguration {

}
