package uk.gov.courtservice.ant.types.mappers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.filters.TokenFilter.ReplaceString;
import org.apache.tools.ant.types.FilterChain;

/**
 * <p>
 * Title: Translation Bundle Test
 * </p>
 * <p>
 * Description: A translation
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: TestReplaceMapper.java,v 1.1 2006/06/28 11:05:11 bzjrnl Exp $
 */
public class TestReplaceMapper extends TestCase {

	//
	// The Framework!
	//

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
		return new TestSuite(TestReplaceMapper.class);
	}

	private FilterChain filterChain;
	private RefFilterMapper mapper;

	/**
	 * TestCase implementation.
	 * 
	 * @see TestCase#setUp() TestCase
	 */
	public void setUp() {
		Project project = new Project();
		mapper = new RefFilterMapper();
		mapper.setProject(project);
		filterChain = new FilterChain();
		filterChain.setProject(project);
		project.addReference("test.filterchain", filterChain);
		mapper.setFrom("test.filterchain");
	}

	/**
	 * TestCase implementation.
	 * 
	 * @see TestCase#tearDown() TestCase
	 */
	public void tearDown() {
		mapper = null;
		filterChain = null;
	}

	//
	// The Tests!
	//

	public void testMapFileNameNone() {		
		String[] mapped = mapper.mapFileName("-A-X-");
		assertNotNull(mapped);
		assertEquals(1, mapped.length);
		assertEquals("-A-X-", mapped[0]);
	}

	public void testMapFileNameSingle() {
		filterChain.addReplaceString(createReplaceString("A", "B"));
		String[] mapped = mapper.mapFileName("-A-X-");
		assertNotNull(mapped);
		assertEquals(1, mapped.length);
		assertEquals("-B-X-", mapped[0]);
	}

	public void testMapFileNameFromNull() {
		filterChain.addReplaceString(createReplaceString("A", "B"));
		filterChain.addReplaceString(createReplaceString("X", "Y"));
		String[] mapped = mapper.mapFileName("-A-X-");
		assertNotNull(mapped);
		assertEquals(1, mapped.length);
		assertEquals("-B-Y-", mapped[0]);
	}

	//
	// Utils
	//
	private static ReplaceString createReplaceString(String from, String to) {
		ReplaceString replaceString = new ReplaceString();
		replaceString.setFrom(from);
		replaceString.setTo(to);
		return replaceString;
	}
}
