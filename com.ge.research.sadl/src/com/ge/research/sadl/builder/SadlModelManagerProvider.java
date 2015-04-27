/************************************************************************
 * Copyright � 2007-2015 - General Electric Company, All Rights Reserved
 *
 * Project: SADL
 *
 * Description: The Semantic Application Design Language (SADL) is a
 * language for building semantic models and expressing rules that
 * capture additional domain knowledge. The SADL-IDE (integrated
 * development environment) is a set of Eclipse plug-ins that
 * support the editing and testing of semantic models using the
 * SADL language.
 *
 * This software is distributed "AS-IS" without ANY WARRANTIES
 * and licensed under the Eclipse Public License - v 1.0
 * which is available at http://www.eclipse.org/org/documents/epl-v10.php
 *
 ***********************************************************************/
package com.ge.research.sadl.builder;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.emf.common.util.URI;

/**
 * Provides SadlModelManagaers, one per Eclipse project. SadlModelManagers are stored n a Map (smmMap) by key project URI. 
 * Each SadlModelManager has its own instance of a class implementing IConfigurationManagerForIDE. This configuration manager
 * is responsible for mapping of public URIs to alternative URLs for all imports within the project. 
 * Since a project may depend upon another project in the Eclipse settings, thus allowing import of and linking of concepts in one 
 * project's models to models within another project, a check must be made to make sure that no public URIs nor global prefixes
 * are duplicated between the two projects. Such duplication must be reported as an error. 
 * 
 * The ont-policy.rdf mapping file maintained in a project dependent upon another project will have the mappings for any model
 * imported in the project in its own mapping file. These will be cleaned (if SADL-created) when the project is cleaned, just as
 * for mappings within the project. The Jena LocationMapper will have all of the mappings for the project and for the project(s)
 * upon which the active project depends. Switching from one project to another requires that the LocationMapper be reset with
 * respect to its mappings.
 * 
 * @author 200005201
 *
 */
@Singleton
public class SadlModelManagerProvider implements Provider<SadlModelManager>, IResourceChangeListener {
	private URI uri;
	
	private Map<URI, SadlModelManager> smmMap = new HashMap<URI, SadlModelManager>();
	
	
	public SadlModelManager get(URI uri) {
		uri = ResourceManager.getProjectUri(uri);
		setUri(uri);
		return get();
	}
	
	public SadlModelManager getAny() {
		if (smmMap != null && smmMap.size() > 0) {
			return smmMap.values().iterator().next();
		}
		return null;
	}
	
	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	@Override
	public SadlModelManager get() {
		if (smmMap.containsKey(uri)) {
			return smmMap.get(uri);
		}
		SadlModelManager smm = new SadlModelManager(this);
		smmMap.put(uri, smm);
		return smm;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		// TODO Auto-generated method stub
		if (event.getResource() instanceof IProject) {
			String prjPath = event.getResource().getProject().getRawLocationURI().toString(); //FullPath().append(ResourceManager.OWLDIR).toString();
			URI projectUri = URI.createURI(prjPath);
			
			// remove model manager for this project from map
			if (smmMap.containsKey(projectUri)) {
				smmMap.remove(projectUri);
			}
			else {
				System.err.println("Resource '" + event.getResource().getLocationURI() + "' not in SadlModelManagerProvider map");
			}
		}
	}

}
