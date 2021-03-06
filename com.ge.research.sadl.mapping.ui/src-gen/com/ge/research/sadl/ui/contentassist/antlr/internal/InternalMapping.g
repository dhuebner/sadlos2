/*
* generated by Xtext
*/
grammar InternalMapping;

options {
	superClass=AbstractInternalContentAssistParser;
	
}

@lexer::header {
package com.ge.research.sadl.ui.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer;
}

@parser::header {
package com.ge.research.sadl.ui.contentassist.antlr.internal; 

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.DFA;
import com.ge.research.sadl.services.MappingGrammarAccess;

}

@parser::members {
 
 	private MappingGrammarAccess grammarAccess;
 	
    public void setGrammarAccess(MappingGrammarAccess grammarAccess) {
    	this.grammarAccess = grammarAccess;
    }
    
    @Override
    protected Grammar getGrammar() {
    	return grammarAccess.getGrammar();
    }
    
    @Override
    protected String getValueForTokenName(String tokenName) {
    	return tokenName;
    }

}




// Entry rule entryRuleModel
entryRuleModel 
:
{ before(grammarAccess.getModelRule()); }
	 ruleModel
{ after(grammarAccess.getModelRule()); } 
	 EOF 
;

// Rule Model
ruleModel
    @init {
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getModelAccess().getGroup()); }
(rule__Model__Group__0)
{ after(grammarAccess.getModelAccess().getGroup()); }
)

;
finally {
	restoreStackSize(stackSize);
}



// Entry rule entryRuleNewModelNS
entryRuleNewModelNS 
:
{ before(grammarAccess.getNewModelNSRule()); }
	 ruleNewModelNS
{ after(grammarAccess.getNewModelNSRule()); } 
	 EOF 
;

// Rule NewModelNS
ruleNewModelNS
    @init {
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getNewModelNSAccess().getGroup()); }
(rule__NewModelNS__Group__0)
{ after(grammarAccess.getNewModelNSAccess().getGroup()); }
)

;
finally {
	restoreStackSize(stackSize);
}



// Entry rule entryRuleImport
entryRuleImport 
:
{ before(grammarAccess.getImportRule()); }
	 ruleImport
{ after(grammarAccess.getImportRule()); } 
	 EOF 
;

// Rule Import
ruleImport
    @init {
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getImportAccess().getGroup()); }
(rule__Import__Group__0)
{ after(grammarAccess.getImportAccess().getGroup()); }
)

;
finally {
	restoreStackSize(stackSize);
}



// Entry rule entryRuleGroup
entryRuleGroup 
:
{ before(grammarAccess.getGroupRule()); }
	 ruleGroup
{ after(grammarAccess.getGroupRule()); } 
	 EOF 
;

// Rule Group
ruleGroup
    @init {
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getGroupAccess().getGroup()); }
(rule__Group__Group__0)
{ after(grammarAccess.getGroupAccess().getGroup()); }
)

;
finally {
	restoreStackSize(stackSize);
}



// Entry rule entryRuleTriple
entryRuleTriple 
:
{ before(grammarAccess.getTripleRule()); }
	 ruleTriple
{ after(grammarAccess.getTripleRule()); } 
	 EOF 
;

// Rule Triple
ruleTriple
    @init {
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getTripleAccess().getGroup()); }
(rule__Triple__Group__0)
{ after(grammarAccess.getTripleAccess().getGroup()); }
)

;
finally {
	restoreStackSize(stackSize);
}



// Entry rule entryRuleLiteralValue
entryRuleLiteralValue 
:
{ before(grammarAccess.getLiteralValueRule()); }
	 ruleLiteralValue
{ after(grammarAccess.getLiteralValueRule()); } 
	 EOF 
;

// Rule LiteralValue
ruleLiteralValue
    @init {
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getLiteralValueAccess().getAlternatives()); }
(rule__LiteralValue__Alternatives)
{ after(grammarAccess.getLiteralValueAccess().getAlternatives()); }
)

