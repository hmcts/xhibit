package uk.gov.courtservice.xhibit.business.services.cpp;

import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.util.ArrayList;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.services.cpp.CPPInitialProcessingController;
import uk.gov.courtservice.xhibit.business.services.cppformatting.CppFormattingController;
import uk.gov.courtservice.xhibit.business.services.cpplist.CppListController;
import uk.gov.courtservice.xhibit.business.services.cppstaginginbound.CppStagingInboundController;
import uk.gov.courtservice.xhibit.business.vos.entities.CppListBasicValue;
//import uk.gov.courtservice.xhibit.business.services.formatting.FormattingController;
import uk.gov.courtservice.xhibit.business.vos.entities.CppStagingInboundBasicValue;

public class TestCPPInitialProcessing extends TestCase {

	// private String PROVIDER_URL="t3://xdevdis30:7003";
	private static String PROVIDER_URL = "t3://localhost:7003";
	private static String INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";
	static InitialContext ctx = null;
	static CPPInitialProcessingController ejbRef = null;
	static CppStagingInboundController ejbRef2 = null;
	static CppListController ejbRef3 = null;
	static CppFormattingController ejbRef4 = null;
	// static FormattingController ejbRef = null;

	@Before
	public void setUp() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, PROVIDER_URL);

		try {
			ctx = new InitialContext(env);
			assertEquals(false, ctx == null);
			ejbRef = (CPPInitialProcessingController) ctx.lookup("MidTierListDistribution2Controller_jarCPPInitialProcessingController_EO");
			ejbRef2 = (CppStagingInboundController) ctx.lookup("MidTierCppStagingInboundController_jarCppStagingInboundController_EO");
			ejbRef3 = (CppListController) ctx.lookup("MidTierCppListController_jarCppListController_EO");
			ejbRef4 = (CppFormattingController) ctx.lookup("MidTierCppFormattingController_jarCppFormattingController_EO");
			assertEquals(false, ejbRef == null);
		} catch (NoInitialContextException nice) {
			System.out.println("Cannot find Initial Context:");
			nice.printStackTrace();
			fail();
		} catch (NamingException ne) {
			System.out.println("Naming Exception:");
			ne.printStackTrace();
			fail();
		}
	}

	@Test
	public void testFindStagingInboundDocuments() {
		// Look for any documents that have been inserted into
		// XHB_CPP_STAGING_INBOUND
		try {
			ejbRef.processCPPStagingInboundMessages();
			assertEquals(false, ejbRef == null);
		} catch (Exception e) {
			System.out.println("E is " + e);
			fail();
		}
	}

	@Test
	public void testProcessValidatedDocument() {
		try {
			ArrayList<CppStagingInboundBasicValue> thisDoc = ejbRef2.getNextValidatedDocument();
			if(thisDoc.size()>0){
        boolean validated = ejbRef.processValidatedDocument(thisDoc.get(0));
        assertEquals(true, validated == true);
      }
		} catch (Exception e) {
			System.out.println("E is " + e);
			fail();
		}
	}

	@Ignore
	public void testGetListStartDate() {
		try {
			ArrayList<CppStagingInboundBasicValue> thisDoc = ejbRef2.getNextValidatedDocument();
			assertEquals("Testing thisDoc", true, thisDoc != null);
			if (thisDoc != null && thisDoc.size()>0) {
				String docType = thisDoc.get(0).getDocumentType();
				if (docType.equals("DL") || docType.equals("FL") || docType.equals("WL")) {
					// Get the xml and retrieve the start date
					String xml = ejbRef2.getClobXmlAsString(thisDoc.get(0).getClobId());
					Date startDate = ejbRef.getListStartDate(xml, docType);
					assertEquals(true, startDate != null);
				}
			}
		} catch (Exception e) {
			System.out.println("E is " + e);
			fail();
		}
	}

	@Test
	public void testGetListEndDate() {
		try {
			ArrayList<CppStagingInboundBasicValue> thisDoc = ejbRef2.getNextValidatedDocument();
			assertEquals(true, thisDoc != null);
			
			if (thisDoc != null && thisDoc.size()>0) {
				String docType = thisDoc.get(0).getDocumentType();
				if (docType.equals("DL") || docType.equals("FL") || docType.equals("WL")) {
					// Get the xml and retrieve the end date
					String xml = ejbRef2.getClobXmlAsString(thisDoc.get(0).getClobId());
					Date endDate = ejbRef.getListEndDate(xml, docType);
					assertEquals(true, endDate != null);
				}
			}
		} catch (Exception e) {
			System.out.println("E is " + e);
			fail();
		}
	}
	
	@Test
	public void testGetCourtCode() {
		try {
			ArrayList<CppStagingInboundBasicValue> thisDoc = ejbRef2.getNextValidatedDocument();
			assertEquals(true, thisDoc != null);
			
			if (thisDoc != null && thisDoc.size()>0) {
				String docType = thisDoc.get(0).getDocumentType();
				if (docType.equals("DL") || docType.equals("FL") || docType.equals("WL")) {
					// Get the xml and retrieve the court code
					String xml = ejbRef2.getClobXmlAsString(thisDoc.get(0).getClobId());
					String courtCode = ejbRef.getCourtHouseCode(xml, docType);
					assertEquals(true, courtCode != null);
				}
			}
		} catch (Exception e) {
			System.out.println("E is " + e);
			fail();
		}
	}
	
	
	/**
	 * Should really be in TestCppList but that hasnt been setup yet
	 */
	@Test
	public void testCheckForExistingCppListRecord() {
		try {
			
			ArrayList<CppStagingInboundBasicValue> thisDoc = ejbRef2.getNextValidatedDocument();
			assertEquals("Testing thisDoc", true, thisDoc != null);
			
			if (thisDoc != null && thisDoc.size()>0) {
				// Get the xml and retrieve the start/end date
				String xml = ejbRef2.getClobXmlAsString(thisDoc.get(0).getClobId());

				String docType = thisDoc.get(0).getDocumentType();
				if (docType.equals("DL") || docType.equals("FL") || docType.equals("WL")) {
			
					Date startDate = ejbRef.getListStartDate(xml, "DL");
					Date endDate = ejbRef.getListEndDate(xml, "DL");
					assertEquals(true, startDate != null);
					assertEquals(true, endDate != null);
			
					String courtCode = ejbRef.getCourtHouseCode(xml,"DL");
					assertEquals(true, courtCode != null);
			
					CppListBasicValue listDoc = ejbRef3.checkForExistingCppListRecord(new Integer(courtCode), docType, startDate, endDate);
					assertEquals(true, listDoc != null);
				}
			}
			
		} catch (Exception e) {
			System.out.println("E is " + e);
			fail();
		}
	}
	
	@Test
	public void testCheckForExistingListRecord2() {
		try {
			CppListBasicValue listDoc = ejbRef3.checkForExistingCppListRecord(453, "DL", new Date(), new Date());
			assertEquals(true, listDoc != null);
		} catch (Exception e) {
			System.out.println("E is " + e);
			fail();
		}
	}
	
	@Ignore
	public void testCreateUpdateListRecords() {
		// Use testProcessValidatedDocument
	}
	
	@Ignore
	public void testCreateUpdateNonListRecords() {
		// Use testProcessValidatedDocument
	}
	
	@Ignore
	public void processValidatedDocument1() {
		// Use testProcessValidatedDocument
	}
	
	@Ignore
	public void processValidatedDocument2() {
		// Use testProcessValidatedDocument
	}
	
	@Ignore
	public void processValidatedDocument3() {
		// Use testProcessValidatedDocument
	}
	
	@Ignore
	public void processValidatedDocument4() {
		// Use testProcessValidatedDocument
	}

}
