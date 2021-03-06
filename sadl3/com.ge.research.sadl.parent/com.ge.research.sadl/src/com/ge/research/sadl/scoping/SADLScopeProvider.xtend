/*
 * generated by Xtext 2.9.0-SNAPSHOT
 */
package com.ge.research.sadl.scoping

import com.ge.research.sadl.model.DeclarationExtensions
import com.ge.research.sadl.sADL.SadlImport
import com.ge.research.sadl.sADL.SadlResource
import com.google.inject.Inject
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.resource.EObjectDescription
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.impl.AbstractScopeProvider
import org.eclipse.xtext.scoping.impl.MapBasedScope

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
class SADLScopeProvider extends AbstractScopeProvider {
	
	@Inject extension DeclarationExtensions
	@Inject IQualifiedNameProvider qnProvider 
	
	override getScope(EObject context, EReference reference) {
		val result = newHashMap
		val iter = context.eResource.allContents
		while (iter.hasNext) {
			switch it : iter.next {
				SadlResource case concreteName!==null : {
					val simpleName =  QualifiedName.create(concreteName)
					result.addElement(simpleName, it)
					val qn = qnProvider.getFullyQualifiedName(it)
					result.addElement(qn, it)
				}
				SadlImport : {
					//TODO
				}
			} 
		}
		return MapBasedScope.createScope(IScope.NULLSCOPE, result.values)
	}
	
	private def void addElement(Map<QualifiedName, IEObjectDescription> scope, QualifiedName qn, EObject obj) {
		if (!scope.containsKey(qn)) {
			scope.put(qn, new EObjectDescription(qn, obj, emptyMap))
		}
	}

}
