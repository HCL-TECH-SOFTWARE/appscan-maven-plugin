/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.util;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class MavenUtil {
	
	public static String getPluginConfigurationProperty(MavenProject project, String pluginKey, String property)
	{
		Plugin plugin = project.getBuild().getPluginsAsMap().get(pluginKey);
		
		if(plugin != null) {
			Xpp3Dom dom = (Xpp3Dom)plugin.getConfiguration();
			if(dom != null && dom.getChild(property) != null)
				return dom.getChild(property).getValue();
		}
		
		return null;
	}
}
