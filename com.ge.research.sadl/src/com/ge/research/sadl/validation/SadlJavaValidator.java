/*
* generated by Xtext
*/
package com.ge.research.sadl.validation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IGlobalScopeProvider;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ge.research.sadl.builder.IConfigurationManagerForIDE;
import com.ge.research.sadl.builder.ResourceManager;
import com.ge.research.sadl.builder.SadlModelManager;
import com.ge.research.sadl.builder.SadlModelManagerProvider;
import com.ge.research.sadl.model.PendingModelError;
import com.ge.research.sadl.reasoner.ConfigurationException;
import com.ge.research.sadl.reasoner.IConfigurationManager;
import com.ge.research.sadl.reasoner.IConfigurationManagerForEditing.Scope;
import com.ge.research.sadl.sadl.Model;
import com.ge.research.sadl.sadl.Import;
import com.ge.research.sadl.sadl.ModelName;
import com.ge.research.sadl.sadl.ResourceByName;
import com.ge.research.sadl.sadl.ResourceIdentifier;
import com.ge.research.sadl.sadl.ResourceName;
import com.ge.research.sadl.sadl.SadlPackage;
import com.ge.research.sadl.utils.SadlUtils;
import com.ge.research.sadl.utils.SadlUtils.ConceptType;
import com.google.inject.Inject;

/**
 * Custom validation rules. 
 *
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
public class SadlJavaValidator extends com.ge.research.sadl.validation.AbstractSadlJavaValidator {
	public static final String MISSING_MODEL_NAME = "com.ge.research.SADL.MissingModelName";
	public static final String MISSING_HTTP_PREFIX = "com.ge.research.SADL.MissingHttpPrefix";
	public static final String DUPLICATE_MODEL_NAME = "com.ge.research.SADL.DuplicateModelName";
	public static final String IMPROPER_IMPORT_FILE_URI = "com.ge.research.SADL.ImproperImportFileUri";
	public static final String ADD_URI_END_CHAR = "com.ge.research.SADL.AddUriEndChar";
	public static final String ADD_MODEL_VERSION = "com.ge.research.SADL.AddModelVersion";
	public static final String ADD_GLOBAL_ALIAS = "com.ge.research.SADL.AddModelGlobalAlias";
	public static final String ONTCLASS_NOT_DEFINED = "com.ge.research.SADL.OntClassNotDefined";
	public static final String OBJECTPROPERTY_NOT_DEFINED = "com.ge.research.SADL.ObjectPropertyNotDefined";
	public static final String DATATYPEPROPERTY_NOT_DEFINED = "com.ge.research.SADL.DatatypePropertyNotDefined";
	public static final String INSTANCE_NOT_DEFINED = "com.ge.research.SADL.InstanceNotDefined";
	public static final String MISSING_ALIAS = "com.ge.research.SADL.MissingAlias";
	public static final String DOUBLE_ALIAS = "com.ge.research.SADL.DoubleAlias";
	public static final String AMBIGUOUS_NAME = "com.ge.researech.SADL.AmbiguousName";
	public static final String DUPLICATE_ALIAS = "com.ge.research.SADL.DuplicateAlias";
    
    private static final Logger logger = LoggerFactory.getLogger(SadlJavaValidator.class);
    
    @Inject
	private SadlModelManagerProvider sadlModelManagerProvider;
    
    @Inject
    private IGlobalScopeProvider globalScopeProvider;
    @Inject
    private IQualifiedNameProvider qnProvider;
    @Inject
    private IQualifiedNameConverter qnConverter;
    
    /**
     * Through (transitive) imports the short name ResourceName referred by a ResourceNyName might
     * be multiple times on the scope, but only the first one is linked. There should be a warning that
     * this is ambiguous.
     * @param rn
     */