;
finally {
	restoreStackSize(stackSize);
}



// Entry rule entryRuleNUMBER
entryRuleNUMBER 
@init {
	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens();
}
:
{ before(grammarAccess.getNUMBERRule()); }
	 ruleNUMBER
{ after(grammarAccess.getNUMBERRule()); } 
	 EOF 
;
finally {
	myHiddenTokenState.restore();
}

// Rule NUMBER
ruleNUMBER
    @init {
		HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens();
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getNUMBERAccess().getGroup()); }
(rule__NUMBER__Group__0)
{ after(grammarAccess.getNUMBERAccess().getGroup()); }
)

;
finally {
	restoreStackSize(stackSize);
	myHiddenTokenState.restore();
}



// Entry rule entryRuleRef
entryRuleRef 
:
{ before(grammarAccess.getRefRule()); }
	 ruleRef
{ after(grammarAccess.getRefRule()); } 
	 EOF 
;

// Rule Ref
ruleRef
    @init {
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getRefAccess().getGroup()); }
(rule__Ref__Group__0)
{ after(grammarAccess.getRefAccess().getGroup()); }
)

;
finally {
	restoreStackSize(stackSize);
}



// Entry rule entryRuleColumnName
entryRuleColumnName 
:
{ before(grammarAccess.getColumnNameRule()); }
	 ruleColumnName
{ after(grammarAccess.getColumnNameRule()); } 
	 EOF 
;

// Rule ColumnName
ruleColumnName
    @init {
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getColumnNameAccess().getGroup()); }
(rule__ColumnName__Group__0)
{ after(grammarAccess.getColumnNameAccess().getGroup()); }
)

;
finally {
	restoreStackSize(stackSize);
}



// Entry rule entryRuleColumnID
entryRuleColumnID 
:
{ before(grammarAccess.getColumnIDRule()); }
	 ruleColumnID
{ after(grammarAccess.getColumnIDRule()); } 
	 EOF 
;

// Rule ColumnID
ruleColumnID
    @init {
		int stackSize = keepStackSize();
    }
	:
(
{ before(grammarAccess.getColumnIDAccess().getAlternatives()); }
(rule__ColumnID__Alternatives)
{ after(grammarAccess.getColumnIDAccess().getAlternatives()); }
)

;
finally {
	restoreStackSize(stackSize);
}




