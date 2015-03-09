/************************************************************************
 * Copyright (c) 2007-2014 - General Electric Company, All Rights Reserved
 *
 * Project: SADL Knowledge Server
 *
 * Description: The Semantic Application Design Language (SADL) is a
 * language for building semantic models and expressing rules that
 * capture additional domain knowledge. The SADL-IDE (integrated
 * development environment) is a set of Eclipse plug-ins that
 * support the editing and testing of semantic models using the
 * SADL language. 
 * 
 * The SADL Knowledge Server is a set of Java classes implementing 
 * a service interface for deploying ontology-based knowledge bases
 * for use in a client-server environment.
 *
 * This software is distributed "AS-IS" without ANY WARRANTIES
 * and licensed under the Eclipse Public License - v 1.0
 * which is available at http://www.eclipse.org/org/documents/epl-v10.php
 *
 ***********************************************************************/

/***********************************************************************
 * $Last revised by: crapo $ 
 * $Revision: 1.1 $ Last modified on   $Date: 2013/08/09 14:06:51 $
 ***********************************************************************/

package com.ge.research.sadl.server.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.activation.DataSource;
import javax.naming.NameNotFoundException;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ge.research.sadl.reasoner.ConfigurationException;
import com.ge.research.sadl.reasoner.InvalidNameException;
import com.ge.research.sadl.reasoner.QueryCancelledException;
import com.ge.research.sadl.reasoner.QueryParseException;
import com.ge.research.sadl.reasoner.ReasonerNotFoundException;
import com.ge.research.sadl.reasoner.ResultSet;
import com.ge.research.sadl.reasoner.TripleNotFoundException;
import com.ge.research.sadl.server.ISadlServerMD;
import com.ge.research.sadl.server.NamedServiceNotFoundException;
import com.ge.research.sadl.server.SessionNotFoundException;
import com.ge.research.sadl.server.server.SadlServerMDImpl;

public class TestSadlServerMD extends TestCase {

	private String modelFolder;
	private String modelName;
	private String instNS = "http://edu.uiuc/mobius/advise#";
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		modelFolder = ClassLoader.getSystemResource("DataModels/Advise2").getFile();
		List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
		loggers.add(LogManager.getRootLogger());
		for ( Logger logger : loggers ) {
		    logger.setLevel(Level.OFF);
		}
		modelName = "http://www.illinois.edu/advise/SmallExample";
	}

	@Test
	public void testSadlExtendedService() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NamedServiceNotFoundException, IOException {
		// there are two ways of specifying a kbase entry point
		// 1) by specifying the kbasefolder and the URI of the entry point
		ISadlServerMD srvr1 = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr1);
		
		// 2) by specifying the kbaseroot and a named service (as named in the ServicesConfig.owl file in the model folder)
		// Note: in this case modelFolder is used as kbaseroot but it could be a parent folder containing multiple model folders (kbases)
		File mf = new File(modelFolder);
		String kbroot = mf.getParent();
		ISadlServerMD srvr2 = new SadlServerMDImpl(kbroot);
		assertNotNull(srvr2);
		assertNotNull(srvr2.selectServiceModel("SmallExample"));
		assertTrue(modelName.equals(srvr2.getModelName()));
	}

	@Ignore
	@Test
	public void testGetModelQuery() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Test
	public void testGetAllSubclassesOfTaxonomy() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, IOException, QueryParseException, InvalidNameException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String[] subClasses = srvr.getAllSubclassesOfTaxonomy("http://www.illinois.edu/advise/System#Condition");
		assertNotNull(subClasses);
		for (int i = 0; i < subClasses.length; i++) {
			System.out.println("all subclass of Condition: " + subClasses[i]);
		}
	}

	@Test
	public void testGetLeafClassesOfTaxonomy() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, IOException, QueryParseException, InvalidNameException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String[] leafClasses = srvr.getLeafClassesOfTaxonomy("http://www.illinois.edu/advise/System#Condition");
		assertNotNull(leafClasses);
		for (int i = 0; i < leafClasses.length; i++) {
			System.out.println("leaf subclass of Condition: " + leafClasses[i]);
		}
	}

	@Test
	public void testGetInstancesOfClass() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, IOException, QueryParseException, InvalidNameException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String[] instances = srvr.getInstancesOfClass("Firewall");
		assertNotNull(instances);
		for (int i = 0; i < instances.length; i++) {
			System.out.println("instance of Firewall: " + instances[i]);
		}
	}

	@Test
	public void testIsObjectProperty() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, QueryParseException, InvalidNameException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		assertTrue(srvr.isObjectProperty("goalPriority"));
		assertFalse(srvr.isObjectProperty("planningHorizon"));
	}

	@Test
	public void testIsDatatypeProperty() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, QueryParseException, InvalidNameException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		assertFalse(srvr.isDatatypeProperty("goalPriority"));
		assertTrue(srvr.isDatatypeProperty("planningHorizon"));
	}

	@Test
	public void testGetPropertyDomain() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, QueryParseException, InvalidNameException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String[] domainClasses = srvr.getPropertyDomain("connectedTo");
		assertNotNull(domainClasses);
		for (int i = 0; i < domainClasses.length; i++) {
			System.out.println("domain of connectedTo: " + domainClasses[i]);
		}
	}

	@Test
	public void testGetPropertyRange() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, QueryParseException, InvalidNameException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String[] rangeClasses = srvr.getPropertyRange("connectedTo");
		assertNotNull(rangeClasses);
		for (int i = 0; i < rangeClasses.length; i++) {
			System.out.println("range of connectedTo: " + rangeClasses[i]);
		}
	}

	@Test
	public void testGetRequiredRangeClassesOfPropertyOfClass() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, QueryParseException, InvalidNameException, QueryCancelledException, IOException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String[] rangeClasses = srvr.getRequiredRangeClassesOfPropertyOfClass("Host", "subsystemof");
		assertNotNull(rangeClasses);
		assertTrue(rangeClasses.length == 1);
		for (int i = 0; i < rangeClasses.length; i++) {
			System.out.println("required range of subsystemof or Host: " + rangeClasses[i]);
		}
	}

	@Test
	public void testGetAllowedRangeClassesOfPropertyOfClass() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, IOException, QueryParseException, InvalidNameException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String[] rangeClasses = srvr.getAllowedRangeClassesOfPropertyOfClass("Host", "subsystemof");
		assertNotNull(rangeClasses);
