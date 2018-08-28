/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.managers;

import org.apache.maven.plugin.logging.Log;

import com.hcl.appscan.sdk.logging.IProgress;
import com.hcl.appscan.sdk.logging.Message;

public class MavenProgress implements IProgress {

	private Log m_log;
	
	public MavenProgress(Log log) {
		m_log = log;
	}
	
	@Override
	public void setStatus(Message message) {
		log(message);
	}

	@Override
	public void setStatus(Throwable e) {
		logError(new Message(Message.ERROR, e.getLocalizedMessage()));
	}

	@Override
	public void setStatus(Message message, Throwable e) {
		logInfo(message);
		logError(new Message(Message.ERROR, e.getLocalizedMessage()));
	}

	private void log(Message message) {
		switch(message.getSeverity()) {
			case Message.ERROR:
				logError(message);
				break;
			case Message.WARNING:
				logWarning(message);
				break;
			default:
				logInfo(message);
		}
	}
	
	private void logError(Message error) {
		m_log.error(error.getText());
	}
	
	private void logWarning(Message warning) {
		m_log.warn(warning.getText());
	}
	
	private void logInfo(Message info) {
		m_log.info(info.getText());
	}
}
