/*
 * generated by Xtext 2.9.0-SNAPSHOT
 */
package com.ge.research.sadl


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class SADLStandaloneSetup extends SADLStandaloneSetupGenerated {

	def static void doSetup() {
		new SADLStandaloneSetup().createInjectorAndDoEMFRegistration()
	}

}