//    @Check(CheckType.NORMAL)
    @Check
    public void checkNoAmbiguousQualifiedNameOnScope (ResourceByName rn) {
    	Resource resource = rn.eResource();
    	if (resource==null || rn.getName().eIsProxy()) return;
    	// the qualified name of the actually linked ResourceName
    	String crossRefString = NodeModelUtils.getTokenText(NodeModelUtils.findActualNodeFor(rn));
    	if (qnConverter.toQualifiedName(crossRefString).getSegmentCount()>1) return; // only interested in unqualified names
    	
    	// now get the qn of the actual linked ResourceName
    	QualifiedName qn = qnProvider.getFullyQualifiedName(rn.getName());
    	// search the global scope for any other name
    	IScope scope = globalScopeProvider.getScope(resource, SadlPackage.Literals.RESOURCE_BY_NAME__NAME, null);
    	List<String> ambiguousNSs = null;
    	for (IEObjectDescription description : scope.getAllElements()) {
			if (qn != null && qn.getLastSegment().equals(description.getQualifiedName().getLastSegment()) && !qn.equals(description.getQualifiedName())) {
				// is this an import of the Resource?
				if (isImportOfResource(resource, description)) {
					if (ambiguousNSs == null) {
						ambiguousNSs = new ArrayList<String>();
						if (qn.getSegmentCount() > 1) {
							ambiguousNSs.add(qn.getFirstSegment() + ":" + qn.getLastSegment());
						}
						else {
							ambiguousNSs.add(qn.getLastSegment());
						}
					}
					ambiguousNSs.add(description.getQualifiedName().getFirstSegment() + ":" + qn.getLastSegment());
				}
			}
		}
    	if (ambiguousNSs != null) {
    		StringBuilder sb = new StringBuilder();
    		Iterator<String> itr = ambiguousNSs.iterator();
    		int cntr = 0;
    		while (itr.hasNext()) {
    			if (cntr > 0) sb.append(",");
    			sb.append(itr.next());
    			cntr++;
    		}
			error("The name "+qn.getLastSegment()+" is ambiguous. Please qualify the name.", rn, SadlPackage.Literals.RESOURCE_BY_NAME__NAME,
					AMBIGUOUS_NAME, sb.toString());    		
    	}
    }
	
	private boolean isImportOfResource(Resource resource, IEObjectDescription description) {
		TreeIterator<EObject> contents = resource.getAllContents();
		boolean modelNameFound = false;
		while (contents.hasNext()) {
			EObject tobj = contents.next();
			if (tobj instanceof ModelName) {
				modelNameFound = true;
			}
			else if (tobj instanceof Import) {
				String impUri = ((Import)tobj).getImportURI();
				String candidatePrefix = description.getQualifiedName().getFirstSegment();
				if (((Import)tobj).getAlias() != null) {
					// the import has a local alias
					if (((Import)tobj).getAlias().equals(candidatePrefix)) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
			        try {
				        SadlModelManager visitor = sadlModelManagerProvider.get(resource);
				        URI modelFolder = SadlModelManager.getProjectFromResourceSet(resource.getResourceSet()).appendSegment(ResourceManager.OWLDIR);
						IConfigurationManagerForIDE configMgr = visitor.getConfigurationMgr(modelFolder.toString());
						if (!impUri.startsWith(ResourceManager.HTTP_URL_PREFIX)) {
							String owlUrl = null;
							if (impUri.endsWith(ResourceManager.SADLEXTWITHPREFIX)) {
								owlUrl = modelFolder.appendSegment(impUri).trimFileExtension().appendFileExtension(ResourceManager.OWLFILEEXT).toString();
							}
							else {
								owlUrl = ResourceManager.validateAndReturnOwlUrlOfSadlUri(URI.createURI(impUri)).toString();	
							}
							impUri = configMgr.getPublicUriFromActualUrl(owlUrl);
						}
						String gprefix = configMgr.getGlobalPrefix(impUri);
						if (gprefix != null && gprefix.equals(candidatePrefix)) {
							return true;
						}
						Map<String, String> impMap = configMgr.getImports(impUri, Scope.INCLUDEIMPORTS);
						Iterator<String> itr = impMap.keySet().iterator();
						while (itr.hasNext()) {
							String key = itr.next();
							String val = impMap.get(key);
							if (val != null && val.equals(candidatePrefix)) {
								return true;
							}
						}
						return false;
					} catch (ConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
				}
			}
			else if (!(tobj instanceof Model)) {
				// we're past any possible imports
				break;
			}
		}
		return false;
	}

	@Check
	public void checkModelName(ModelName uri) {
	    // Most of our checks will focus on the baseUri feature.
	    String baseUri = uri.getBaseUri();

		if (baseUri == null || baseUri.isEmpty()) {
			error("Model name cannot be empty", uri, SadlPackage.Literals.MODEL_NAME__BASE_URI, MISSING_MODEL_NAME);			
		}
		else if (!baseUri.startsWith(IConfigurationManager.HTTP_URL_PREFIX)) {
			error("Model name must be a valid URI", SadlPackage.Literals.MODEL_NAME__BASE_URI, MISSING_HTTP_PREFIX, baseUri);
		}
		else {
			try {
				baseUri = ResourceManager.validateHTTP_URI(baseUri);
			}
			catch (Exception e) {
		        error(e.getLocalizedMessage(), SadlPackage.Literals.MODEL_NAME__BASE_URI);
				
			}
	        try {
	            URI modelUrl = uri.eResource().getURI();
                String owlUrl = ResourceManager.validateAndReturnOwlUrlOfSadlUri(modelUrl).toString();
		        String publicUri = baseUri;
		        SadlModelManager visitor = sadlModelManagerProvider.get(uri.eResource());
		        String altUri = visitor.getAltUrl(publicUri, modelUrl);
		        // If those urls differ, that indicates another model has already used the public uri
		        if (altUri != null && !publicUri.equals(altUri) && owlUrl != null && !owlUrl.equals(altUri)) {
		            error("Model name must be unique", SadlPackage.Literals.MODEL_NAME__BASE_URI, DUPLICATE_MODEL_NAME, baseUri);
		        }
		        String alias = uri.getAlias();
		        String otherUri = visitor.getConfigurationMgr(modelUrl.toString()).getUriFromGlobalPrefix(alias);
		        if (otherUri != null && !otherUri.equals(publicUri)) {
		        	error("Global prefix must be unique but '" + alias + "' is already used for namespace '" + otherUri + "'", 
		        			SadlPackage.Literals.MODEL_NAME__ALIAS, DUPLICATE_ALIAS, alias);
		        }
            }
	        catch (CoreException e) {
                e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (uri.getVersion() == null) {
			warning("No model version present", uri, SadlPackage.Literals.MODEL_NAME__VERSION, ADD_MODEL_VERSION);
		}
		if (uri.getAlias() == null) {
			warning("Model does not have a global alias", uri, SadlPackage.Literals.MODEL_NAME__ALIAS, ADD_GLOBAL_ALIAS);
		}
	}
	
    @Check
    public void checkImport(Import imp) {
    	String uri = imp.getImportURI();
		if (uri.startsWith(ResourceManager.FILE_URL_PREFIX)) {
			warning("File-type URL is improper; hyperlinking may not work.", imp, SadlPackage.Literals.IMPORT__IMPORT_URI, IMPROPER_IMPORT_FILE_URI);
		}
		// do we have a global alias?
		try {
			SadlModelManager visitor = sadlModelManagerProvider.get(imp.eResource());
    		IConfigurationManagerForIDE configMgr = visitor.getConfigurationMgr((String)null);
    		if (uri.endsWith(ResourceManager.SADLEXTWITHPREFIX)) {
    	    	URI importedURI = URI.createURI(uri);
    	    	if (importedURI.scheme()==null) {
    	    		importedURI = importedURI.resolve(imp.eResource().getURI());
    	    	}

    	    	Resource importingResource = imp.eResource();

    	    	if (importedURI.isPlatformResource()) {
    	    		String rawSadlFilename = importedURI.toString();
    	    		if (rawSadlFilename.startsWith(ResourceManager.FILE_URL_PREFIX) &&
    	    				rawSadlFilename.endsWith(ResourceManager.SADLEXT)) {
    	    			rawSadlFilename = rawSadlFilename.substring(ResourceManager.FILE_URL_PREFIX.length());
    	    			int lastSegmentDivider = rawSadlFilename.lastIndexOf("/");
    	    			if (lastSegmentDivider > 0) {
    	    				rawSadlFilename = rawSadlFilename.substring(lastSegmentDivider + 1);
    	    			}
    	    			importedURI = URI.createFileURI(rawSadlFilename);
    	    		}
    	    		importedURI = ResourceManager.convertPlatformUriToAbsoluteUri(importedURI);
    	    		// Convert any SADL URI to an OWL URI.
    	    		try {
    	    			if (ResourceManager.SADLEXT.equalsIgnoreCase(importedURI.fileExtension())) {
    	    				importedURI = ResourceManager.validateAndReturnOwlUrlOfSadlUri(importedURI);
    	    			}
    	    			if (ResourceManager.OWLFILEEXT.equals(importedURI.fileExtension())) {
     	    				importedURI = URI.createURI(configMgr.getPublicUriFromActualUrl(importedURI.toString()));
    	    			}
    	    			uri = importedURI.toString();
    	    		}
    	    		catch (CoreException e) {
    	    			// TODO Auto-generated catch block
    	    			e.printStackTrace();
    	    		} catch (ConfigurationException e) {
    	    			SadlModelManager smMgr = sadlModelManagerProvider.get(importingResource);
    	    			smMgr.getMessageManager().error(e.getMessage());
    	    		}

    	    	}
       		}
    		if (!configMgr.containsMappingForURI(uri)) {
    			configMgr = ResourceManager.findConfigurationManagerInOtherProject(sadlModelManagerProvider, 
    					ResourceManager.getProjectUri(imp.eResource().getURI()).appendSegment(ResourceManager.OWLDIR), uri);
    		}
    		if (configMgr != null) {
    			if (uri.endsWith(ResourceManager.SADLEXTWITHPREFIX)) {
    				if (uri.startsWith(ResourceManager.FILE_URL_PREFIX)) {
		    			int idx = ResourceManager.FILE_URL_PREFIX.length();
		    			while (idx < uri.length() && uri.charAt(idx) == '/') {
		    				idx++;
		    			}
		    			uri = uri.substring(idx);
    				}
    				
					URI projectUri = ResourceManager.getProjectUri(visitor.getModelResource().getURI());
					SadlUtils su = new SadlUtils();
					URI sadlUri = URI.createURI(su.fileNameToFileUrl(ResourceManager.findSadlFileInProject(projectUri.toFileString(), uri)));
					URI owlUri = ResourceManager.validateAndReturnOwlUrlOfSadlUri(sadlUri);
					uri = configMgr.findPublicUriOfOwlFile(owlUri.toString());
	    		}
    			String globalAlias = configMgr.getGlobalPrefix(uri);		// global alias of imported model
    			if (globalAlias == null && imp.getAlias() == null) {
	    			warning("An imported model without a global alias may need a local alias.", imp, SadlPackage.Literals.IMPORT__ALIAS, MISSING_ALIAS);			        			
	    		}
	    		else if (globalAlias != null && imp.getAlias() != null) {
	    			warning("Giving a local alias to an imported model with a global alias may be confusing.", imp, SadlPackage.Literals.IMPORT__ALIAS, DOUBLE_ALIAS);			        				    			
	    		}
    			if (imp.getAlias() != null) {
    				// check to see if any imports of this model or of the new import use this alias
    				Map<String, String> importImportMap = configMgr.getImports(uri, Scope.INCLUDEIMPORTS);
    				Iterator<String> iimItr = importImportMap.keySet().iterator();
    				while (iimItr.hasNext()) {
    					String key = iimItr.next();
    					String val = importImportMap.get(key);
    					if (val != null && val.equals(imp.getAlias())) {
    						error("Alias '" + imp.getAlias() + "' is the same as the alias give to imported model '" + key + "'.", imp, SadlPackage.Literals.IMPORT__ALIAS, DUPLICATE_ALIAS);
    					}
    				}
    				EObject model = imp.eContainer();
    				Map<String, String> otherImportsMap = configMgr.getImports(((Model)model).getModelName().getBaseUri(), Scope.INCLUDEIMPORTS);
    				Iterator<String> oimItr = otherImportsMap.keySet().iterator();
    				while (oimItr.hasNext()) {
    					String key = oimItr.next();
    					if (!key.equals(uri)) {
	    					String val = otherImportsMap.get(key);
	    					if (val != null && val.equals(imp.getAlias())) {
	    						error("Alias '" + imp.getAlias() + "' is the same as the alias give to imported model '" + key + "'.", imp, SadlPackage.Literals.IMPORT__ALIAS, DUPLICATE_ALIAS);
	    					}
    					}
    				}
    			}
    		}
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (imp.getAlias() == null) {
		}
    }
    
	@Check
	public void checkResourceIdentifier(ResourceIdentifier rsrcId) {
		if (rsrcId instanceof ResourceByName) {
			ResourceName name = ((ResourceByName)rsrcId).getName();
			if (name != null) {
				String nm = name.getName();
				if (nm == null) {
					logger.debug("Unresolved name doesn't allow a quick fix to be identified.");
				}
//				if (visitor.getModelBaseUri() == null) {
//					// the model hasn't been parsed so we can't generate quick fixes until we parse it
//					visitor.processModel(rsrcId.resource, false, null)
//				}
				Resource resource = rsrcId.eResource();
				PendingModelError pendingErr = null;
				SadlModelManager visitor;
				try {
					visitor = sadlModelManagerProvider.get(resource);
					if (visitor.hasModelManager(resource)) { 
						pendingErr = visitor.getPendingError(resource, nm);
					}
					if (pendingErr != null) {
						if (pendingErr.getConceptType().equals(ConceptType.ONTCLASS)) {
							error("Class not found", rsrcId, SadlPackage.Literals.RESOURCE_BY_NAME__NAME, ONTCLASS_NOT_DEFINED);
						}
						else if (pendingErr.getConceptType().equals(ConceptType.OBJECTPROPERTY)) {
							error("Object property not found", rsrcId, SadlPackage.Literals.RESOURCE_BY_NAME__NAME, OBJECTPROPERTY_NOT_DEFINED);
						}
						else if (pendingErr.getConceptType().equals(ConceptType.DATATYPEPROPERTY)) {
							error("Data property not found", rsrcId, SadlPackage.Literals.RESOURCE_BY_NAME__NAME, DATATYPEPROPERTY_NOT_DEFINED);	
						}
						else if (pendingErr.getConceptType().equals(ConceptType.INDIVIDUAL)) {
							error("Instance not found", rsrcId, SadlPackage.Literals.RESOURCE_BY_NAME__NAME, INSTANCE_NOT_DEFINED);	
						}
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
