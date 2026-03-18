package uk.gov.courtservice.xhibit.cpp.scripts.tests;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.cpp.scripts.DocumentType;
public class TestDocumentType extends TestCase {
	
	public void testDocumentTypes() {
		
		DocumentType dl = DocumentType.DL;
		assertTrue(dl.name().equals("DL"));
		assertTrue(dl.getDocName().equals("DailyList"));
		
		DocumentType wl = DocumentType.WL;
		assertTrue(wl.name().equals("WL"));
		assertTrue(wl.getDocName().equals("WarnedList"));
		
		DocumentType fl = DocumentType.FL;
		assertTrue(fl.name().equals("FL"));
		assertTrue(fl.getDocName().equals("FirmList"));
		
		DocumentType pD = DocumentType.PD;
		assertTrue(pD.name().equals("PD"));
		assertTrue(pD.getDocName().equals("PublicDisplay"));
		
		DocumentType wP = DocumentType.WP;
		assertTrue(wP.name().equals("WP"));
		assertTrue(wP.getDocName().equals("WebPage"));
		
	}

}
