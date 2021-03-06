/*
 * generated by Xtext 2.9.0-SNAPSHOT
 */
package com.ge.research.sadl.tests

import com.ge.research.sadl.sADL.SadlModel
import com.google.inject.Inject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.runner.RunWith

@RunWith(XtextRunner)
@InjectWith(SADLInjectorProvider)
abstract class SADLParsingTest{

	@Inject ParseHelper<SadlModel> parseHelper
	@Inject ValidationTestHelper validationTestHelper
	
	protected def void assertNoErrors(CharSequence text) {
		val model = parseHelper.parse(text)
		val issues = validationTestHelper.validate(model)
		if (issues.isEmpty)
			return;
		var String annotatedText = text.toString
		for (issue : issues.filter[isSyntaxError].sortBy[-offset]) {
			annotatedText = annotatedText.substring(0, issue.offset) + '''[«issue.message»]''' + annotatedText.substring(issue.offset)
		}
		Assert.assertEquals(text.toString, annotatedText)
	}
	
	def void assertAST(CharSequence text, (SadlModel)=>void assertion) {
		val model = parseHelper.parse(text)
		validationTestHelper.assertNoErrors(model)
		assertion.apply(model)
	}
	
	def String prependUri(CharSequence sequence) {
		return '''
			«model»
			«sequence»
		'''
	}
	
	protected def String model() {
		val name = Thread.currentThread.stackTrace.findFirst[className!=SADLParsingTest.simpleName].methodName
		return '''uri "http://sadl.org/TestRequrements/«name»" alias «name».'''
	}

}