rule__Model__TriplesAlternatives_2_0
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getModelAccess().getTriplesTripleParserRuleCall_2_0_0()); }
	ruleTriple
{ after(grammarAccess.getModelAccess().getTriplesTripleParserRuleCall_2_0_0()); }
)

    |(
{ before(grammarAccess.getModelAccess().getTriplesGroupParserRuleCall_2_0_1()); }
	ruleGroup
{ after(grammarAccess.getModelAccess().getTriplesGroupParserRuleCall_2_0_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Group__GroupLinesAlternatives_1_0
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getGroupAccess().getGroupLinesTripleParserRuleCall_1_0_0()); }
	ruleTriple
{ after(grammarAccess.getGroupAccess().getGroupLinesTripleParserRuleCall_1_0_0()); }
)

    |(
{ before(grammarAccess.getGroupAccess().getGroupLinesGroupParserRuleCall_1_0_1()); }
	ruleGroup
{ after(grammarAccess.getGroupAccess().getGroupLinesGroupParserRuleCall_1_0_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Triple__SubjAlternatives_0_0
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getTripleAccess().getSubjRefParserRuleCall_0_0_0()); }
	ruleRef
{ after(grammarAccess.getTripleAccess().getSubjRefParserRuleCall_0_0_0()); }
)

    |(
{ before(grammarAccess.getTripleAccess().getSubjResourceNameCrossReference_0_0_1()); }
(

)
{ after(grammarAccess.getTripleAccess().getSubjResourceNameCrossReference_0_0_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Triple__ObjvalAlternatives_3_0
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getTripleAccess().getObjvalRefParserRuleCall_3_0_0()); }
	ruleRef
{ after(grammarAccess.getTripleAccess().getObjvalRefParserRuleCall_3_0_0()); }
)

    |(
{ before(grammarAccess.getTripleAccess().getObjvalResourceNameCrossReference_3_0_1()); }
(

)
{ after(grammarAccess.getTripleAccess().getObjvalResourceNameCrossReference_3_0_1()); }
)

    |(
{ before(grammarAccess.getTripleAccess().getObjvalLiteralValueParserRuleCall_3_0_2()); }
	ruleLiteralValue
{ after(grammarAccess.getTripleAccess().getObjvalLiteralValueParserRuleCall_3_0_2()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__LiteralValue__Alternatives
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getLiteralValueAccess().getLiteralNumberAssignment_0()); }
(rule__LiteralValue__LiteralNumberAssignment_0)
{ after(grammarAccess.getLiteralValueAccess().getLiteralNumberAssignment_0()); }
)

    |(
{ before(grammarAccess.getLiteralValueAccess().getLiteralStringAssignment_1()); }
(rule__LiteralValue__LiteralStringAssignment_1)
{ after(grammarAccess.getLiteralValueAccess().getLiteralStringAssignment_1()); }
)

    |(
{ before(grammarAccess.getLiteralValueAccess().getLiteralBooleanAssignment_2()); }
(rule__LiteralValue__LiteralBooleanAssignment_2)
{ after(grammarAccess.getLiteralValueAccess().getLiteralBooleanAssignment_2()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__LiteralValue__LiteralBooleanAlternatives_2_0
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getLiteralValueAccess().getLiteralBooleanTrueKeyword_2_0_0()); }

	'true' 

{ after(grammarAccess.getLiteralValueAccess().getLiteralBooleanTrueKeyword_2_0_0()); }
)

    |(
{ before(grammarAccess.getLiteralValueAccess().getLiteralBooleanFalseKeyword_2_0_1()); }

	'false' 

{ after(grammarAccess.getLiteralValueAccess().getLiteralBooleanFalseKeyword_2_0_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__ColumnID__Alternatives
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getColumnIDAccess().getIDTerminalRuleCall_0()); }
	RULE_ID
{ after(grammarAccess.getColumnIDAccess().getIDTerminalRuleCall_0()); }
)

    |(
{ before(grammarAccess.getColumnIDAccess().getDIGITSTerminalRuleCall_1()); }
	RULE_DIGITS
{ after(grammarAccess.getColumnIDAccess().getDIGITSTerminalRuleCall_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}



rule__Model__Group__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Model__Group__0__Impl
	rule__Model__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Model__Group__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getModelAccess().getUriAssignment_0()); }
(rule__Model__UriAssignment_0)
{ after(grammarAccess.getModelAccess().getUriAssignment_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Model__Group__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Model__Group__1__Impl
	rule__Model__Group__2
;
finally {
	restoreStackSize(stackSize);
}

rule__Model__Group__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getModelAccess().getImportsAssignment_1()); }
(rule__Model__ImportsAssignment_1)*
{ after(grammarAccess.getModelAccess().getImportsAssignment_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Model__Group__2
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Model__Group__2__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Model__Group__2__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getModelAccess().getTriplesAssignment_2()); }
(rule__Model__TriplesAssignment_2)*
{ after(grammarAccess.getModelAccess().getTriplesAssignment_2()); }
)

;
finally {
	restoreStackSize(stackSize);
}








rule__NewModelNS__Group__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__NewModelNS__Group__0__Impl
	rule__NewModelNS__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__NewModelNS__Group__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNewModelNSAccess().getUriKeyword_0()); }

	'uri' 

