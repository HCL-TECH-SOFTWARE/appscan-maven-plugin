/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017, 2020. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.targets;

import java.io.File;
import java.util.Map;

import org.apache.maven.project.MavenProject;

import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.maven.plugin.util.MavenUtil;
import com.hcl.appscan.sdk.scanners.sast.targets.JEETarget;

public class MavenJEETarget extends JEETarget implements IMavenConstants {

	private static String DEFAULT_JSP_COMPILER = "Default Tomcat JSP Compiler"; //$NON-NLS-1$
	
	private MavenProject m_project;
	private MavenJavaTarget m_target;
	
	public MavenJEETarget(MavenProject project) {
		m_project = project;
		m_target = new MavenJavaTarget(project);
	}

	@Override
	public String getJSPCompiler() {
		String jspCompiler = System.getProperty(JSP_COMPILER);
		if(jspCompiler != null && jspCompiler != "true" && !(jspCompiler.trim().isEmpty())) //$NON-NLS-1$
			return jspCompiler;
		else
			return DEFAULT_JSP_COMPILER;
	}
	
	@Override
	public File getTargetFile() {
		String packaging = m_project.getPackaging();
		String finalName = null;
		String extension = "." + packaging; //$NON-NLS-1$
		
		if(packaging.equalsIgnoreCase(WAR)) {
			finalName = MavenUtil.getPluginConfigurationProperty(m_project, WAR_KEY, WAR_NAME);
			extension = WAR_EXTENSION;
		}
		else if(packaging.equalsIgnoreCase(EAR)) {
			finalName = MavenUtil.getPluginConfigurationProperty(m_project, EAR_KEY, FINAL_NAME);
			extension = EAR_EXTENSION;
		}
		
		if(finalName == null)
			finalName = m_project.getBuild().getFinalName();
		return new File(m_project.getBuild().getDirectory(), finalName + extension);
	}

	@Override
	public String getClasspath() {
		return m_target.getClasspath();
	}

	@Override
	public String getJava() {
		return m_target.getJava();
	}
	
	@Override
	public Map<String, String> getProperties() {
		Map<String, String> buildInfos = super.getProperties();
		buildInfos.putAll(m_target.getProperties());
		return buildInfos;
	}
}
