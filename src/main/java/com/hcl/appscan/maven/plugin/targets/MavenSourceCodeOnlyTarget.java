/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017, 2021. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.targets;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import com.hcl.appscan.sdk.scan.ITarget;
import com.hcl.appscan.sdk.scanners.sast.targets.DefaultTarget;
import com.hcl.appscan.sdk.scanners.sast.targets.GenericTarget;
import com.hcl.appscan.sdk.utils.SystemUtil;
import com.hcl.appscan.maven.plugin.IMavenConstants;

public class MavenSourceCodeOnlyTarget extends DefaultTarget implements IMavenConstants {
	
	private MavenProject m_project;
	private ArrayList<ITarget> m_targets;

	public MavenSourceCodeOnlyTarget(MavenProject project) {
		m_project = project;
		m_targets = new ArrayList<ITarget>();
	}
	
	public ArrayList<ITarget> getTargets() {
		File dir = new File(m_project.getBuild().getSourceDirectory());
		m_targets.add(new GenericTarget(dir.toString()));
        if (m_project.getPackaging().equalsIgnoreCase(WAR)) {
            if(SystemUtil.getOS()=="win")
            	m_targets.add(new GenericTarget(m_project.getBasedir()+"\\src\\main\\webapp"));
            else
            	m_targets.add(new GenericTarget(m_project.getBasedir()+"/src/main/webapp"));
        }
		return m_targets;
	}

	@Override
	public File getTargetFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