{ after(grammarAccess.getNewModelNSAccess().getUriKeyword_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__NewModelNS__Group__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__NewModelNS__Group__1__Impl
	rule__NewModelNS__Group__2
;
finally {
	restoreStackSize(stackSize);
}

rule__NewModelNS__Group__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNewModelNSAccess().getBaseUriAssignment_1()); }
(rule__NewModelNS__BaseUriAssignment_1)
{ after(grammarAccess.getNewModelNSAccess().getBaseUriAssignment_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__NewModelNS__Group__2
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__NewModelNS__Group__2__Impl
	rule__NewModelNS__Group__3
;
finally {
	restoreStackSize(stackSize);
}

rule__NewModelNS__Group__2__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNewModelNSAccess().getGroup_2()); }
(rule__NewModelNS__Group_2__0)?
{ after(grammarAccess.getNewModelNSAccess().getGroup_2()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__NewModelNS__Group__3
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__NewModelNS__Group__3__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__NewModelNS__Group__3__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNewModelNSAccess().getEOSTerminalRuleCall_3()); }
	RULE_EOS
{ after(grammarAccess.getNewModelNSAccess().getEOSTerminalRuleCall_3()); }
)

;
finally {
	restoreStackSize(stackSize);
}










rule__NewModelNS__Group_2__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__NewModelNS__Group_2__0__Impl
	rule__NewModelNS__Group_2__1
;
finally {
	restoreStackSize(stackSize);
}

rule__NewModelNS__Group_2__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNewModelNSAccess().getAliasKeyword_2_0()); }

	'alias' 

{ after(grammarAccess.getNewModelNSAccess().getAliasKeyword_2_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__NewModelNS__Group_2__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__NewModelNS__Group_2__1__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__NewModelNS__Group_2__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNewModelNSAccess().getPrefixAssignment_2_1()); }
(rule__NewModelNS__PrefixAssignment_2_1)
{ after(grammarAccess.getNewModelNSAccess().getPrefixAssignment_2_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}






rule__Import__Group__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Import__Group__0__Impl
	rule__Import__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Import__Group__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getImportAccess().getImportKeyword_0()); }

	'import' 

{ after(grammarAccess.getImportAccess().getImportKeyword_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Import__Group__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Import__Group__1__Impl
	rule__Import__Group__2
;
finally {
	restoreStackSize(stackSize);
}

rule__Import__Group__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getImportAccess().getImportURIAssignment_1()); }
(rule__Import__ImportURIAssignment_1)
{ after(grammarAccess.getImportAccess().getImportURIAssignment_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Import__Group__2
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Import__Group__2__Impl
	rule__Import__Group__3
;
finally {
	restoreStackSize(stackSize);
}

rule__Import__Group__2__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getImportAccess().getGroup_2()); }
(rule__Import__Group_2__0)?
{ after(grammarAccess.getImportAccess().getGroup_2()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Import__Group__3
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Import__Group__3__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Import__Group__3__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getImportAccess().getEOSTerminalRuleCall_3()); }
	RULE_EOS
{ after(grammarAccess.getImportAccess().getEOSTerminalRuleCall_3()); }
)

;
finally {
	restoreStackSize(stackSize);
}










rule__Import__Group_2__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Import__Group_2__0__Impl
	rule__Import__Group_2__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Import__Group_2__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getImportAccess().getAsKeyword_2_0()); }

	'as' 

{ after(grammarAccess.getImportAccess().getAsKeyword_2_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Import__Group_2__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Import__Group_2__1__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Import__Group_2__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getImportAccess().getAliasAssignment_2_1()); }
(rule__Import__AliasAssignment_2_1)
{ after(grammarAccess.getImportAccess().getAliasAssignment_2_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}






rule__Group__Group__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Group__Group__0__Impl
	rule__Group__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Group__Group__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getGroupAccess().getLeftCurlyBracketKeyword_0()); }

	'{' 

