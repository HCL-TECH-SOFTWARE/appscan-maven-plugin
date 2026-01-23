# HCL AppScan Maven Plugin

   Leverage the power of static application security testing (SAST) with HCL AppScan on Cloud, a SaaS service for eliminating app vulnerabilities before deployment, and
HCL AppScan 360°, a cloud-native, self-managed platform for vulnerability elimination. Both solutions integrate directly into the software development lifecycle (SDLC), providing static, dynamic, and open-source testing.

You can submit static and open-source scans directly from the HCL AppScan Maven plugin or generate an IRX file for later submission to the service. Results are ready
quickly—90% are ready in less than one hour—having been honed by Intelligent Finding Analytics (IFA). IFA uses HCL&#39;s AI capabilities to reduce false positives and
other noise by an average of more than 98%. IFA also displays optimal locations for developers to fix multiple vulnerabilities in the code. [Learn more about Intelligent
Finding Analytics.](https://securityintelligence.com/intelligent-finding-analytics-cognitive-computing-application-security-expert).

Not yet an HCL AppScan on Cloud or AppScan 360 customer? [Get a free trial of Application Security on Cloud](https://cloud.appscan.com/) to use with Maven,or [get a free trial of AppScan 360°](https://www.hcl-software.com/appscan/products/appscan360/contact). 

# Prerequisites:

- An account on the [HCL AppScan on Cloud Service](https://cloud.appscan.com/). You'll need to [create an application](https://help.hcltechsw.com/appscan/ASoC/ent_create_application.html) on the service to associate your scans with.
- To execute scans in HCL AppScan 360°, you must have access to an instance of AppScan 360°.[Learn more about AppScan 360° features and installation](https://help.hcl-software.com/appscan/360/2.0.0/home.html).

# Goals:

- <b>prepare</b>:  Generates an IRX file for all jar, war, and ear projects in the build. The IRX file will be generated in the root project's "target" directory by default.
- <b>analyze</b>:  Generates an IRX file for all jar, war, and ear projects in the build and submits it to the HCL AppScan on Cloud service or AppScan 360° for analysis. This task requires an API key, secret, and application ID (additional parameters serviceUrl and acceptssl would be needed for AppScan 360°). The IRX file will be generated in the root project's "target" directory by default.
- <b>listTargets</b>:  Lists the targets that will be included in the generated .irx file.

# Usage:

To execute the "prepare" goal, run the following command:

	mvn com.hcl.security:appscan-maven-plugin:prepare
	
To execute the "analyze" goal, run the following command:

	mvn com.hcl.security:appscan-maven-plugin:analyze
This goal requires the appId, appscanKey, and appscanSecret parameters.Additional parameters serviceUrl and acceptssl would be needed for AppScan 360°.
  
**Note**: You can simplify the preceding commands by adding com.hcl.security to the list of plugin groups in your Maven settings.xml. To do so, add the following to ~/.m2/settings.xml or ${maven.home}/conf/settings.xml:
XML
<pluginGroups>
 <pluginGroup>com.hcl.security</pluginGroup>
</pluginGroups>

After doing so, you can execute the prepare goal using the "appscan" prefix. For example:

	mvn appscan:prepare

# Configurable Options:

	OPTION:			DEFAULT VALUE					DESCRIPTION
    output		<root project>/target/<root project name>.irx	The name and/or location of the generated .irx file. If the selected path does not exist, the default path is applied.
    appId		null - Required for 'analyze' goal		The id of the application in the cloud service.
    appscanKey	null - Required for 'analyze' goal        	The user's API key id for authentication.
    appscanSecret	null - Required for 'analyze' goal        	The user's API key secret for authentication.
    namespaces		null					Override automatic namespace detection. Set to "" to disable namespace detection.
    sourceCodeOnly		false					If set to true, only scan source code.
    openSourceOnly		false					Only run software composition analysis (SCA). Do not run static analysis.
    staticAnalysisOnly	false					Only run static analysis. Do not run software composition analysis (SCA).
    jspCompiler     Default Tomcat JSP Compiler                     The JSP compiler path.
    thirdParty		false					Include known third party packages in static analysis (not recommended).
    serviceUrl		null					REQUIRED for AppScan 360. The AppScan 360 service url. Not applicable to AppScan on Cloud.
    acceptssl		false					Ignore untrusted certificates when connecting to AppScan 360. Only intended for testing purposes. Not applicable to AppScan on Cloud.

# License

All files found in this project are licensed under the [Apache License 2.0](LICENSE).

