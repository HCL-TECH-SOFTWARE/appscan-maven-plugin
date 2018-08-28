/*
 * (c) Copyright HCL Technologies Ltd. 2017. 
*/

package com.hcl.appscan.maven.plugin.targets;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.sdk.scanners.sast.targets.ISASTTarget;

public class TargetFactoryTest {
	
	@Test
	public void testJarPackaging() {
		MavenProject project = new MavenProject();
		project.setPackaging(IMavenConstants.JAR);
		ISASTTarget target = TargetFactory.create(project);
		assertTrue(target instanceof MavenJavaTarget);
	}
	
	@Test
	public void testEarPackaging() {
		MavenProject project = new MavenProject();
		project.setPackaging(IMavenConstants.EAR);
		ISASTTarget target = TargetFactory.create(project);
		assertTrue(target instanceof MavenJEETarget);
	}
	
	@Test
	public void testUnknownPackaging() {
		MavenProject project = new MavenProject();
		project.setPackaging("abc"); //$NON-NLS-1$
		ISASTTarget target = TargetFactory.create(project);
		assertTrue(target instanceof MavenJavaTarget);
	}
	
	@Test
	public void testPomPackaging() {
		MavenProject project = new MavenProject();
		project.setPackaging(IMavenConstants.POM);
		ISASTTarget target = TargetFactory.create(project);
		assertNull(target);
	}
}
