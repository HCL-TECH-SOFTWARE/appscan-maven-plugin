/**
 * � Copyright HCL Technologies Ltd. 2020. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.targets;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.maven.plugin.util.MavenUtil;
import com.hcl.appscan.sdk.scanners.sast.targets.DefaultTarget;

public class MavenAndroidTarget extends DefaultTarget implements IMavenConstants {

	private MavenProject m_project;
	private File m_androidManifestFile = null;
	
	public MavenAndroidTarget(MavenProject project) {
		m_project = project;
	}
	
	public MavenProject getProject() {
		return m_project;
	}
	
	public File getTargetFile() {
		File androidManifest = null;
		try {
			androidManifest = new File(MavenUtil.getPluginConfigurationProperty(m_project, ANDROID_KEY, ANDROID_MANIFEST_FILE));
		}
		catch (NullPointerException npe) {
			// will ignore and continue so we can search for the AndroidManifest.xml file
		}
		
		if(androidManifest != null && androidManifest.exists()) {
			return androidManifest;
		}
		else {
			androidManifest = new File(m_project.getBasedir(), ANDROID_MANIFEST);
			if (androidManifest.exists()) {
				return androidManifest;
			}
			else {
				File[] files = new File((m_project.getBasedir()).toString()).listFiles();
				findAndroidManifestFile(files);
				return m_androidManifestFile != null ? m_androidManifestFile : null; // null won't be reached because maven will throw error about missing AndroidManifest.xml file
			}
		}
	}

	@Override
	public Map<String, String> getProperties() {
		return new HashMap<String, String>();
	}
	
	private void findAndroidManifestFile(File[] files) {
	    for (File file : files) {
	    	if (file.isFile() && file.toString().endsWith(ANDROID_MANIFEST)) {
	    		m_androidManifestFile = file;
	    		break;
	    	}
	    	else if (file.isDirectory()) {
	            findAndroidManifestFile(file.listFiles());
	        }
	    }
	}
}
