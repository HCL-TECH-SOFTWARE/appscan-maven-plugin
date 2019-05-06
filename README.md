# HCL AppScan on Cloud Maven Plugin

Apply the power of static application security testing with HCL AppScan on Cloud – a SaaS solution that helps to eliminate vulnerabilities from applications before they are deployed. HCL AppScan on Cloud integrates directly into the SDLC, providing static, dynamic, mobile and open source testing.

You can use the HCL AppScan on Cloud Maven plugin to generate an IRX file for submission to the cloud analysis service. The results are ready quickly (90% are ready in less than one hour) having been honed by Intelligent Finding Analytics, which uses HCL's Artificial Intelligence capabilities to greatly reduce false positives and other noise by an average of more than 98%. IFA also displays optimal locations for developers to fix multiple vulnerabilities in the code. Click [here](https://securityintelligence.com/intelligent-finding-analytics-cognitive-computing-application-security-expert/) for more information.

Not yet a customer of HCL AppScan on Cloud? Click [here](http://ibm.biz/ASoC-FromIDE) for a free trial of Application Security on Cloud to use with this plugin

# Prerequisites:

- An account on the [HCL AppScan on Cloud](https://www.ibm.com/marketplace/cloud/application-security-on-cloud/) service. You'll need to [create an application](http://www.ibm.com/support/knowledgecenter/SSYJJF_1.0.0/ApplicationSecurityonCloud/ent_create_application.html) on the service to associate your scans with.

# Goals:

- <b>prepare</b>:  Generates an IRX file for all jar, war, and ear projects in the build. The IRX file will be generated in the root project's "target" directory by default.
- <b>listTargets</b>:  Lists the targets that will be included in the generated .irx file.

# Usage:

To execute the "prepare" goal, run the following command:

	mvn com.hcl.security:appscan-maven-plugin:prepare
  
Note: The above command can be simplified by adding com.hcl.security to the list of plugin groups in your Maven settings.xml. To do so, add the following to ~/.m2/settings.xml or ${maven.home}/conf/settings.xml:

	<pluginGroups>
  	  <pluginGroup>com.hcl.security</pluginGroup>
	</pluginGroups>

After doing so, you can execute the prepare goal using the "appscan" prefix. For example:

	mvn appscan:prepare

# Configurable Options:

	OPTION:			DEFAULT VALUE				DESCRIPTION
    output     <root project>/target/<root project name>.irx      The name and/or location of the generated .irx file.

# License

All files found in this project are licensed under the [Apache License 2.0](LICENSE).

