/**
 * � Copyright HCL Technologies Ltd. 2020. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.targets;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.project.MavenProject;
import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.maven.plugin.util.MavenUtil;
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
		File androidManifest = null;
		
		if (MavenUtil.getPluginConfigurationProperty(m_project, ANDROID_KEY, ANDROID_MANIFEST_FILE) != null)
			androidManifest = new File(MavenUtil.getPluginConfigurationProperty(m_project, ANDROID_KEY, ANDROID_MANIFEST_FILE));
		
		if(androidManifest != null && androidManifest.isFile()) {
			return androidManifest;
		}
		else {
			Iterator<File> files = FileUtils.iterateFiles(m_project.getBasedir(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            while (files.hasNext()) {
                File curr = files.next();
                if (curr.getName().equalsIgnoreCase(ANDROID_MANIFEST))
                    return curr;
            }
		}
		return androidManifest;
	}

	@Override
	public Map<String, String> getProperties() {
		return new HashMap<String, String>();
	}
}
