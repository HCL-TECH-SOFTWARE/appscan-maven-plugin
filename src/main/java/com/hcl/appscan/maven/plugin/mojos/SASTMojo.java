/**
 * © Copyright HCL Technologies Ltd. 2017-2024. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.mojos;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.maven.plugin.Messages;
import com.hcl.appscan.maven.plugin.targets.MavenTarget;
import com.hcl.appscan.maven.plugin.util.MavenUtil;
import com.hcl.appscan.sdk.CoreConstants;
import com.hcl.appscan.sdk.logging.Message;
import com.hcl.appscan.sdk.scanners.sast.SASTConstants;
import com.hcl.appscan.sdk.scanners.sast.SASTScanManager;
import com.hcl.appscan.sdk.scanners.sast.targets.GenericTarget;
import com.hcl.appscan.sdk.utils.FileUtil;

public abstract class SASTMojo extends AppScanMojo {

	/**
	 * The .irx file.
	 */
	@Parameter (property="output", alias="output", defaultValue="", required=false, readonly=false) //$NON-NLS-1$ //$NON-NLS-2$
	private String m_output;
	
	/**
	 * Scan source code rather than build output.
	 */
	@Parameter (property="sourceCodeOnly", alias="sourceCodeOnly", defaultValue="false", required=false, readonly=false) //$NON-NLS-1$ //$NON-NLS-2$
	private Boolean m_isSourceCodeOnly;
	
	/**
	 * Only scan for known vulnerabilities in dependencies. Disables static analysis.
	 */
	@Parameter (property="openSourceOnly", alias="openSourceOnly", defaultValue="false", required=false, readonly=false) //$NON-NLS-1$ //$NON-NLS-2$
	private Boolean m_isOpenSourceOnly;
	
	/**
	 * Only run static analysis. Disables scanning for known vulnerabilities in dependencies.
	 */
	@Parameter (property="staticAnalysisOnly", alias="staticAnalysisOnly", defaultValue="false", required=false, readonly=false) //$NON-NLS-1$ //$NON-NLS-2$
	private Boolean m_isStaticAnalysisOnly;

	/**
	 * Ignore untrusted certificates when connecting to AppScan 360. Only intended for testing purposes. Not applicable to AppScan on Cloud.
	 */
	@Parameter (property="acceptssl", alias="acceptssl", defaultValue="false", required=false, readonly=false) //$NON-NLS-1$ //$NON-NLS-2$
	private Boolean m_acceptssl;
	
	/**
	 * The AppScan 360 service url. Not applicable to AppScan on Cloud.
	 */
	@Parameter (property="serviceUrl", alias="serviceUrl", required=false, readonly=false) //$NON-NLS-1$ //$NON-NLS-2$
	private String m_serviceUrl;
	
	private File m_irx;
	private SASTScanManager m_scanManager;
	
	@Override
	protected void initialize() {
		super.initialize();
		m_scanManager = new SASTScanManager(m_targetDir);
		m_scanManager.setIsSourceCodeOnlyEnabled(m_isSourceCodeOnly);
		m_scanManager.setIsOpenSourceOnlyEnabled(m_isOpenSourceOnly);
		m_scanManager.setIsStaticAnalysisOnlyEnabled(m_isStaticAnalysisOnly);
	}
	
	@Override
   	public void execute() throws MojoExecutionException, MojoFailureException {    		

		if(isLastProject(m_project)) {
			initialize();
    		
    		for(MavenProject project : m_projects)
    			addScanTarget(project);
 			
    		run();
    	}
    }
	
	protected SASTScanManager getScanManager() {
		return m_scanManager;
	}
	
	protected File getIrx() {
		return m_irx;
	}
	
	protected String getServiceUrl() {
		return m_serviceUrl;
	}
	
	protected boolean shouldAcceptSSL() {
		return m_acceptssl;
	}
	
	protected Map<String, String> getScanProperties() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(CoreConstants.SCAN_NAME, getScanName());
		properties.put(SASTConstants.SAVE_LOCATION, m_irx.getParent());
		properties.put(SASTConstants.APPSCAN_IRGEN_CLIENT, "Maven"); //$NON-NLS-1$
		properties.put(SASTConstants.APPSCAN_CLIENT_VERSION, m_runtimeInformation.getMavenVersion());
		properties.put(SASTConstants.IRGEN_CLIENT_PLUGIN_VERSION, getPluginVersion());
		properties.put(CoreConstants.CLIENT_TYPE, getClientType());
		properties.put(CoreConstants.SERVER_URL, m_serviceUrl);
		properties.put(CoreConstants.ACCEPT_INVALID_CERTS, Boolean.toString(m_acceptssl));
		return properties;
	}
	
	protected abstract void run() throws MojoExecutionException;

	private void addScanTarget(MavenProject project) {
		if(shouldSkipProject(project)){
			return;
		}else if(m_isSourceCodeOnly){
			for(String sourceRoot : project.getCompileSourceRoots()){
				m_scanManager.addScanTarget(new GenericTarget(sourceRoot));
			}
			if(project.getPackaging().equalsIgnoreCase(IMavenConstants.WAR)){
				String warSourceDir = MavenUtil.getPluginConfigurationProperty(project, IMavenConstants.WAR_KEY, "warSourceDirectory");
				if(warSourceDir!=null){
					m_scanManager.addScanTarget(new GenericTarget(warSourceDir));
				} else {
					m_scanManager.addScanTarget(new GenericTarget(new File(project.getBasedir(), "src/main/webapp").getAbsolutePath()));
				}
			}			
		} else {
			m_scanManager.addScanTarget(new MavenTarget(project));
		}
	}
	
	private String getScanName() {
		return m_irx.getName().substring(0, m_irx.getName().lastIndexOf('.')); //$NON-NLS-1$
	}
	
	private String getDefaultScanName() {
		return FileUtil.getValidFilename(m_rootProject.getName() + IMavenConstants.IRX_EXTENSION);
	}

	protected void setIrxFile() {
		if(m_output == null || m_output.trim().equals("") || m_output.equals("true")) { //$NON-NLS-1$ $NON-NLS-2$
			m_irx = new File(m_targetDir, getDefaultScanName());
			getProgress().setStatus(new Message(Message.INFO, Messages.getMessage("missing.output.arg",  m_irx))); //$NON-NLS-1$
		}
		else {
			File irxFile = new File(m_output);
			if(!irxFile.isAbsolute()) {
				//If the path is relative, make it absolute based on the current directory.
				irxFile = FileSystems.getDefault().getPath(irxFile.getPath()).normalize().toAbsolutePath().toFile();
			}
			
			if(irxFile.isDirectory()) {
				m_irx = new File(irxFile, getDefaultScanName());
				getProgress().setStatus(new Message(Message.INFO, Messages.getMessage("scan.file.is.directory",  irxFile, m_irx))); //$NON-NLS-1$
			}	
			else if(irxFile.getParentFile() == null || !irxFile.getParentFile().exists()) {
				m_irx = new File(m_targetDir, getDefaultScanName());
				getProgress().setStatus(new Message(Message.INFO, Messages.getMessage("scan.file.invalid", m_output, m_irx))); //$NON-NLS-1$
			}
			else {
				//ensure .irx extension
				String name = irxFile.getName();
				name = name.endsWith(SASTConstants.IRX_EXTENSION) ? name : name + SASTConstants.IRX_EXTENSION;
				m_irx = new File(irxFile.getParentFile(), name);
			}
		}
		
		if(m_irx.isFile())
			m_irx.delete();
	}
	
	private boolean shouldSkipProject(MavenProject project) {
		return project.getPackaging().equalsIgnoreCase(IMavenConstants.POM) ||
				(project.getPackaging().equalsIgnoreCase(IMavenConstants.EAR) && !project.isExecutionRoot());
	}
}
