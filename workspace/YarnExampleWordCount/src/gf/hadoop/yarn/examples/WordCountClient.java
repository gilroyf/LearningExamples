package gf.hadoop.yarn.examples;


//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ClassUtil;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.ApplicationConstants.Environment;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
//import org.apache.hadoop.yarn.applications.distributedshell.ApplicationMaster;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
//import org.apache.hadoop.yarn.util.Apps;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;

import java.util.*;
//import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/// A. have client connect to resource manager to get application ID
/// B. client creates applicationsubmissioncontext sets appId, name, priority, queue
/// C. applicationsubmissioncontext defines containerlaunchcontext with which application master is laucnhed
/// D. 
public class WordCountClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//
		//create a client
		//
		YarnConfiguration conf= new YarnConfiguration();
		YarnClient yarnClient = YarnClient.createYarnClient();
		yarnClient.init(conf);
		yarnClient.start();
		
		
		// 
		// --2 and A--
		// create application - which will communicate with resource
		// manager to get application ID
		//
		YarnClientApplication yarnApp=null;
		GetNewApplicationResponse appResponse=null;
		try {
			//YarnClientApplication 
			yarnApp = yarnClient.createApplication();
			appResponse  = yarnApp.getNewApplicationResponse();
		}	catch (YarnException e) {
		    System.err.println("Caught YarnException: " + e.getMessage());
		}   catch (IOException e) {
		    System.err.println("Caught IOException: " + e.getMessage());

		} 
		
		//
		// --3and C--
		// create launch context where we specify command to 
		// launch application  master
		//

	    ContainerLaunchContext container =
	        Records.newRecord(ContainerLaunchContext.class);

	    String amLaunchCmd =
	        String.format(
	            "$JAVA_HOME/bin/java -Xmx256M %s -debug -container_memory 10 -container_vcores 2 -num_containers 1  1>%s/stdout 2>%s/stderr",
	            ApplicationMaster.class.getName(),
	            ApplicationConstants.LOG_DIR_EXPANSION_VAR,
	            ApplicationConstants.LOG_DIR_EXPANSION_VAR);

	    List<String> list = new ArrayList<String>();
	    list.add(amLaunchCmd);
	    // set command
	    container.setCommands(list);

		//////////////////////////////////////////////////
		//setup jar for AM to create
		//////////////////////////////////////////////////
		String jar = ClassUtil.findContainingJar(WordCountClient.class);
		FileSystem fs = null;
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
		Path src = new Path(jar);
		Path dest = new Path(fs.getHomeDirectory(), src.getName());
		System.out.format("Copying JAR from %s to %s%n", src, dest);
		// set localresources
		try {
			fs.copyFromLocalFile(src, dest);
		
			FileStatus jarStat = FileSystem.get(conf).getFileStatus(dest);
		
			LocalResource appMasterJar = Records.newRecord(LocalResource.class);
			appMasterJar.setResource(ConverterUtils.getYarnUrlFromPath(dest));
			appMasterJar.setSize(jarStat.getLen());
			appMasterJar.setTimestamp(jarStat.getModificationTime());
			appMasterJar.setType(LocalResourceType.FILE);
			appMasterJar.setVisibility(LocalResourceVisibility.APPLICATION);
			Map<String, LocalResource> m = new HashMap<String, LocalResource>();
			m.put("AppMaster.jar", appMasterJar);
			
			
			container.setLocalResources(m);
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
//		ImmutableMap.of("AppMaster.jar", appMasterJar));
		
		
		//////////////////////////////////////////////////
		//setup classpath for AM
		//////////////////////////////////////////////////
		// set env
		Map<String, String> appMasterEnv = new HashMap<String, String>();
		StringBuilder classPathEnv = new StringBuilder(Environment.CLASSPATH.$$()).append(ApplicationConstants.CLASS_PATH_SEPARATOR).append("./*");
		for (String c : conf.getStrings(
				YarnConfiguration.YARN_APPLICATION_CLASSPATH,
				YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH)) {
			//Apps.addToEnvironment(appMasterEnv, Environment.CLASSPATH.name(), c.trim());
			classPathEnv.append(ApplicationConstants.CLASS_PATH_SEPARATOR);
			classPathEnv.append(c.trim());
		}
//		Apps.addToEnvironment(appMasterEnv,	Environment.CLASSPATH.name(),Environment.PWD.$() + File.separator + "*");
		appMasterEnv.put("CLASSPATH", classPathEnv.toString());
		container.setEnvironment(appMasterEnv);
		
		//////////////////////////////////////////////////
		// specify resource requirements for AM
		//////////////////////////////////////////////////
		Resource capability = Records.newRecord(Resource.class);
		capability.setMemory(256);
		capability.setVirtualCores(1);
		
		//Finally, set-up ApplicationSubmissionContext for the application
		// ---B---
		ApplicationSubmissionContext appContext =
		yarnApp.getApplicationSubmissionContext();
		appContext.setApplicationName("basic-dshell"); // application name
		appContext.setAMContainerSpec(container);
		appContext.setResource(capability);
		appContext.setQueue("default"); // queue
		
		//////////////////////////////////////////////////
		//submit the application
		//////////////////////////////////////////////////
		ApplicationId appId = appContext.getApplicationId();
		System.out.println("Submitting application " + appId);
		try {
		yarnClient.submitApplication(appContext);
		
		ApplicationReport report = yarnClient.getApplicationReport(appId);
		YarnApplicationState state = report.getYarnApplicationState();
		
		EnumSet<YarnApplicationState> terminalStates =
		EnumSet.of(YarnApplicationState.FINISHED,
		YarnApplicationState.KILLED,
		YarnApplicationState.FAILED);
		
		//////////////////////////////////////////////////
		//wait for the application to complete
		//////////////////////////////////////////////////
		while (!terminalStates.contains(state)) {
			TimeUnit.SECONDS.sleep(1);
			report = yarnClient.getApplicationReport(appId);
			state = report.getYarnApplicationState();
		}
		
		System.out.printf("Application %s finished with state %s%n",
		appId, state);
		} catch (InterruptedException e) {
		}catch (YarnException e) {
		
		    System.err.println("Caught YarnException: " + e.getMessage());
		}   catch (IOException e) {
		    System.err.println("Caught IOException: " + e.getMessage());

		}
			}

}
