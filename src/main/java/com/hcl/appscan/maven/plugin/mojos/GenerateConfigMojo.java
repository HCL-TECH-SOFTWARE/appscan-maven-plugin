/**
 * © Copyright HCL Technologies Ltd. 2022. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.mojos;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.hcl.appscan.maven.plugin.Messages;
import com.hcl.appscan.sdk.error.AppScanException;
import com.hcl.appscan.sdk.logging.Message;
import com.hcl.appscan.sdk.scanners.sast.xml.IModelXMLConstants;

/**
 * Generates an appscan-config.xml based on the projects in the build. 
 */
@Mojo (name="generate-appscan-config", //$NON-NLS-1$
		requiresProject=true,
		requiresDependencyResolution=ResolutionScope.COMPILE,
		defaultPhase=LifecyclePhase.PACKAGE)
@Execute( phase=LifecyclePhase.PACKAGE )
public class GenerateConfigMojo extends SASTMojo {

	@Override
	protected void run() throws MojoExecutionException {
		try {
			getScanManager().createConfig();
			File configFile = new File(m_targetDir, IModelXMLConstants.APPSCAN_CONFIG + IModelXMLConstants.DOT_XML);
			
			if(configFile.isFile()) {
				getProgress().setStatus(new Message(Message.INFO, Messages.getMessage("ir.gen.success", configFile.getAbsolutePath()))); //$NON-NLS-1$
			}
			else {
				throw new AppScanException(Messages.getMessage("no.targets.found")); //$NON-NLS-1$
			}
		} catch (AppScanException e) {
			getProgress().setStatus(e);
			throw new MojoExecutionException(Messages.getMessage("generate.config.failed", e.getLocalizedMessage())); //$NON-NLS-1$
		}
	}
}
