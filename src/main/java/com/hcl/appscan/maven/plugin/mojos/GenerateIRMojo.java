/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017, 2022. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.hcl.appscan.maven.plugin.Messages;
import com.hcl.appscan.sdk.error.AppScanException;
import com.hcl.appscan.sdk.logging.Message;

/**
 * Generates a .irx file for running security analysis. 
 */
@Mojo (name="prepare", //$NON-NLS-1$
		requiresProject=true,
		requiresDependencyResolution=ResolutionScope.COMPILE,
		defaultPhase=LifecyclePhase.PACKAGE)
@Execute( phase=LifecyclePhase.PACKAGE )
public final class GenerateIRMojo extends SASTMojo {

	@Override
	protected void run() throws MojoExecutionException {
		try {
			setIrxFile();
			getScanManager().prepare(getProgress(), getScanProperties());
			getProgress().setStatus(new Message(Message.INFO, Messages.getMessage("ir.gen.success", getIrx()))); //$NON-NLS-1$
		} catch (AppScanException  e) {
			getProgress().setStatus(e);
			throw new MojoExecutionException(Messages.getMessage("ir.gen.failed", e.getLocalizedMessage())); //$NON-NLS-1$
		}
	}
}
