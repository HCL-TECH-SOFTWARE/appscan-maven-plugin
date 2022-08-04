/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017, 2020. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.targets;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
	
	@Override
	public Map<String, String> getProperties() {
		String irx_cache_path = "";
		Map<String, String> buildInfos = super.getProperties();
		
		if (System.getProperty(IRX_MINOR_CACHE_HOME.toUpperCase()) != null)
			irx_cache_path = System.getProperty(IRX_MINOR_CACHE_HOME.toUpperCase());
		else if (System.getProperty(IRX_MINOR_CACHE_HOME) != null)
			irx_cache_path = System.getProperty(IRX_MINOR_CACHE_HOME);
		
		if (irx_cache_path != "") {
			File cache_dir = new File(irx_cache_path);
			cache_dir.mkdir();
			buildInfos.put(IRX_MINOR_CACHE_HOME, irx_cache_path);
		}
		
		buildInfos.put("package_includes", getNamespaces());
		return buildInfos;
	}
	
	private String getNamespaces() {
		//Allow user to override automatic namespace detection.
		String namespaceOverride = System.getProperty(NAMESPACES);
		if(namespaceOverride != null && !namespaceOverride.equalsIgnoreCase("true"))
			return namespaceOverride.replaceAll("[^A-Za-z0-9;.]", "");
	
		//From each source root, get the first 2 directories and use as a whitelist for packages that we'll scan.
		String separator = ";";
		String ret = "";
		Set<String> namespaces = new HashSet<String>();
		
		for(String root : m_project.getCompileSourceRoots()) {
			File sourceRoot = new File(root);
			if(sourceRoot.isDirectory()) {
				for(File child : sourceRoot.listFiles())
					namespaces.addAll(getNamespaces(child));
			}
		}
		
		for(String namespace : namespaces)
			ret += namespace + separator;
		
		return ret.endsWith(separator) ? ret.substring(0, ret.length() - 1) : ret;
	}
	
	private Set<String> getNamespaces(File parent) {
		Set<String> namespaces = new HashSet<String>();
		
		if(parent.isDirectory()) {
			for(File child : parent.listFiles()) {
				if(child.isDirectory())
					namespaces.add(parent.getName() + "." + child.getName());
			}
		}
		
		return namespaces;
	}
}
