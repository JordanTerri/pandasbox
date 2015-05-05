package org.pandasbox.sippbx.recorder;

import org.apache.log4j.Logger;
import org.pandasbox.recorder.core.RecorderService;
import org.pandasbox.sippbx.sip.PandaSipProvider;

public class RecorderProxy {
	
	private static final Logger logger = Logger.getLogger(RecorderProxy.class);
	
	RecorderService service;
	
	public void init(){
		logger.info(service);
	}
}