{ after(grammarAccess.getGroupAccess().getLeftCurlyBracketKeyword_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Group__Group__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Group__Group__1__Impl
	rule__Group__Group__2
;
finally {
	restoreStackSize(stackSize);
}

rule__Group__Group__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
(
{ before(grammarAccess.getGroupAccess().getGroupLinesAssignment_1()); }
(rule__Group__GroupLinesAssignment_1)
{ after(grammarAccess.getGroupAccess().getGroupLinesAssignment_1()); }
)
(
{ before(grammarAccess.getGroupAccess().getGroupLinesAssignment_1()); }
(rule__Group__GroupLinesAssignment_1)*
{ after(grammarAccess.getGroupAccess().getGroupLinesAssignment_1()); }
)
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Group__Group__2
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Group__Group__2__Impl
	rule__Group__Group__3
;
finally {
	restoreStackSize(stackSize);
}

rule__Group__Group__2__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getGroupAccess().getRightCurlyBracketKeyword_2()); }

	'}' 

{ after(grammarAccess.getGroupAccess().getRightCurlyBracketKeyword_2()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Group__Group__3
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Group__Group__3__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Group__Group__3__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getGroupAccess().getEOSTerminalRuleCall_3()); }
	RULE_EOS
{ after(grammarAccess.getGroupAccess().getEOSTerminalRuleCall_3()); }
)

;
finally {
	restoreStackSize(stackSize);
}










rule__Triple__Group__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Triple__Group__0__Impl
	rule__Triple__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Triple__Group__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getTripleAccess().getSubjAssignment_0()); }
(rule__Triple__SubjAssignment_0)
{ after(grammarAccess.getTripleAccess().getSubjAssignment_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Triple__Group__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Triple__Group__1__Impl
	rule__Triple__Group__2
;
finally {
	restoreStackSize(stackSize);
}

rule__Triple__Group__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getTripleAccess().getHasKeyword_1()); }
(
	'has' 
)?
{ after(grammarAccess.getTripleAccess().getHasKeyword_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Triple__Group__2
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Triple__Group__2__Impl
	rule__Triple__Group__3
;
finally {
	restoreStackSize(stackSize);
}

rule__Triple__Group__2__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getTripleAccess().getPredAssignment_2()); }
(rule__Triple__PredAssignment_2)
{ after(grammarAccess.getTripleAccess().getPredAssignment_2()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Triple__Group__3
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Triple__Group__3__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Triple__Group__3__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getTripleAccess().getObjvalAssignment_3()); }
(rule__Triple__ObjvalAssignment_3)
{ after(grammarAccess.getTripleAccess().getObjvalAssignment_3()); }
)

;
finally {
	restoreStackSize(stackSize);
}










rule__NUMBER__Group__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__NUMBER__Group__0__Impl
	rule__NUMBER__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__NUMBER__Group__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNUMBERAccess().getHyphenMinusKeyword_0()); }
(
	'-' 
)?
{ after(grammarAccess.getNUMBERAccess().getHyphenMinusKeyword_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__NUMBER__Group__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__NUMBER__Group__1__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__NUMBER__Group__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNUMBERAccess().getUNSIGNED_NUMBERTerminalRuleCall_1()); }
	RULE_UNSIGNED_NUMBER
{ after(grammarAccess.getNUMBERAccess().getUNSIGNED_NUMBERTerminalRuleCall_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}






rule__Ref__Group__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Ref__Group__0__Impl
	rule__Ref__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Ref__Group__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getRefAccess().getRefAssignment_0()); }