//		assertTrue(rangeClasses.length == 1);
		for (int i = 0; i < rangeClasses.length; i++) {
			System.out.println("allowed range of subsystemof or Host: " + rangeClasses[i]);
		}
	}

	@Test
	public void testGetAllowedValuesOfObjectPropertyOfClass() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, IOException, QueryParseException, InvalidNameException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String[] allowedValues = srvr.getAllowedValuesOfObjectPropertyOfClass("ThreatAgent", "resourceLevel");
		assertNotNull(allowedValues);
//		assertTrue(rangeClasses.length == 1);
		for (int i = 0; i < allowedValues.length; i++) {
			System.out.println("allowed values of resourceLevel of ThreatAgent: " + allowedValues[i]);
		}
	}

	@Ignore
	@Test
	public void testGetAllowedValuesOfDataPropertyOfClass() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testLoadData() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testSendDataDataSource() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testSendDataDataSourceString() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddTripleStringStringObject() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testDeleteTripleStringStringObject() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testReset() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Test
	public void testSetInstanceDataNamespace() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, InvalidNameException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		assertNull(srvr.setInstanceDataNamespace(instNS));
		assertTrue(instNS.equals(srvr.getInstanceDataNamespace()));
	}

	@Test
	public void testCreateInstanceStringString() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, InvalidNameException, IOException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String className = "http://www.illinois.edu/advise/Adversary#ThreatAgent";
		srvr.setInstanceDataNamespace(instNS);
		String name = srvr.getUniqueInstanceUri(instNS, "ThreatAgent");
		String instUri = srvr.createInstance(name, className);
		assertTrue(instUri.equals(instNS + "ThreatAgent1"));
	}

	@Test
	public void testGetModelNamespace() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, IOException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String mn = srvr.getModelName();
		assertNotNull(mn);
	}

	@Ignore
	@Test
	public void testCreateServiceModel() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testPersistInstanceModelStringString() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testPersistInstanceModelStringStringString() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testPersistChangesToServiceModels() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddTripleStringStringStringObject() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testDeleteTripleStringStringStringObject() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Test
	public void testGetUniqueInstanceUri() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, InvalidNameException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		srvr.setInstanceDataNamespace(instNS);
		String name = srvr.getUniqueInstanceUri(instNS, "ThreatAgent");
		assertTrue(name.equals(instNS + "ThreatAgent1"));
	}

	@Ignore
	@Test
	public void testAddInstance() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, InvalidNameException, IOException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String className = "http://www.illinois.edu/advise/Adversary#ThreatAgent";
		String name = srvr.getUniqueInstanceUri(srvr.getModelName() + "#", "ThreatAgent");
		String instUri = srvr.createInstance(name, className);
		assertTrue(instUri.equals(srvr.getModelName() + "#ThreatAgent1"));
		assertTrue(srvr.addInstance(srvr.getModelName(), instUri, className));
	}

	@Ignore
	@Test
	public void testAddInstanceToUserModel() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, InvalidNameException, IOException, NameNotFoundException, QueryParseException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		srvr.setInstanceDataNamespace("http://www.illinois.edu/advise/models#user1#");
		String className = "http://www.illinois.edu/advise/Adversary#ThreatAgent";
		String name = srvr.getUniqueInstanceUri(srvr.getInstanceDataNamespace() + "#", "ThreatAgent");
		String instUri = srvr.createInstance(name, className);
		assertTrue(instUri.equals(srvr.getInstanceDataNamespace() + "ThreatAgent1"));
		assertTrue(srvr.addInstance(srvr.getInstanceDataNamespace(), instUri, className));
		String[] instances = srvr.getInstancesOfClass(className);
		assertNotNull(instances);
		boolean match = false;
		for (int i = 0; i < instances.length; i++) {
			if (instances[i].equals(srvr.getInstanceDataNamespace() + "ThreatAgent1")) {
				match = true;
				break;
			}
		}
		assertTrue(match);
	}

	@Ignore
	@Test
	public void testAddClass() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddOntProperty() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddOntPropertyDomainClass() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddObjectPropertyRangeClass() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testSetDatatypePropertyRange() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddAllValuesFromRestriction() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddSomeValuesFromRestriction() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddHasValueRestriction() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddCardinalityRestriction() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddMinCardinalityRestriction() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testAddMaxCardinalityRestriction() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testGetErrors() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Test
	public void testCreateInstanceStringStringString() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, InvalidNameException, IOException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String className = "http://www.illinois.edu/advise/Adversary#ThreatAgent";
		String name = srvr.getUniqueInstanceUri(srvr.getModelName() + "#", "ThreatAgent");
		String instUri = srvr.createInstance(srvr.getModelName(), name, className);
		assertTrue(instUri.equals(srvr.getModelName() + "#ThreatAgent1"));
	}

	@Ignore
	@Test
	public void testAddRule() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testDeleteModel() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Test
	public void testGetClassName() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		assertTrue("com.ge.research.sadl.server.server.SadlServerMDImpl".equals(srvr.getClassName()));
	}

	@Test
	public void testGetServiceVersion() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		assertNotNull(srvr.getServiceVersion());
		System.out.println(srvr.getServiceVersion());
	}

	@Test
	public void testQuery() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, QueryCancelledException, QueryParseException, InvalidNameException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String query = "select ?fw where {?fw <rdf:type> <Firewall>}";
		ResultSet rs = srvr.query(srvr.prepareQuery(query));
		assertNotNull(rs);
		assertTrue(rs.getRowCount() > 0);
		System.out.println(rs.toString());
		rs.setShowNamespaces(false);
		System.out.println(rs.toString());
		
		query = "select ?fw where {?fw <rdfs:subClassOf> <Condition>}";
		rs = srvr.query(srvr.prepareQuery(query));
		assertNotNull(rs);
		assertTrue(rs.getRowCount() > 0);
		rs.setShowNamespaces(false);
		System.out.println(rs.toString());

		query = "select ?x ?y where {?x <connectedTo> ?y}";
		rs = srvr.query(srvr.prepareQuery(query));
		assertNotNull(rs);
		assertTrue(rs.getRowCount() > 0);
		rs.setShowNamespaces(false);
		System.out.println(rs.toString());
		
	}

	@Ignore
	@Test
	public void testGetTimingInformation() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Test
	public void testConstruct() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, QueryCancelledException, QueryParseException, InvalidNameException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		DataSource ds = srvr.construct(srvr.prepareQuery("construct {?s ?p ?o} where { values ?p {<subsystemof>} ?s ?p ?o}"));
		assertNotNull(ds);
		String grstr = writeDataSourceToString(ds);
		assertNotNull(grstr);
		System.out.println(grstr);
	}

	@Test
	public void testAsk() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, TripleNotFoundException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		ResultSet rs = srvr.ask("http://www.illinois.edu/advise/SmallExample#SCADA_Network", null, null);
		assertNotNull(rs);
		assertTrue(rs.getRowCount() > 0);
		System.out.println(rs.toString());
		rs.setShowNamespaces(false);
		System.out.println(rs.toString());
	}

	@Test
	public void testSelectServiceModelString() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NamedServiceNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl();
		assertNotNull(srvr);
		srvr.setKbaseRoot(modelFolder);
		assertNotNull(srvr.selectServiceModel("SmallExample"));
	}

	@Ignore
	@Test
	public void testSelectServiceModelStringListOfConfigurationItem() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Test
	public void testSelectServiceModelStringString() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl();
		assertNotNull(srvr);
		assertNotNull(srvr.selectServiceModel(modelFolder, modelName));
	}

	@Ignore
	@Test
	public void testSelectServiceModelStringStringListOfConfigurationItem() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Test
	public void testPrepareQuery() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, InvalidNameException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String qp = srvr.prepareQuery("select ?s ?o where {?s <subsystemof> ?o");
		assertNotNull(qp);
		assertTrue(qp.contains("<http://www.illinois.edu/advise/System#subsystemof>"));
	}

	@Test
	public void testSetOwlFileOutputFormat() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, QueryCancelledException, QueryParseException, InvalidNameException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		srvr.setOutputFormat("N3");
		DataSource ds = srvr.construct(srvr.prepareQuery("construct {?s ?p ?o} where { values ?p {<subsystemof>} ?s ?p ?o}"));
		assertNotNull(ds);
		String grstr = writeDataSourceToString(ds);
		assertNotNull(grstr);
		System.out.println(grstr);
	}

	@Test
	public void testGetKBaseIdentifier() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String kbid = srvr.getKBaseIdentifier();
		assertNotNull(kbid);
		assertTrue(kbid.contains("DataModels"));
		assertTrue(kbid.endsWith("Advise2"));
	}

	@Test
	public void testGetModelName() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, IOException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		assertTrue(modelName.equals(srvr.getModelName()));
	}

	@Test
	public void testGetReasonerVersion() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String rv = srvr.getReasonerVersion();
		assertNotNull(rv);
		System.out.println(rv);
	}

	@Ignore
	@Test
	public void testGetDerivations() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testSetServiceNameMap() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testGetServiceNameMap() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testCollectTimingInformation() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testSetKbaseRoot() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testGetKbaseRoot() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}
	
	@Ignore
	@Test
	public void testAtomicQuery() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testSetQueryTimeout() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}

	@Ignore
	@Test
	public void testClearCache() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
	}
	
	@Test
	public void testGetPropertiesWithGivenClassInDomain() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, InvalidNameException, QueryParseException, QueryCancelledException {
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, modelName);
		assertNotNull(srvr);
		String cls = "http://www.illinois.edu/advise/System#Data";
	    String[] props = srvr.getPropertiesWithGivenClassInDomain(cls);
	    assertNotNull(props);
	}
	
	@Test
	public void testGetDefaultValueOfPropertyOnClass() throws ConfigurationException, ReasonerNotFoundException, SessionNotFoundException, NameNotFoundException, InvalidNameException, QueryParseException, QueryCancelledException {
		String adversaryModelName = "http://www.illinois.edu/advise/Adversary";
		ISadlServerMD srvr = new SadlServerMDImpl(modelFolder, adversaryModelName);
		assertNotNull(srvr);
		String cls = "http://www.illinois.edu/advise/Adversary#TerroristOrganization";
		String prop = "http://www.illinois.edu/advise/Adversary#payoffWeight";
		Object defval = srvr.getDefaultValueOfPropertyOnClass(cls, prop);
		ResultSet rs = srvr.query("select ?dv where {<http://www.illinois.edu/advise/Adversary#TerroristOrganization> <http://www.w3.org/2000/01/rdf-schema#seeAlso> ?sa . ?sa <http://research.ge.com/Acuity/defaults.owl#appliesToProperty> <http://www.illinois.edu/advise/Adversary#payoffWeight> . ?sa <http://research.ge.com/Acuity/defaults.owl#hasDataDefault> ?dv }");
		if (rs != null) {
			System.out.println(rs.toString());
		}
		assertNotNull(defval);
		assertTrue(defval instanceof Float);
		assertTrue((Float)defval == 0.5);
	}

	private static String writeDataSourceToString(DataSource out) {
		InputStream is = null;
		try {
			is = out.getInputStream();
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		    return s.hasNext() ? s.next() : "";
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (is != null) {
			    try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}
		return null;
	}

}