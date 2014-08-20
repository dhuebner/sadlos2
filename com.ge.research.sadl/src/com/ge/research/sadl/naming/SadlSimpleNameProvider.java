package com.ge.research.sadl.naming;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.util.SimpleAttributeResolver;

import com.ge.research.sadl.sadl.Import;
import com.google.inject.Inject;

public class SadlSimpleNameProvider extends IQualifiedNameProvider.AbstractImpl {
	public final static String MATCH_TOKEN1 = "import_"; 
	public final static String MATCH_TOKEN2 = "__alias";
	public final static String DOT_REPLACE_TOKEN = ";;;dot;;;"; 
	
	@Inject
	private IQualifiedNameConverter qualifiedNameConverter;

	public QualifiedName getFullyQualifiedName(EObject obj) {
		String name = null;
		if (obj instanceof Import) {
			Import imp = (Import) obj;
			name = MATCH_TOKEN1 + replace(imp.getImportURI()) + MATCH_TOKEN2 + replace(imp.getAlias());
		} else {
			name = SimpleAttributeResolver.NAME_RESOLVER.apply(obj);
		}
		if (name == null)
			return null;
		return qualifiedNameConverter.toQualifiedName(name);
	}

	private String replace(String string) {
		if(string != null) {
			string = string.replaceAll("\\.", DOT_REPLACE_TOKEN);
		}
		return string;
	}

}