(rule__Ref__RefAssignment_0)
{ after(grammarAccess.getRefAccess().getRefAssignment_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Ref__Group__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Ref__Group__1__Impl
	rule__Ref__Group__2
;
finally {
	restoreStackSize(stackSize);
}

rule__Ref__Group__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getRefAccess().getAddlcolsAssignment_1()); }
(rule__Ref__AddlcolsAssignment_1)?
{ after(grammarAccess.getRefAccess().getAddlcolsAssignment_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Ref__Group__2
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Ref__Group__2__Impl
	rule__Ref__Group__3
;
finally {
	restoreStackSize(stackSize);
}

rule__Ref__Group__2__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getRefAccess().getColumnNameParserRuleCall_2()); }
(	ruleColumnName)*
{ after(grammarAccess.getRefAccess().getColumnNameParserRuleCall_2()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__Ref__Group__3
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__Ref__Group__3__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Ref__Group__3__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getRefAccess().getRowAssignment_3()); }
(rule__Ref__RowAssignment_3)?
{ after(grammarAccess.getRefAccess().getRowAssignment_3()); }
)

;
finally {
	restoreStackSize(stackSize);
}










rule__ColumnName__Group__0
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__ColumnName__Group__0__Impl
	rule__ColumnName__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__ColumnName__Group__0__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getColumnNameAccess().getLessThanSignKeyword_0()); }

	'<' 

{ after(grammarAccess.getColumnNameAccess().getLessThanSignKeyword_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__ColumnName__Group__1
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__ColumnName__Group__1__Impl
	rule__ColumnName__Group__2
;
finally {
	restoreStackSize(stackSize);
}

rule__ColumnName__Group__1__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getColumnNameAccess().getColumnIDParserRuleCall_1()); }
	ruleColumnID
{ after(grammarAccess.getColumnNameAccess().getColumnIDParserRuleCall_1()); }
)

;
finally {
	restoreStackSize(stackSize);
}


rule__ColumnName__Group__2
    @init {
		int stackSize = keepStackSize();
    }
:
	rule__ColumnName__Group__2__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__ColumnName__Group__2__Impl
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getColumnNameAccess().getGreaterThanSignKeyword_2()); }

	'>' 

{ after(grammarAccess.getColumnNameAccess().getGreaterThanSignKeyword_2()); }
)

;
finally {
	restoreStackSize(stackSize);
}









