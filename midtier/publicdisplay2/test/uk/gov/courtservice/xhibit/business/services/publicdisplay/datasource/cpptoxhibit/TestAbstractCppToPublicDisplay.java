package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

import junit.framework.TestCase;

public abstract class TestAbstractCppToPublicDisplay extends TestCase {
	
	protected final AbstractCppToPublicDisplay cppToPublicDisplay;	

	public TestAbstractCppToPublicDisplay(AbstractCppToPublicDisplay cppToPublicDisplay) {
		this.cppToPublicDisplay = cppToPublicDisplay;
	}
	
	/**
	 * Tests any basic get/set methods on the class
	 * @throws Exception
	 */
	public void testGetsAndSets() throws Exception {
		cppToPublicDisplay.setCourtName("TEST");
		assertEquals("Error in field CourtName", "TEST", cppToPublicDisplay.getCourtName());
	}
	
	
}