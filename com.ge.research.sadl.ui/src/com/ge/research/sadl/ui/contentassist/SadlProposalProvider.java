/*
* generated by Xtext
*/
package com.ge.research.sadl.ui.contentassist;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.impl.AliasedEObjectDescription;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

import com.ge.research.sadl.builder.ResourceManager;
import com.ge.research.sadl.builder.SadlModelManager;
import com.ge.research.sadl.builder.SadlModelManagerProvider;
import com.ge.research.sadl.model.ConceptName;
import com.ge.research.sadl.model.ImportMapping;
import com.ge.research.sadl.reasoner.ConfigurationException;
import com.ge.research.sadl.reasoner.IConfigurationManagerForEditing.Scope;
import com.ge.research.sadl.reasoner.InvalidNameException;
import com.ge.research.sadl.sadl.Import;
import com.ge.research.sadl.sadl.ModelName;
import com.ge.research.sadl.sadl.PropOfSubj;
import com.ge.research.sadl.sadl.ResourceName;
import com.ge.research.sadl.sadl.SadlPackage;
import com.ge.research.sadl.sadl.TypedBNode;
import com.ge.research.sadl.services.SadlGrammarAccess;
import com.ge.research.sadl.utils.SadlUtils.ConceptType;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#contentAssist on how to customize content assistant
 */
public class SadlProposalProvider extends AbstractSadlProposalProvider {
    
    @Inject
	private SadlModelManagerProvider sadlModelManagerProvider;
    
    private SadlGrammarAccess g;
    private IPreferenceStoreAccess preferencesAccess;
    
    /** Collection of keywords which require a space before */
    private Set<Keyword> keywordsWithSpaceBefore = Sets.newHashSet();
    /** Collection of keywords which require a space after */
    private Set<Keyword> keywordsWithSpaceAfter = Sets.newHashSet();
    
    private boolean bRemoveUnnecessaryPrefixes;
    @Inject
    public SadlProposalProvider(SadlGrammarAccess g, IPreferenceStoreAccess preferencesAccess) {
    	this.g = g;
		this.preferencesAccess = preferencesAccess;
    	
    	initKeywordRules();
    	IPreferenceStore preferenceStore = preferencesAccess.getPreferenceStore();

    	bRemoveUnnecessaryPrefixes = preferenceStore.getBoolean("prefixesOnlyAsNeeded");
	}

    /**
     * Prevents that a proposal is shown if it was already inserted and assist is invoked directly after the insertion
     */
    class SadlCompletionProposalAcceptor extends ICompletionProposalAcceptor.Delegate {
		private ContentAssistContext context;

		public SadlCompletionProposalAcceptor (ICompletionProposalAcceptor delegate, ContentAssistContext context) {
			super(delegate);
			this.context = context;
    	}
		
		@Override
		public void accept(ICompletionProposal proposal) {
			String proposalText = null;
			if (proposal instanceof ConfigurableCompletionProposal) {
				proposalText = ((ConfigurableCompletionProposal)proposal).getReplacementString();
			} else {
				proposalText = proposal.getDisplayString();
			}
			if (!proposalText.equals(context.getPrefix()) && !proposalText.equals(context.getCurrentNode().getText())) {
				getDelegate().accept(proposal);
			}
		}

    }

    /**
     * {@inheritDoc}
     * Replace acceptor by own type
     */
    @Override
	public void createProposals(ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    	super.createProposals(context, new SadlCompletionProposalAcceptor(acceptor, context));
	}
    
    
    private void initKeywordRules () {
    	Set<Keyword> allKeywords = Sets.newHashSet();
    	
		for (AbstractRule ar : GrammarUtil.allRules(g.getGrammar())) {
			allKeywords.addAll(IterableExtensions.toSet(EcoreUtil2.eAllOfType(ar, Keyword.class)));
		}
    	// Remove all keywords which should not be in the collection
    	keywordsWithSpaceBefore.addAll(allKeywords);
    	keywordsWithSpaceBefore.remove(g.getModelNameAccess().getUriKeyword_0());
    	keywordsWithSpaceBefore.removeAll(g.findKeywords(")"));
    	keywordsWithSpaceBefore.removeAll(g.findKeywords("note"));
    	
    	// Remove all keywords which should not be in the collection
    	keywordsWithSpaceAfter.addAll(allKeywords);
    	keywordsWithSpaceAfter.removeAll(g.findKeywords("("));
    }

