/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.mojos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.hcl.appscan.maven.plugin.Messages;
import com.hcl.appscan.maven.plugin.targets.TargetFactory;
import com.hcl.appscan.sdk.scanners.sast.targets.ISASTTarget;

@Mojo (name="listTargets", //$NON-NLS-1$
		requiresProject=true,
		threadSafe=false)
public final class ListTargetsMojo extends AppScanMojo {
	
	private static List<String> m_targets = null;
	
	public ListTargetsMojo() {
		if(m_targets == null)
			m_targets = new ArrayList<String>();
	}
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		ISASTTarget target = TargetFactory.create(m_project);
		if(target != null) {
			File output = target.getTargetFile();
			if(output != null)
				m_targets.add(output.getAbsolutePath());
		}
		if(isLastProject(m_project))
			printTargets();
	}
    
	private void printTargets() {
		getLog().info(Messages.getMessage("targets.found")); //$NON-NLS-1$
		for(String target : m_targets)
			getLog().info(target);
	}
}
