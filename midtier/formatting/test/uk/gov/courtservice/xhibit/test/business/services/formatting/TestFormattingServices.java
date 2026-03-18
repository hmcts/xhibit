package uk.gov.courtservice.xhibit.test.business.services.formatting;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import junit.framework.TestCase;

import uk.gov.courtservice.xhibit.business.services.formatting.FormattingController;

public class TestFormattingServices extends TestCase  {

	/**
	 * Constant definitions.
	 */
	private static final String PROVIDER_URL="t3://localhost:7003";
	private static final String INITIAL_CONTEXT_FACTORY="weblogic.jndi.WLInitialContextFactory";

	
	public void test1() throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, PROVIDER_URL);
		InitialContext ctx = new InitialContext(env);
			
		FormattingController del = (FormattingController) ctx.lookup("MidTierFormattingController_jarFormattingController_EO");
		
		//If there's anything to pick up it will pick it up and if it throws any kind of exception then it will fail this test, this way
		//we know when doing a build if anything falls over because it is wrong.
			try {
				del.processFormattingDocument();
			} catch (Exception e) {
				fail();
			}	
	}
	
}
