package uk.gov.courtservice.xhibit.business.services.cppstaginginbound.test;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.services.cppstaginginbound.CppStagingInboundController;
import uk.gov.courtservice.xhibit.business.vos.entities.CppStagingInboundBasicValue;

public class TestCppStagingInbound extends TestCase {
	
	//private String PROVIDER_URL="t3://xdevdis30:7003";
	private static String PROVIDER_URL="t3://localhost:7003";
	private static String INITIAL_CONTEXT_FACTORY="weblogic.jndi.WLInitialContextFactory";
	static InitialContext ctx = null;
	static CppStagingInboundController ejbRef = null;
	//static FormattingController ejbRef = null;
	
	// Setup this value depending on whether there are documents to be processed
	static boolean DOCUMENTS_EXPECTED_FOR_PROCESSING = true;
	static boolean DOCUMENTS_EXPECTED_FOR_VALIDATION = true;
	static String testUsername = "JUnit";
	
	@Before
	public void setUp() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, PROVIDER_URL);

		try {
			ctx = new InitialContext(env);
			assertEquals(false, ctx==null);
			ejbRef = (CppStagingInboundController) ctx.lookup("MidTierCppStagingInboundController_jarCppStagingInboundController_EO");
			assertEquals(false, ejbRef==null);
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
		// Look for any documents that have been inserted into XHB_CPP_STAGING_INBOUND
		try {
			ArrayList<CppStagingInboundBasicValue> latestDoc = ejbRef.getLatestUnprocessedDocument();
			assertEquals(false,ejbRef==null);
			if (DOCUMENTS_EXPECTED_FOR_PROCESSING) {
				System.out.println(latestDoc.toString());
				assertEquals(false,latestDoc==null);
			} else {
				assertEquals(true,latestDoc==null);
			}
		} catch (Exception e) {
			System.out.println("E is "+e);
			fail();
		}	
	}
	
	@Test
	public void testUpdateStatus_ValidationSuccess() {
		// Run a series of tests for different status updates of validation and processing status
		// But first get the latest document
		try {
			ArrayList<CppStagingInboundBasicValue> latestDoc = ejbRef.getLatestUnprocessedDocument();
			assertEquals(false,ejbRef==null);
			
			if (DOCUMENTS_EXPECTED_FOR_PROCESSING) {
				System.out.println(latestDoc.toString());
				assertEquals(false,latestDoc==null);
				if(latestDoc.size()>0) {
					// Test updating status: Validation Successful:: VALIDATION_STATUS='VS', PROCESSING_STATUS='NP'
					ejbRef.updateStatusSuccess(latestDoc.get(0), testUsername);
				}
				
			} else {
				// Cannot do any tests here but this is not necessarily a fail either
			}
		} catch (Exception e) {
			System.out.println("E is "+e);
			fail();
		}	
	}
	
	@Test
	public void testUpdateStatus_ValidationFailed() {
		// Run a series of tests for different status updates of validation and processing status
		// But first get the latest document
		try {
			ArrayList<CppStagingInboundBasicValue> latestDoc = ejbRef.getLatestUnprocessedDocument();
			assertEquals(false,ejbRef==null);
			
			if (DOCUMENTS_EXPECTED_FOR_PROCESSING) {
				System.out.println(latestDoc.toString());
				assertEquals(false,latestDoc==null);
				
				// Test updating status: Validation Successful:: VALIDATION_STATUS='VF', VALIDATION_ERROR_MESSAGE='ERROR'
				if(latestDoc.size()>0) {
					ejbRef.updateStatusFailed(latestDoc.get(0), "There was an error", testUsername);
				}
			} else {
				// Cannot do any tests here but this is not necessarily a fail either
			}
		} catch (Exception e) {
			System.out.println("E is "+e);
			fail();
		}	
	}
	
	@Test
	public void testUpdateStatus_ValidationInProcess() {
		// Run a series of tests for different status updates of validation and processing status
		// But first get the latest document
		try {
			ArrayList<CppStagingInboundBasicValue> latestDoc = ejbRef.getLatestUnprocessedDocument();
			assertEquals(false,ejbRef==null);

			if (DOCUMENTS_EXPECTED_FOR_PROCESSING) {
				System.out.println(latestDoc.toString());
				assertEquals(false,latestDoc==null);
				
				if(latestDoc.size()>0) {
					// Test updating status: Validation In Process:: VALIDATION_STATUS='IP'
					ejbRef.updateStatusInProcess(latestDoc.get(0), testUsername);
				}
			} else {
				// Cannot do any tests here but this is not necessarily a fail either
			}
		} catch (Exception e) {
			System.out.println("E is "+e);
			fail();
		}
	}
	
	@Test
	public void testUpdateStatus_ProcessingSuccess() {
		// Run a series of tests for different status updates of validation and processing status
		// But first get a documetn that has been validated and not processed
		try {
			ArrayList<CppStagingInboundBasicValue> latestDoc = ejbRef.getNextValidatedDocument();
			assertEquals(false,ejbRef==null);
			
			if (DOCUMENTS_EXPECTED_FOR_PROCESSING) {
				System.out.println(latestDoc.toString());
				assertEquals(false,latestDoc==null);
				
				if(latestDoc.size()>0) {
					// Test updating status: Validation Successful:: PROCESSING_STATUS='SP'
					ejbRef.updateStatusProcessingSuccess(latestDoc.get(0), testUsername);
				}
				
			} else {
				// Cannot do any tests here but this is not necessarily a fail either
			}
		} catch (Exception e) {
			System.out.println("E is "+e);
			fail();
		}	
	}
	
	@Test
	public void testUpdateStatus_ProcessingFailed() {
		// Run a series of tests for different status updates of validation and processing status
		// But first get a documetn that has been validated and not processed
		try {
			ArrayList<CppStagingInboundBasicValue> latestDoc = ejbRef.getNextValidatedDocument();
			assertEquals(false,ejbRef==null);
			
			if (DOCUMENTS_EXPECTED_FOR_PROCESSING) {
				System.out.println(latestDoc.toString());
				assertEquals(false,latestDoc==null);
				if(latestDoc.size()>0) {
					// Test updating status: Validation Failed:: PROCESSING_STATUS='PF'
					ejbRef.updateStatusProcessingFail(latestDoc.get(0), "Successfully failed via a unit test", testUsername);
				}
				
			} else {
				// Cannot do any tests here but this is not necessarily a fail either
			}
		} catch (Exception e) {
			System.out.println("E is "+e);
			fail();
		}	
	}
	
	@Test
	public void testValidateDocument() {
		// Validate a document which has the VALIDATION_STATUS='IP'
		// But first get the latest document
		try {
			ArrayList<CppStagingInboundBasicValue> thisDoc = ejbRef.getNextDocumentToValidate();
			assertEquals(false,ejbRef==null);

			if (DOCUMENTS_EXPECTED_FOR_VALIDATION) {
				System.out.println(thisDoc.toString());
				assertEquals(false,thisDoc==null);
				
				if(thisDoc.size()>0) {
					// Test updating status: Validation Successful:: VALIDATION_STATUS='IP'
					ejbRef.validateDocument(thisDoc.get(0), testUsername);
				}
			} else {
				// Cannot do any tests here but this is not necessarily a fail either
			}
		} catch (Exception e) {
			System.out.println("E is "+e);
			fail();
		}	
	}
	
	@Test
	public void testIsValidDocumentType() {
		// For any document picked up to be processed, check that the document type is valid; if not then update status to indicate VF
		try {
			String documentType1_Valid = "WP";
			String documentType2_Valid = "PD";
			String documentType3_Valid = "DL";
			String documentType4_Valid = "FL";
			String documentType5_Valid = "WL";
			String documentType1_Invalid = "WD";
			String documentType2_Invalid = "WX";
			String documentType3_Invalid = "LF";
			String documentType4_Invalid = "WL1";
			String documentType5_Invalid = "";
			
			boolean validDocType = ejbRef.isValidDocumentType(documentType1_Valid);
			assertEquals(true,validDocType);
			
			validDocType = ejbRef.isValidDocumentType(documentType2_Valid);
			assertEquals(true,validDocType);
			
			validDocType = ejbRef.isValidDocumentType(documentType3_Valid);
			assertEquals(true,validDocType);
			
			validDocType = ejbRef.isValidDocumentType(documentType4_Valid);
			assertEquals(true,validDocType);
			
			validDocType = ejbRef.isValidDocumentType(documentType5_Valid);
			assertEquals(true,validDocType);
			
			validDocType = ejbRef.isValidDocumentType(documentType1_Invalid);
			assertEquals(false,validDocType);
			
			validDocType = ejbRef.isValidDocumentType(documentType2_Invalid);
			assertEquals(false,validDocType);
			
			validDocType = ejbRef.isValidDocumentType(documentType3_Invalid);
			assertEquals(false,validDocType);
			
			validDocType = ejbRef.isValidDocumentType(documentType4_Invalid);
			assertEquals(false,validDocType);
			
			validDocType = ejbRef.isValidDocumentType(documentType5_Invalid);
			assertEquals(false,validDocType);
			
		} catch (Exception e) {
			System.out.println("E is "+e);
			fail();
		}	
	}
	
	@Test
	public void testIsValidDocumentName() {
		// For any document picked up to be processed, check that the document type is valid; if not then update status to indicate VF
		try {
			String documentName1_Valid = "WebPage_453_20200106120000.xml";
			String documentName2_Valid = "PublicDisplay_453_20200106120000.xml";
			String documentName3_Valid = "DailyList_453_20200106120000.xml";
			String documentName4_Valid = "FirmList_453_20200106120000.xml";
			String documentName5_Valid = "WarnedList_453_20200106120000.xml";
			
			// Invalid date/time
			String documentName1_Invalid = "DailyList_453_20200106240000.xml"; // Invalid hours
			String documentName2_Invalid = "WebPage_453_20190106120000.xml"; // In the past
			
			// Invalid court code 
			String documentName3_Invalid = "WebPage_4530_20190106120000.xml";
			String documentName4_Invalid = "WebPage_399_20190106120000.xml";
			
			// Invalid type of document
			String documentName5_Invalid = "WebPages_453_20190106120000.xml";
			String documentName6_Invalid = "WebPag_453_20190106120000.xml";
			
			// Invalid extension name
			String documentName7_Invalid = "DailyList_453_20190106120000.xsd";
			String documentName8_Invalid = "WebPage_453_20190106120000.xmll";
			
			String fl_Test1 = "FirmList_453_20200203000000.xml";
			String dl_Test1 = "DailyList_453_20201901010000.xml";
			
			boolean validDocName = ejbRef.isValidDocumentName(documentName1_Valid);
			assertEquals(true,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName2_Valid);
			assertEquals(true,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName3_Valid);
			assertEquals(true,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName4_Valid);
			assertEquals(true,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName5_Valid);
			assertEquals(true,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName1_Invalid);
			assertEquals(false,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName2_Invalid);
			assertEquals(false,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName3_Invalid);
			assertEquals(false,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName4_Invalid);
			assertEquals(false,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName5_Invalid);
			assertEquals(false,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName6_Invalid);
			assertEquals(false,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName7_Invalid);
			assertEquals(false,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(documentName8_Invalid);
			assertEquals(false,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(fl_Test1);
			assertEquals(false,validDocName);
			
			validDocName = ejbRef.isValidDocumentName(dl_Test1);
			assertEquals(false,validDocName);
			
		} catch (Exception e) {
			System.out.println("E is "+e);
			fail();
		}
	}


}
