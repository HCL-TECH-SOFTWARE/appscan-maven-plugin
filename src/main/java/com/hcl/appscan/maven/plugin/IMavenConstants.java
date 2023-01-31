/**
 * © Copyright IBM Corporation 2016.
 * © Copyright HCL Technologies Ltd. 2017, 2020. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin;

public interface IMavenConstants {

	String APK 								= "apk";											//$NON-NLS-1$
	String JAR 								= "jar";											//$NON-NLS-1$
	String WAR 								= "war";											//$NON-NLS-1$
	String EAR  							= "ear";											//$NON-NLS-1$
	String POM  							= "pom";											//$NON-NLS-1$
	String HPI  							= "hpi";											//$NON-NLS-1$
	
	String JAR_EXTENSION  					= ".jar";											//$NON-NLS-1$
	String WAR_EXTENSION 					= ".war";											//$NON-NLS-1$
	String EAR_EXTENSION 					= ".ear";											//$NON-NLS-1$
	String IRX_EXTENSION 					= ".irx";											//$NON-NLS-1$
	
	String ANDROID_MANIFEST					= "AndroidManifest.xml";							//$NON-NLS-1$
	
	String ANDROID_KEY						= "com.simpligility.maven.plugins:android-maven-plugin";	//$NON-NLS-1$
	String JAR_KEY  						= "org.apache.maven.plugins:maven-jar-plugin";		//$NON-NLS-1$
	String WAR_KEY  						= "org.apache.maven.plugins:maven-war-plugin";		//$NON-NLS-1$
	String EAR_KEY  						= "org.apache.maven.plugins:maven-ear-plugin";		//$NON-NLS-1$
	
	String ANDROID_MANIFEST_FILE			= "androidManifestFile";							//$NON-NLS-1$
	String FINAL_NAME  						= "finalName";										//$NON-NLS-1$
	String WAR_NAME 	 					= "warName";										//$NON-NLS-1$
	String APPSCAN_SERVER					= "appscan";										//$NON-NLS-1$
	
	String VAR_JAVA_HOME					= "JAVA_HOME";										//$NON-NLS-1$
	String PROP_JAVA_HOME					= "java.home";										//$NON-NLS-1$
	String JSP_COMPILER						= "jspCompiler";									//$NON-NLS-1$
	String NAMESPACES						= "namespaces"; 									//$NON-NLS-1$
}
