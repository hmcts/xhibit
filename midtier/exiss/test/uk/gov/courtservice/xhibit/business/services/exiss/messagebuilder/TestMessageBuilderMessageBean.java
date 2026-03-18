/**
 * <p>
 * Title: Test the message bean utilities
 * </p>
 * <p>
 * Description: A Unit Test
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jeremy Shields
 * @version $Id: TestMessageBuilderMessageBean.java,v 1.5 2006/07/14 10:10:49 bzjrnl Exp $
 */

package uk.gov.courtservice.xhibit.business.services.exiss.messagebuilder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class TestMessageBuilderMessageBean extends TestCase {

	/**
	 * Execution entry point. Allows the test to be run in stand alone mode.
	 * 
	 * @param args
	 *            String array of command line arguments
	 */
	public static void main(String args[]) {
		TestRunner.run(suite());
	}

	/**
	 * Create a Test useing reflection to determine tests
	 */
	public static Test suite() {
		return new TestSuite(TestMessageBuilderMessageBean.class);
	}

	/**
	 * TestCase implementation.
	 * 
	 * @see TestCase#setUp() TestCase
	 */
	public void setUp() {
	}

	/**
	 * TestCase implementation.
	 * 
	 * @see TestCase#tearDown() TestCase
	 */
	public void tearDown() {
	}

	//
	// Test ...
	//	
    
    public void testBogusTestCase() {
        assertTrue(true);
    }

}