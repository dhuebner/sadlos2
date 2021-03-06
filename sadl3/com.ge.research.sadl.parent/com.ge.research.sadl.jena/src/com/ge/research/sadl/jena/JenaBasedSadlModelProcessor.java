package com.ge.research.sadl.jena;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.validation.Issue;

import com.ge.research.sadl.model.DeclarationExtensions;
import com.ge.research.sadl.processing.ISadlModelProcessor;
import com.ge.research.sadl.sADL.SadlAllValuesCondition;
import com.ge.research.sadl.sADL.SadlAnnotation;
import com.ge.research.sadl.sADL.SadlCardinalityCondition;
import com.ge.research.sadl.sADL.SadlClassOrPropertyDeclaration;
import com.ge.research.sadl.sADL.SadlCondition;
import com.ge.research.sadl.sADL.SadlDataType;
import com.ge.research.sadl.sADL.SadlDataTypeFacet;
import com.ge.research.sadl.sADL.SadlHasValueCondition;
import com.ge.research.sadl.sADL.SadlImport;
import com.ge.research.sadl.sADL.SadlInstance;
import com.ge.research.sadl.sADL.SadlIntersectionType;
import com.ge.research.sadl.sADL.SadlIsAnnotation;
import com.ge.research.sadl.sADL.SadlModel;
import com.ge.research.sadl.sADL.SadlModelElement;
import com.ge.research.sadl.sADL.SadlPrimitiveDataType;
import com.ge.research.sadl.sADL.SadlProperty;
import com.ge.research.sadl.sADL.SadlPropertyCondition;
import com.ge.research.sadl.sADL.SadlPropertyRestriction;
import com.ge.research.sadl.sADL.SadlResource;
import com.ge.research.sadl.sADL.SadlSimpleTypeReference;
import com.ge.research.sadl.sADL.SadlTypeReference;
import com.ge.research.sadl.sADL.SadlUnionType;
import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenaBasedSadlModelProcessor implements ISadlModelProcessor {

	private OntModel theJenaModel;
	
	enum AnnType {ALIAS, NOTE}
	enum TypeType {CLASS, PROPERTY, DATATYPE, ANNOTATION}
	
	@Inject DeclarationExtensions declarationExtensions;
	private String modelNamespace;
	
	/**
	 * For TESTING
	 * @return
	 */
	public OntModel getTheJenaModel() {
		return theJenaModel;
	}
	
	@Override
	public void onValidate(Resource resource, IAcceptor<Issue> issueAcceptor, CancelIndicator cancelIndicator) {
		SadlModel model = (SadlModel) resource.getContents().get(0);
		String modelActualUrl =resource.getURI().toFileString();
		// directly create the Jena Model here!
		theJenaModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		String modelName = model.getBaseUri();
		setModelNamespace(assureNamespaceEndsWithHash(modelName));
		String modelAlias = model.getAlias();
		if (modelAlias == null) {
			modelAlias = "";
		}
		getTheJenaModel().setNsPrefix(modelAlias, getModelNamespace());
		Ontology modelOntology = getTheJenaModel().createOntology(modelName);
		modelOntology.addComment("This ontology was created from a SADL file '"
				+ modelActualUrl + "' and should not be directly edited.", "en");
		
		String modelVersion = model.getVersion();
		if (modelVersion != null) {
			modelOntology.addVersionInfo(modelVersion);
		}

		EList<SadlAnnotation> anns = model.getAnnotations();
		Iterator<SadlAnnotation> iter = anns.iterator();
		while (iter.hasNext()) {
			SadlAnnotation ann = iter.next();
			String anntype = ann.getType();
			EList<String> annContents = ann.getContents();
			Iterator<String> anniter = annContents.iterator();
			while (anniter.hasNext()) {
				String annContent = anniter.next();
				if (anntype.equals(AnnType.ALIAS)) {
					modelOntology.addLabel(annContent, "en");
				}
				else if (anntype.equals(AnnType.NOTE)) {
					modelOntology.addComment(annContent, "en");
				}
			}
		}
		
		EList<SadlImport> implist = model.getImports();
		Iterator<SadlImport> impitr = implist.iterator();
		while (impitr.hasNext()) {
			SadlImport simport = impitr.next();
			String importUri = simport.getImportURI();
			String importPrefix = simport.getAlias();
//			theJenaModel.addImport(simport.getImportURI(), simport.getAlias());
	    	if (importUri.equals(modelName)) {
	    		// don't import to self
//	    		generate error marker--can't import self
	    	}
	    	else {
		    	// Now add import model (with setCachedModels false so that the actual imports are not loaded. If then need to be loaded 
	    		//	at some point to do semantic validation, they will be loaded then.
	    		if (importPrefix == null) {
// TODO	    		// need to get the prefix from the global prefix in the imported model, if SADL, else from the ont-policy.rdf file if external
//	    			OntologyModel importedModel;//  = simport.getImportedNamespace().getURI();	// this will not work until the grammar treats the import as a reference.
	    		}
	    		if (importPrefix != null) {
	    			getTheJenaModel().setNsPrefix(importPrefix, assureNamespaceEndsWithHash(importUri));
	    		}
		    	com.hp.hpl.jena.rdf.model.Resource importedOntology = getTheJenaModel().createResource(importUri);
		    	modelOntology.addImport(importedOntology);
	    	}
	    	
// TODO	Should the imported model actually be loaded by Jena? The OWL model, whether from SADL or external, will potenatially 
//	    	contain information that is necessary for validation. The only information that will be potentially needed for
//	    	processing the rest of the parse tree is the type of the imported concepts. For imports of SADL models, this type
//	    	information is known in the ResourceSet. Likewise for external OWL imports? 
	    	
//	    	this.getJenaDocumentMgr().setCacheModels(true);
//	   		this.getJenaDocumentMgr().setProcessImports(true);
//	   		ReadFailureHandler rfh = this.getJenaDocumentMgr().getReadFailureHandler();
//	   		if (rfh instanceof SadlReadFailureHandler) {
//	   			((SadlReadFailureHandler)rfh).setSadlConfigMgr(this);
//	   		}
//	    	Conclusion: if checking is needed that requires Jena imported models, then it happens here. 
//	    		Otherwise it happens in the validator.
		}
		
		// process rest of parse tree
		List<SadlModelElement> elements = model.getElements();
		if (elements != null) {
			Iterator<SadlModelElement> elitr = elements.iterator();
			while (elitr.hasNext()) {
				// check for cancelation from time to time
				if (cancelIndicator.isCanceled()) {
					throw new OperationCanceledException();
				}
				SadlModelElement element = elitr.next();
				if (element instanceof SadlClassOrPropertyDeclaration) {
					// Get the names of the declared concepts and store in a list
					List<String> newNames = new ArrayList<String>();
					EList<SadlResource> clses = ((SadlClassOrPropertyDeclaration)element).getClassOrProperty();
					if (clses != null) {
						Iterator<SadlResource> citer = clses.iterator();
						while (citer.hasNext()) {
							SadlResource sr = citer.next();
							String nm = declarationExtensions.getConcreteName(sr);
							newNames.add(nm);
						}
					}
					
					// The declared concept(s) will be of type class, property, or datatype. 
					//	Determining which will depend on the structure, including the superElement....
					// 	Get the superElement
					SadlTypeReference superElement = ((SadlClassOrPropertyDeclaration)element).getSuperElement();
					//		1) if superElement is null then it is a top-level class declaration
					if (superElement == null) {
						createOntClass(getModelNamespace(), newNames.get(0), (OntClass)null);
					}
					//  	2) if superElement is not null then the type of the new concept is the same as the type of the superElement
					// 			the superElement can be:
					// 				a) a SadlSimpleTypeReference
					else if (superElement instanceof SadlSimpleTypeReference) {
						//TODO need to get the type (class or property or datatype declaration) from the ResourceSet--how?
						SadlResource superSR = ((SadlSimpleTypeReference)superElement).getType();
// TODO Is this just the local name or is it the URI?
						String superSRUri = declarationExtensions.getConcreteName(superSR);	
// TODO how do I get the URI and the type from the ResourceSet?	
						TypeType superElementType = null;
						try {
							superElementType = getSadlTypeReferenceType(superElement);
						} catch (JenaProcessorException e) {
							// TODO Auto-generated catch block
// TODO need to create an issue via issueAcceptor							
							e.printStackTrace();
							continue;	// go to next element
						}  
						if (superElementType.equals(TypeType.CLASS)) {
							for (int i = 0; i < newNames.size(); i++) {
								createOntClass(getModelNamespace(), newNames.get(i), superSRUri);
							}
						}
						else if (superElementType.equals(TypeType.PROPERTY)){
							for (int i = 0; i < newNames.size(); i++) {
								createOntProperty(getModelNamespace(), newNames.get(i), superSRUri);
							}
						}
						else if (superElementType.equals(TypeType.ANNOTATION)) {
							for (int i = 0; i < newNames.size(); i++) {
								createAnnotationProperty(getModelNamespace(), newNames.get(i));
							}
						}
					}
					//				b) a SadlPrimitiveDataType
					else if (superElement instanceof SadlPrimitiveDataType) {
						SadlDataTypeFacet dtf = ((SadlClassOrPropertyDeclaration)element).getFacet();
						SadlDataType sdt = ((SadlPrimitiveDataType)superElement).getPrimitiveType();
						// pull out facets, create rdfs:Datatype
					}
					//				c) a SadlPropertyCondition
					else if (superElement instanceof SadlPropertyCondition) {
						SadlResource sr = ((SadlPropertyCondition)superElement).getProperty();
						Iterator<SadlCondition> conditer = ((SadlPropertyCondition)superElement).getCond().iterator();
						while (conditer.hasNext()) {
							SadlCondition cond = conditer.next();
							if (cond instanceof SadlAllValuesCondition) {
								
							}
							else if (cond instanceof SadlHasValueCondition) {
								
							}
							else if (cond instanceof SadlCardinalityCondition) {
								// Note: SomeValuesFrom is embedded in cardinality in the SADL grammar--an "at least" cardinality with "one" instead of # 
								String cardinality = ((SadlCardinalityCondition)cond).getCardinality();
								if (cardinality.equals("one")) {
									
								}
								else {
									int cardNum = Integer.parseInt(cardinality);
									
								}
							}
						}		
					}
					//				d) a SadlTypeReference
					else if (superElement instanceof SadlTypeReference) {
						// this can only be a class; can't create a property as a SadlTypeReference
						try {
							OntClass superCls = sadlTypeReferenceToOntClass(superElement);
						} catch (JenaProcessorException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	
				}
				else if (element instanceof SadlProperty) {
					// this has two forms:
					//	1) <name> is a property...
					//	2) relationship of <Domain> to <Range> is <name>
					SadlResource sr = ((SadlProperty)element).getNameOrRef();
					if (sr == null) {
						sr = ((SadlProperty)element).getProperty();
					}
					String propName = declarationExtensions.getConcreteName(sr);
					Iterator<SadlPropertyRestriction> itr = ((SadlProperty)element).getRestrictions().iterator();
					if (itr.hasNext()) {
						while (itr.hasNext()) {
							SadlPropertyRestriction rest = itr.next();
							if (rest instanceof SadlIsAnnotation) {
								createAnnotationProperty(getModelNamespace(), propName);
							}
						}
					}
					else {
						OntProperty ontProp = createOntProperty(getModelNamespace(), propName, null);
						Iterator<SadlPropertyRestriction> restiter = ((SadlProperty)element).getRestrictions().iterator();
						while (restiter.hasNext()) {
							int i = 0;
						}
					}
				}
				else if (element instanceof SadlInstance) {
					// this has two forms:
					//	1) <name> is a <type> ...
					//	2) a <type> <name> ....
					SadlTypeReference type = ((SadlInstance)element).getType();
					SadlResource sr = ((SadlInstance)element).getNameOrRef();
					if (sr == null) {
						Iterator<SadlResource> instItr = ((SadlInstance)element).getInstance().iterator();
						if (instItr.hasNext()) {
							sr = instItr.next();
						}
					}
					if (sr != null) {
						String instName = declarationExtensions.getConcreteName(sr);
						OntClass cls;
						try {
							cls = sadlTypeReferenceToOntClass(type);
						} catch (JenaProcessorException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
//							issueAcceptor.accept(new Issue());
// TODO should this use the issueAccpetor?							
							return;	// fatal
						}
						createIndividual(getModelNamespace(), instName, cls);
					}
				}
				else {
					System.out.println("onValidate for element of type '" + element.getClass().getCanonicalName() + "' not implemented");
				}
			}
		}
	}

	@Override
	public void onGenerate(Resource resource, IFileSystemAccess2 fsa, CancelIndicator cancelIndicator) {
		
	}
	
	/**
	 * Call this method before doing semantic validity checks that require imports to be loaded as Jena OntModels.
	 */
	private void loadImportsInJena() {
		
	}
	
	private OntClass createOntClass(String modelNamespace, String newName, String superSRUri) {
		if (superSRUri != null) {
			OntClass superCls = getTheJenaModel().getOntClass(superSRUri);
			return createOntClass(modelNamespace, newName, superCls);
		}
		return createOntClass(modelNamespace, newName, (OntClass)null);
	}
	
	private OntClass createOntClass(String modelNamespace, String newName, OntClass superCls) {
		OntClass newCls = getTheJenaModel().createClass(getUri(modelNamespace, newName));
		if (superCls != null) {
			newCls.addSuperClass(superCls);
		}
		return newCls;
	}
	
	private OntProperty createOntProperty(String modelNamespace, String newName, String superSRUri) {
		OntProperty newProp = getTheJenaModel().createOntProperty(getUri(modelNamespace, newName));
		if (superSRUri != null) {
			OntProperty superProp = getTheJenaModel().getOntProperty(superSRUri);
			newProp.addSuperProperty(superProp);
		}
		return newProp;
	}
	
	private Individual createIndividual(String modelNamespace, String newName, String superSRUri) {
		OntClass cls = getTheJenaModel().getOntClass(superSRUri);
		return createIndividual(modelNamespace, newName, cls);
	}
	
	private Individual createIndividual(String modelNamespace, String newName, OntClass supercls) {
		Individual inst = getTheJenaModel().createIndividual(getUri(modelNamespace, newName), supercls);
		return inst;
	}
	
	private void createAnnotationProperty(String modelNamespace, String newName) {
		getTheJenaModel().createAnnotationProperty(getUri(modelNamespace, newName));
		
	}

	private OntClass sadlTypeReferenceToOntClass(SadlTypeReference sadlTypeRef) throws JenaProcessorException {
		OntClass cls = null;
		// TODO How do we tell if this is a union versus an intersection?						
		if (sadlTypeRef instanceof SadlSimpleTypeReference) {
			SadlResource strSR = ((SadlSimpleTypeReference)sadlTypeRef).getType();
//TODO Is this just the local name or is it the URI?
			String strSRUri = declarationExtensions.getConcreteName(strSR);	
//TODO how do I get the URI and the type from the ResourceSet?	
			return getTheJenaModel().getOntClass(validateUri(strSRUri));
		}
		else if (sadlTypeRef instanceof SadlPrimitiveDataType) {
			SadlDataType pt = ((SadlPrimitiveDataType)sadlTypeRef).getPrimitiveType();
			throw new JenaProcessorException("Cannot convert primitive data type (" + pt.toString() + ") to OntClass");
		}
		else if (sadlTypeRef instanceof SadlPropertyCondition) {
			SadlResource sr = ((SadlPropertyCondition)sadlTypeRef).getProperty();
			Iterator<SadlCondition> conditer = ((SadlPropertyCondition)sadlTypeRef).getCond().iterator();
			while (conditer.hasNext()) {
				SadlCondition cond = conditer.next();
				if (cond instanceof SadlAllValuesCondition) {
					
				}
				else if (cond instanceof SadlHasValueCondition) {
					
				}
				else if (cond instanceof SadlCardinalityCondition) {
					// Note: SomeValuesFrom is embedded in cardinality in the SADL grammar--an "at least" cardinality with "one" instead of # 
					String cardinality = ((SadlCardinalityCondition)cond).getCardinality();
					if (cardinality.equals("one")) {
						
					}
					else {
						int cardNum = Integer.parseInt(cardinality);
						
					}
				}
			}		
		}
		else if (sadlTypeRef instanceof SadlUnionType) {
			
		}
		else if (sadlTypeRef instanceof SadlIntersectionType) {
			
		}
		return cls;
	}

	private TypeType getSadlTypeReferenceType(SadlTypeReference sadlTypeRef) throws JenaProcessorException {
		if (sadlTypeRef instanceof SadlSimpleTypeReference) {
			SadlResource sr = ((SadlSimpleTypeReference)sadlTypeRef).getType();
			String name = declarationExtensions.getConcreteName(sr);
// TODO  here we have to get the type, class or property, from the Xtext ResourceSet--how do we do that?
			// default for now to class
			return TypeType.CLASS;
		}
		else if (sadlTypeRef instanceof SadlPrimitiveDataType) {
			return TypeType.DATATYPE;
		}
		else if (sadlTypeRef instanceof SadlPropertyCondition) {
			// property conditions => OntClass
			return TypeType.CLASS;
		}
		else if (sadlTypeRef instanceof SadlUnionType) {
			SadlTypeReference lft = ((SadlUnionType)sadlTypeRef).getLeft();
			TypeType lfttype = getSadlTypeReferenceType(lft);
			return lfttype;
//			SadlTypeReference rght = ((SadlUnionType)sadlTypeRef).getRight();
		}
		else if (sadlTypeRef instanceof SadlIntersectionType) {
			SadlTypeReference lft = ((SadlIntersectionType)sadlTypeRef).getLeft();
			TypeType lfttype = getSadlTypeReferenceType(lft);
			return lfttype;
//			SadlTypeReference rght = ((SadlIntersectionType)sadlTypeRef).getRight();
		}
		throw new JenaProcessorException("Unexpected SadlTypeReference subtype: " + sadlTypeRef.getClass().getCanonicalName());
	}

	private String validateUri(String strSRUri) {
		if (!strSRUri.contains("#")) {
			return getUri(getModelNamespace(), strSRUri);
		}
		return strSRUri;
	}

	private String assureNamespaceEndsWithHash(String name) {
		name = name.trim();
		if (!name.endsWith("#")) {
			return name + "#";
		}
		return name;
	}

	private String getUri(String modelNamespace, String nm) {
		modelNamespace = assureNamespaceEndsWithHash(modelNamespace);
		return modelNamespace + nm;
	}

	private String getModelNamespace() {
		return modelNamespace;
	}

	private void setModelNamespace(String modelNamespace) {
		this.modelNamespace = modelNamespace;
	}

}
