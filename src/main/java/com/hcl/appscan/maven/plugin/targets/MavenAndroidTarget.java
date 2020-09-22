/**
 * © Copyright HCL Technologies Ltd. 2020. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.targets;

import java.io.File;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.sdk.scanners.sast.targets.DefaultTarget;

public class MavenAndroidTarget extends DefaultTarget implements IMavenConstants {

	private MavenProject m_project;
	
	public MavenAndroidTarget(MavenProject project) {
		m_project = project;
	}
	
	public MavenProject getProject() {
		return m_project;
	}
	
	public File getTargetFile() {
		File androidManifest = new File(m_project.getBasedir(), ANDROID_MANIFEST);
		if (androidManifest.exists())
			return androidManifest;
		else {
			File[] files = new File((m_project.getBasedir()).toString()).listFiles();
			return findAndroidManifestFile(files);
		}
	}

	@Override
	public Map<String, String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public File findAndroidManifestFile(File[] files) {
	    for (File file : files) {
	    	if (file.isFile() && file.toString().toLowerCase().endsWith(ANDROID_MANIFEST)) {
	    		return file;
	    	}
	    	else {
	            return findAndroidManifestFile(file.listFiles());
	        }
	    }
		return null;	// AndroidManifest.xml file was not found
	}
}
