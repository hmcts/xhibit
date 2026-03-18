package uk.gov.courtservice.xhibit.business.services.courtel.test;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.Ignore;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.services.courtel.controller.CourtelListController;

public class TestCourtelProcessing extends TestCase  {

	private String PROVIDER_URL="t3://localhost:7003";
	private String INITIAL_CONTEXT_FACTORY="weblogic.jndi.WLInitialContextFactory";

	@Ignore
	public void test1() throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, PROVIDER_URL);
		InitialContext ctx = new InitialContext(env);
			
		CourtelListController del = (CourtelListController) ctx.lookup("MidTierListDistribution2Controller_jarCourtelListController_EO");
		
		//If there's anything to pick up it will pick it up and if it throws any kind of exception then it will fail this test, this way
		//we know when doing a build if anything falls over because it is wrong.
			try {
				del.doTask("tester");
			} catch (Exception e) {
				System.out.println("E is "+e);
				fail();
			}	
	}
	
}
