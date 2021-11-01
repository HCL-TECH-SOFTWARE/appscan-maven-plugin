/**
 * © Copyright HCL Technologies Ltd. 2017-2021. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.mojos;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.maven.plugin.Messages;
import com.hcl.appscan.maven.plugin.targets.MavenTarget;
import com.hcl.appscan.sdk.CoreConstants;
import com.hcl.appscan.sdk.logging.Message;
import com.hcl.appscan.sdk.scanners.sast.SASTConstants;
import com.hcl.appscan.sdk.scanners.sast.SASTScanManager;
import com.hcl.appscan.sdk.scanners.sast.targets.GenericTarget;
import com.hcl.appscan.sdk.utils.FileUtil;
import com.hcl.appscan.sdk.utils.SystemUtil;

public abstract class SASTMojo extends AppScanMojo {

	/**
	 * The .irx file.
	 */
	@Parameter (property="output", defaultValue="", required=false, readonly=false) //$NON-NLS-1$ //$NON-NLS-2$
	private String m_output;
	
	private File m_irx;
	private SASTScanManager m_scanManager;
	
	@Parameter (alias="sourceCodeOnly", defaultValue="false", required=false, readonly=false) //$NON-NLS-1$ //$NON-NLS-2$
	private Boolean m_isSourceCodeOnly;
	
	@Override
	protected void initialize() {
		super.initialize();
		m_scanManager = new SASTScanManager(m_targetDir);
	}
	
	@Override
   	public void execute() throws MojoExecutionException, MojoFailureException {    		

		if(isLastProject(m_project)) {
			initialize();
			setIrxFile();
    		
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
	
	protected Map<String, String> getScanProperties() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(CoreConstants.SCAN_NAME, getScanName());
		properties.put(SASTConstants.SAVE_LOCATION, m_irx.getParent());
		properties.put("APPSCAN_IRGEN_CLIENT", "Maven"); //$NON-NLS-1$ //$NON-NLS-2$
		properties.put("APPSCAN_CLIENT_VERSION", m_runtimeInformation.getMavenVersion()); //$NON-NLS-1$
		properties.put("IRGEN_CLIENT_PLUGIN_VERSION", getPluginVersion()); //$NON-NLS-1$
		properties.put("ClientType", "maven-" + SystemUtil.getOS() + "-" + getPluginVersion()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return properties;
	}
	
	private String getPluginVersion () {
		String pluginVersion = "";
		PluginDescriptor descriptor = (PluginDescriptor)getPluginContext().get("pluginDescriptor"); //$NON-NLS-1$
		
		if(descriptor != null) {
			pluginVersion = descriptor.getVersion();
		};
		
		return pluginVersion;
	}
	
	protected abstract void run() throws MojoExecutionException;

	private void addScanTarget(MavenProject project) {
		if(project.getPackaging().equalsIgnoreCase(IMavenConstants.POM)){
			return;
		}else if(m_isSourceCodeOnly){
			m_scanManager.setIsSourceCodeOnlyEnabled(true);
			for(String sourceRoot : project.getBuild().getSourceDirectory().split(File.pathSeparator)){
				if(!sourceRoot.toString().contains("test"))
					m_scanManager.addScanTarget(new GenericTarget(sourceRoot));
			}
			if (m_project.getPackaging().equalsIgnoreCase("war")) {
	            if(SystemUtil.getOS()=="win")
	            	m_scanManager.addScanTarget(new GenericTarget(m_project.getBasedir()+"\\src\\main\\webapp"));
	            else
	            	m_scanManager.addScanTarget(new GenericTarget(m_project.getBasedir()+"/src/main/webapp"));
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

	private void setIrxFile() {
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
}