    // Creates a proposal for a STRING feature of an EObject.  We will
    // filter out the proposal for the baseUri of a model name since we'll
    // be generating that proposal ourselves in the next method.
	public void complete_STRING(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (doCreateStringProposals()) {
			Assignment ass = GrammarUtil.containingAssignment(ruleCall);
			String feature = (ass != null) ? ass.getFeature() : null;
			if (!"baseUri".equals(feature)) {
				if ("importURI".equals(feature)) {
			        List<ConceptName> names;
					try {
						SadlModelManager visitor = sadlModelManagerProvider.get(model.eResource());
						names = visitor.getNamedConceptsOfType(ConceptType.MODELNAME, Scope.INCLUDEIMPORTS);
				        if (names != null) {
				        	Collection<ImportMapping> impmappings = visitor.getModelImportMappings();
				        	if (impmappings != null) {
					        	Iterator<ImportMapping> itr = impmappings.iterator();
					        	while (itr.hasNext()) {
					        		ImportMapping im = itr.next();
					        		if (names.contains(im.getPublicURI())) {
					        			names.remove(im.getPublicURI());
					        		}
					        	}
				        	}
				        	if (!names.isEmpty()) {
				                for (ConceptName name : names) {
				            	    String proposalText = getValueConverter().toString(name.toString(), "STRING");
				            	    String displayText = proposalText;
				            		Image image = getImage(model);
				            		ICompletionProposal proposal = createCompletionProposal(proposalText, displayText, image, context);
				            		acceptor.accept(proposal);
				        	    }
				        	}
				        }
					} catch (MalformedURLException | InvalidNameException
							| ConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
				}
				else {
					super.complete_STRING(model, ruleCall, context, acceptor);
				}
			}
		}
	}

    // Creates a proposal for the baseUri of a model name.  We want
    // the baseUri to be a string that starts with "http://...."
	public void completeModelName_BaseUri(EObject model, Assignment assignment, 
	        ContentAssistContext context, ICompletionProposalAcceptor acceptor) {

	    URI modelURI = model.eResource().getURI();
	    String baseUri;
		try {
			baseUri = generateBaseUri(modelURI);
		    String proposalText = getValueConverter().toString(baseUri, "STRING");
		    String displayText = proposalText + " - Uri of Model";
			Image image = getImage(model);
			// no space after 'uri' keyword would be syntactically incorrect, so insert one
			// check: last node was 'uri' keyword and content assist is invoked directly after the keyword
			if (context.getLastCompleteNode().getText().equals(g.getModelNameAccess().getUriKeyword_0().getValue()) && context.getLastCompleteNode().getTotalEndOffset()==context.getOffset()) {
				proposalText = " " + proposalText;
			}
			ICompletionProposal proposal = createCompletionProposal(proposalText, displayText, image, context);
			acceptor.accept(proposal);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	// Creates a proposal for a NAME, 
	public void complete_NAME(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		String ctx = context.getLastCompleteNode().getText();
		String name = null;
		if (ctx.equals( "alias") || ctx.equals( "as")) {
			if (model instanceof ModelName) {
				name = ((ModelName)model).getBaseUri();
			}
			else if (model instanceof Import) {
				name = ((Import)model).getImportURI();
			}
			if (name != null) {
				int ls = name.lastIndexOf("/");
				if (ls > 0) {
					name = name.substring(ls + 1);
				}
			}
		}
		else {
			name = "NewName ";
		}
		Image image = getImage(model);
		ICompletionProposal proposal = createCompletionProposal(name, name + " - NAME", image, context);
		if (proposal instanceof ConfigurableCompletionProposal) {
			ConfigurableCompletionProposal configurable = (ConfigurableCompletionProposal) proposal;
			configurable.setSelectionStart(configurable.getReplacementOffset());
			configurable.setSelectionLength(name.length());
			configurable.setAutoInsertable(false);
			configurable.setSimpleLinkedMode(context.getViewer(), name.charAt(0), '\t');
		}
		acceptor.accept(proposal);
	}
	
//	public void complete_Import(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
//        List<ConceptName> names;
//		try {
//			names = visitor.getNamedConceptsOfType(ConceptType.MODELNAME, Scope.INCLUDEIMPORTS);
//	        if (names != null) {
//	        	Collection<ImportMapping> impmappings = visitor.getModelImportMappings();
//	        	Iterator<ImportMapping> itr = impmappings.iterator();
//	        	while (itr.hasNext()) {
//	        		ImportMapping im = itr.next();
//	        		if (names.contains(im.getPublicURI())) {
//	        			names.remove(im.getPublicURI());
//	        		}
//	        	}
//	        	if (!names.isEmpty()) {
//	                for (ConceptName name : names) {
//	            	    String proposalText = name.toString();
//	            	    String displayText = proposalText;
//	            		Image image = getImage(model);
//	            		ICompletionProposal proposal = createCompletionProposal(proposalText, displayText, image, context);
//	            		acceptor.accept(proposal);
//	        	    }
//	        	}
//	        }
//		} catch (MalformedURLException | InvalidNameException
//				| ConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//	}
//
	// Generates a plausible baseUri for a model name using the model file's URI, 
	// the project's name, and the preferences or project-specific properties.
	public String generateBaseUri(URI modelURI) throws MalformedURLException {
	    String prefix = "http://sadl.org/" + ResourceManager.getProjectUri(modelURI).lastSegment() + "/";

    	IPreferencesService service = Platform.getPreferencesService();
    	String value = service.getString("com.ge.research.sadl.Sadl", "baseUri", prefix, null);

        if (value != null && !value.isEmpty()) {
            prefix = value;
            if (!prefix.endsWith("/")) {
            	prefix += "/";
            }
        }
	    
	    String baseUri = prefix + modelURI.trimFileExtension().lastSegment();
	    return baseUri;
	}

	// Creates a proposal for an EOS terminal.  Xtext can't guess (at
    // the moment) what the valid values for a terminal rule are, so
    // that's why there is no automatic content assist for EOS.
	public void complete_EOS(EObject model, RuleCall ruleCall, 
	        ContentAssistContext context, ICompletionProposalAcceptor acceptor) {

		String proposalText = ".\n";
		String displayText = ". - End of Sentence";
		Image image = getImage(model);
		ICompletionProposal proposal = createCompletionProposal(proposalText, displayText, image, context);
		acceptor.accept(proposal);
	}

	// Creates proposals for a ResourceByName's name.  We will check which type of
	// model object wants the ResourceByName and propose only names having the 
	// appropriate concept type for that place in the model (e.g., only classes,
	// individuals, datatype properties, or object properties).  We'll ask Jena  
	// for the names by calling visitor.getNamedConceptsOfType(cType) and create
	// our own proposals from that list of names.
	public void completeResourceByName_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    	final boolean bRemoveUnnecessaryPrefixes = preferencesAccess.getPreferenceStore().getBoolean("prefixesOnlyAsNeeded");
		Predicate<IEObjectDescription> filter = new Predicate<IEObjectDescription>() {
			@Override
			public boolean apply(IEObjectDescription input) {
				boolean result = bRemoveUnnecessaryPrefixes ^ !(input instanceof AliasedEObjectDescription);
				return result;
			}
		};
		lookupCrossReference(model, SadlPackage.Literals.RESOURCE_BY_NAME__NAME, acceptor, filter, getProposalFactory(null, context));
	}
	
    static public void removeUnnecessaryPrefixes(List<ConceptName> names) {
    	List<String> ambiguousNames = null;
    	for (int i = 0; i < names.size(); i++) {
    		ConceptName cn1 = names.get(i);
    		// if they don't both have a prefix we can't fix it anyway; if this is already known to be ambiguous skip
    		if (cn1.hasPrefix() && (ambiguousNames == null || !ambiguousNames.contains(cn1.getName()))) {
    			for (int j = i + 1; j < names.size(); j++) {
    				ConceptName cn2 = names.get(j);
    				if (cn2.hasPrefix()) {
    					if (cn1.getName().equals(cn2.getName())) {
    						if (ambiguousNames == null) {
    							ambiguousNames = new ArrayList<String>();
    						}
    						ambiguousNames.add(cn1.getName());
    						break;
    					}
    				}
    			}
    		}
    	}
    	if (ambiguousNames != null) {
    		for (ConceptName name : names) {
    			if (!ambiguousNames.contains(name.getName())) {
    				name.setPrefix(null);
    				name.setNamespace(null);
    			}
    		}
    	}
    	else {
    		// there are no ambiguous names--remove all prefixes
    		for (ConceptName name : names) {
   				name.setPrefix(null);
   				name.setNamespace(null);
    		}
    	}
    }


	private ICompletionProposal createCompletionProposal(String proposal, String label, Image image, 
			String prefix, ContentAssistContext context) {
		return createCompletionProposal(
			proposal, new StyledString(label), image, getPriorityHelper().getDefaultPriority(), prefix, context);
	}
	
	@Override
	public void completeTypedBNode_Article(EObject model,
			Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		// do nothing
	}

	@Override
	public void completeKeyword(Keyword keyword, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		String proposalText = keyword.getValue();
		if (isInvokedDirectlyAfterKeyword(context) && requireSpaceBefore(keyword, context) && !hasSpaceBefore(context)) {
			proposalText = " " + proposalText;
		}
		if (requireSpaceAfter(keyword, context) && !hasSpaceAfter(context)) {
			proposalText = proposalText + " ";
		}
	
		ICompletionProposal proposal = createCompletionProposal(proposalText, getKeywordDisplayString(keyword),
				getImage(keyword), context);
		getPriorityHelper().adjustKeywordPriority(proposal, context.getPrefix());
		acceptor.accept(proposal);
	}

	private boolean isInvokedDirectlyAfterKeyword (ContentAssistContext context) {
		return context.getLastCompleteNode().getTotalEndOffset()==context.getOffset();
	}
	
	private boolean requireSpaceBefore (Keyword keyword, ContentAssistContext context) {
		if (!keywordsWithSpaceBefore.contains(keyword)) {
			return false;
		}
		return true;
	}
	
	private boolean hasSpaceBefore (ContentAssistContext context) {
		//TODO: Detect space before invocation offset
		return false;
	}
	
	private boolean requireSpaceAfter (Keyword keyword, ContentAssistContext context) {
		if (!keywordsWithSpaceAfter.contains(keyword)) {
			return false;
		}
		return true;
	}
	
	private boolean hasSpaceAfter (ContentAssistContext context) {
		if (!context.getCurrentNode().hasNextSibling()) return false; // EOF
		//TODO: Detect space after invocation offset
		return false;
	}

	@Override
	protected String getDisplayString(EObject element,
			String qualifiedNameAsString, String shortName) {
		if (element instanceof ResourceName) {
			final boolean bRemoveUnnecessaryPrefixes = preferencesAccess.getPreferenceStore().getBoolean("prefixesOnlyAsNeeded");
			if (!bRemoveUnnecessaryPrefixes) {
				return qualifiedNameAsString;
			}
		}
		return super.getDisplayString(element, qualifiedNameAsString, shortName);
	}	

	@Override
	protected Function<IEObjectDescription, ICompletionProposal> getProposalFactory(
			String ruleName, ContentAssistContext contentAssistContext) {
		return new DefaultProposalCreator(contentAssistContext, ruleName, getQualifiedNameConverter()) {
			
		};
	}
}
