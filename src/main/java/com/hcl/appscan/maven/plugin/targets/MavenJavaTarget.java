/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.targets;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.maven.plugin.util.MavenUtil;
import com.hcl.appscan.sdk.scanners.sast.targets.JavaTarget;

public class MavenJavaTarget extends JavaTarget implements IMavenConstants{
	
	private MavenProject m_project;
	
	public MavenJavaTarget(MavenProject project) {
		m_project = project;
	}
	
	@Override
	public String getClasspath() {
		String classpath = ""; //$NON-NLS-1$	

		for(Artifact dependency : m_project.getArtifacts()) {
			if(dependency.getFile() != null && dependency.getScope() != Artifact.SCOPE_TEST)
				classpath += dependency.getFile().getAbsolutePath() + File.pathSeparator;
		}
		return classpath;
	}
	
	@Override
	public String getJava() {
		String jdk = System.getenv(VAR_JAVA_HOME);
		return jdk == null ? System.getProperty(PROP_JAVA_HOME) : jdk;
	}
	
	@Override
	public File getTargetFile() {
		String packaging = m_project.getPackaging();
		if(!packaging.equalsIgnoreCase(JAR) && !packaging.equalsIgnoreCase(HPI))
			return new File(m_project.getBuild().getOutputDirectory());
		
		String finalName = MavenUtil.getPluginConfigurationProperty(m_project, JAR_KEY, FINAL_NAME);
		if(finalName == null)
			finalName = m_project.getBuild().getFinalName();
		return new File(m_project.getBuild().getDirectory(), finalName + JAR_EXTENSION);
	}
}
