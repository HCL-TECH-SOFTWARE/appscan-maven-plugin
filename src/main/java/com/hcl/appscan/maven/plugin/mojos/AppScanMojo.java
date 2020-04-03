/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.mojos;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.RuntimeInformation;

import com.hcl.appscan.maven.plugin.managers.MavenProgress;
import com.hcl.appscan.sdk.logging.IProgress;

public abstract class AppScanMojo extends AbstractMojo
{
	/**
	* This project.
	*/ 
	@Parameter (defaultValue="${project}", required=true, readonly=true) //$NON-NLS-1$
	protected MavenProject m_project;
	
	/**
	* All projects involved in the build.
	*/ 
	@Parameter (defaultValue="${reactorProjects}", required=false, readonly=true) //$NON-NLS-1$
	protected List<MavenProject> m_projects;
	
	/**
	 * The root project of the build.
	 */
	@Parameter (defaultValue="${session.topLevelProject}", required=false, readonly=true)
	protected MavenProject m_rootProject;
	
	/**
	 * The default output directory.
	 */
	@Parameter (defaultValue="${session.executionRootDirectory}/target", required=false, readonly=true) //$NON-NLS-1$
	protected String m_targetDir;
	
	/**
	* Maven runtime information.
	*/
	@Component
	protected RuntimeInformation m_runtimeInformation;
	
	private IProgress m_progress;
	
	protected void initialize() {
		m_progress = new MavenProgress(getLog());
	}
	
	protected IProgress getProgress() {
		return m_progress;
	}
	
	protected boolean isLastProject(MavenProject project) {
		MavenProject lastProject = m_projects.get(m_projects.size() - 1);
		if(project == lastProject)
			return true;
		return false;
	}  
}
