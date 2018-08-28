package com.hcl.appscan.maven.plugin.targets;

/*
 * (c) Copyright HCL Technologies Ltd. 2017. 
*/

import static org.junit.Assert.*;

import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.sdk.scanners.sast.xml.IModelXMLConstants;

public class MavenTargetTest {

	@Test
	public void testJavaTarget() {
		MavenProject project = new MavenProject();
		project.setPackaging(IMavenConstants.JAR);
		MavenTarget target = new MavenTarget(project);
		Map<String, String> buildInfos = target.getProperties();
		assertTrue(buildInfos.containsKey(IModelXMLConstants.A_ADDITIONAL_CLASSPATH));
		assertTrue(buildInfos.containsKey(IModelXMLConstants.A_JDK_PATH));
		assertTrue(buildInfos.containsKey(IModelXMLConstants.A_SRC_ROOT));
	}
	
	@Test
	public void testJEETarget() {
		MavenProject project = new MavenProject();
		project.setPackaging(IMavenConstants.WAR);
		MavenTarget target = new MavenTarget(project);
		Map<String, String> buildInfos = target.getProperties();
		assertTrue(buildInfos.containsKey(IModelXMLConstants.A_ADDITIONAL_CLASSPATH));
		assertTrue(buildInfos.containsKey(IModelXMLConstants.A_JDK_PATH));
		assertTrue(buildInfos.containsKey(IModelXMLConstants.A_JSP_COMPILER));
		assertTrue(buildInfos.containsKey(IModelXMLConstants.A_SRC_ROOT));
	}
}
