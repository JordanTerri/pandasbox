package org.pandasbox.sippbx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.pandasbox.sippbx.config")
public class App {
	private static final Logger lOGGER = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		lOGGER.info("Launching Sip PBX Implementation");
		SpringApplication.run(App.class, args);
	}
}
