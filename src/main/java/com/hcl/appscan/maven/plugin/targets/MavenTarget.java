/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.targets;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.apache.maven.project.MavenProject;

import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.sdk.scanners.sast.targets.ISASTTarget;
import com.hcl.appscan.sdk.scanners.sast.xml.IModelXMLConstants;

public class MavenTarget implements ISASTTarget, IMavenConstants {

	private MavenProject m_project;
	private ISASTTarget m_target;

	
	public MavenTarget(MavenProject project) {
		m_project = project;
		m_target = TargetFactory.create(project);
	}
	
	public MavenProject getProject() {
		return m_project;
	}
	
	@Override
	public String getTarget() {
		return m_target.getTarget();
	}
	
	@Override
	public Set<String>getExclusionPatterns() {
		return m_target.getExclusionPatterns();
	}
	
	@Override
	public Set<String>getInclusionPatterns() {
		return m_target.getInclusionPatterns();
	}
	
	@Override
	public boolean outputsOnly() {
		return m_target.outputsOnly();
	}
	
	@Override
	public File getTargetFile() {
		return m_target.getTargetFile();
	}
	
	@Override
	public Map<String, String> getProperties() {
		Map<String, String> buildInfos = m_target.getProperties();
		buildInfos.put(IModelXMLConstants.A_SRC_ROOT, getSourceDirs());
		return buildInfos;
	}
	
	protected String getSourceDirs() {
		String sourceDirs = ""; //$NON-NLS-1$
		for(String root : m_project.getCompileSourceRoots())
			sourceDirs += root + File.pathSeparator;
		
		if(sourceDirs.endsWith(File.pathSeparator))
			return sourceDirs.substring(0, sourceDirs.length() - 1);
		
		return sourceDirs;
	}
}