rule__Model__UriAssignment_0
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getModelAccess().getUriNewModelNSParserRuleCall_0_0()); }
	ruleNewModelNS{ after(grammarAccess.getModelAccess().getUriNewModelNSParserRuleCall_0_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Model__ImportsAssignment_1
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getModelAccess().getImportsImportParserRuleCall_1_0()); }
	ruleImport{ after(grammarAccess.getModelAccess().getImportsImportParserRuleCall_1_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Model__TriplesAssignment_2
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getModelAccess().getTriplesAlternatives_2_0()); }
(rule__Model__TriplesAlternatives_2_0)
{ after(grammarAccess.getModelAccess().getTriplesAlternatives_2_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__NewModelNS__BaseUriAssignment_1
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNewModelNSAccess().getBaseUriSTRINGTerminalRuleCall_1_0()); }
	RULE_STRING{ after(grammarAccess.getNewModelNSAccess().getBaseUriSTRINGTerminalRuleCall_1_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__NewModelNS__PrefixAssignment_2_1
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getNewModelNSAccess().getPrefixIDTerminalRuleCall_2_1_0()); }
	RULE_ID{ after(grammarAccess.getNewModelNSAccess().getPrefixIDTerminalRuleCall_2_1_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Import__ImportURIAssignment_1
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getImportAccess().getImportURISTRINGTerminalRuleCall_1_0()); }
	RULE_STRING{ after(grammarAccess.getImportAccess().getImportURISTRINGTerminalRuleCall_1_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Import__AliasAssignment_2_1
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getImportAccess().getAliasIDTerminalRuleCall_2_1_0()); }
	RULE_ID{ after(grammarAccess.getImportAccess().getAliasIDTerminalRuleCall_2_1_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Group__GroupLinesAssignment_1
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getGroupAccess().getGroupLinesAlternatives_1_0()); }
(rule__Group__GroupLinesAlternatives_1_0)
{ after(grammarAccess.getGroupAccess().getGroupLinesAlternatives_1_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Triple__SubjAssignment_0
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getTripleAccess().getSubjAlternatives_0_0()); }
(rule__Triple__SubjAlternatives_0_0)
{ after(grammarAccess.getTripleAccess().getSubjAlternatives_0_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Triple__PredAssignment_2
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getTripleAccess().getPredResourceNameCrossReference_2_0()); }
(
{ before(grammarAccess.getTripleAccess().getPredResourceNameIDTerminalRuleCall_2_0_1()); }
	RULE_ID{ after(grammarAccess.getTripleAccess().getPredResourceNameIDTerminalRuleCall_2_0_1()); }
)
{ after(grammarAccess.getTripleAccess().getPredResourceNameCrossReference_2_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Triple__ObjvalAssignment_3
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getTripleAccess().getObjvalAlternatives_3_0()); }
(rule__Triple__ObjvalAlternatives_3_0)
{ after(grammarAccess.getTripleAccess().getObjvalAlternatives_3_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__LiteralValue__LiteralNumberAssignment_0
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getLiteralValueAccess().getLiteralNumberNUMBERParserRuleCall_0_0()); }
	ruleNUMBER{ after(grammarAccess.getLiteralValueAccess().getLiteralNumberNUMBERParserRuleCall_0_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__LiteralValue__LiteralStringAssignment_1
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getLiteralValueAccess().getLiteralStringSTRINGTerminalRuleCall_1_0()); }
	RULE_STRING{ after(grammarAccess.getLiteralValueAccess().getLiteralStringSTRINGTerminalRuleCall_1_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__LiteralValue__LiteralBooleanAssignment_2
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getLiteralValueAccess().getLiteralBooleanAlternatives_2_0()); }
(rule__LiteralValue__LiteralBooleanAlternatives_2_0)
{ after(grammarAccess.getLiteralValueAccess().getLiteralBooleanAlternatives_2_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Ref__RefAssignment_0
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getRefAccess().getRefColumnNameParserRuleCall_0_0()); }
	ruleColumnName{ after(grammarAccess.getRefAccess().getRefColumnNameParserRuleCall_0_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Ref__AddlcolsAssignment_1
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getRefAccess().getAddlcols_Keyword_1_0()); }
(
{ before(grammarAccess.getRefAccess().getAddlcols_Keyword_1_0()); }

	'_' 

{ after(grammarAccess.getRefAccess().getAddlcols_Keyword_1_0()); }
)

{ after(grammarAccess.getRefAccess().getAddlcols_Keyword_1_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}

rule__Ref__RowAssignment_3
    @init {
		int stackSize = keepStackSize();
    }
:
(
{ before(grammarAccess.getRefAccess().getRowLeftParenthesisRightParenthesisKeyword_3_0()); }
(
{ before(grammarAccess.getRefAccess().getRowLeftParenthesisRightParenthesisKeyword_3_0()); }

	'()' 

{ after(grammarAccess.getRefAccess().getRowLeftParenthesisRightParenthesisKeyword_3_0()); }
)

{ after(grammarAccess.getRefAccess().getRowLeftParenthesisRightParenthesisKeyword_3_0()); }
)

;
finally {
	restoreStackSize(stackSize);
}


RULE_DIGITS : ('0'..'9')+;

RULE_UNSIGNED_NUMBER : (RULE_DIGITS|RULE_DIGITS '.' ('0'..'9')* (('e'|'E') ('+'|'-')? RULE_DIGITS)?|'.' RULE_DIGITS (('e'|'E') ('+'|'-')? RULE_DIGITS)?|RULE_DIGITS ('e'|'E') ('+'|'-')? RULE_DIGITS);

RULE_EOS : '.' (' '|'\t'|'\r'|'\n'|EOF);

RULE_WS : ('\u00A0'|' '|'\t'|'\r'|'\n')+;

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'-'|'%'|'~')*;

RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_ANY_OTHER : .;


