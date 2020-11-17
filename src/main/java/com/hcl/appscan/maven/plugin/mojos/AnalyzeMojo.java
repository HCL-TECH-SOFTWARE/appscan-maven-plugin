package com.hcl.appscan.maven.plugin.mojos;

import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.hcl.appscan.maven.plugin.Messages;
import com.hcl.appscan.maven.plugin.auth.MavenAuthenticationProvider;
import com.hcl.appscan.sdk.CoreConstants;
import com.hcl.appscan.sdk.auth.IAuthenticationProvider;
import com.hcl.appscan.sdk.error.AppScanException;
import com.hcl.appscan.sdk.logging.Message;
import com.hcl.appscan.sdk.scan.CloudScanServiceProvider;
import com.hcl.appscan.sdk.scan.IScanServiceProvider;

/**
 * Generates a .irx file and submits it to HCL Appscan for analysis.
 */
@Mojo (name="analyze", //$NON-NLS-1$
		requiresProject=true,
		requiresDependencyResolution=ResolutionScope.COMPILE,
		defaultPhase=LifecyclePhase.PACKAGE)
@Execute( phase=LifecyclePhase.PACKAGE )
public final class AnalyzeMojo extends SASTMojo {

	/**
	* The api key for authenticating with the service.
	*/ 
	@Parameter (property="appscanKey", required=true, readonly=true) //$NON-NLS-1$
	private String appscanKey;
	
	/**
	* The secret for authenticating with the service.
	*/ 
	@Parameter (property="appscanSecret", required=true, readonly=true) //$NON-NLS-1$
	private String appscanSecret;
	
	/**
	* The application ID to associate the scan with.
	*/ 
	@Parameter (property="appId", required=true, readonly=true) //$NON-NLS-1$
	private String appId;
	
	@Override
	protected void run() throws MojoExecutionException {
		try {
			Map<String, String> properties = getScanProperties();
			properties.put(CoreConstants.APP_ID, appId);
			getScanManager().analyze(getProgress(), properties, getServiceProvider());
			getProgress().setStatus(new Message(Message.INFO, Messages.getMessage("ir.analyze.success", getIrx()))); //$NON-NLS-1$
			getProgress().setStatus(new Message(Message.INFO, getScanManager().getScanId()))); //$NON-NLS-1$
		} catch (AppScanException  e) {
			getProgress().setStatus(e);
			throw new MojoExecutionException(Messages.getMessage("ir.analyze.failed", e.getLocalizedMessage())); //$NON-NLS-1$
		}
	}
	
	private IScanServiceProvider getServiceProvider() throws AppScanException {
		IAuthenticationProvider authProvider = new MavenAuthenticationProvider(appscanKey, appscanSecret);
		IScanServiceProvider serviceProvider = new CloudScanServiceProvider(getProgress(), authProvider);
		return serviceProvider;
	}
}