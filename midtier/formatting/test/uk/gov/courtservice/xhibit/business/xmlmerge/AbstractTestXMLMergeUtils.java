package uk.gov.courtservice.xhibit.business.xmlmerge;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.services.formatting.AbstractListXMLMergeUtils;
import uk.gov.courtservice.xhibit.business.services.formatting.AbstractXMLMergeUtils;
import uk.gov.courtservice.xhibit.business.services.formatting.DailyListXMLMergeUtils;
import uk.gov.courtservice.xhibit.business.services.formatting.FirmListXMLMergeUtils;
import uk.gov.courtservice.xhibit.business.services.formatting.WarnedListXMLMergeUtils;

public abstract class AbstractTestXMLMergeUtils extends TestCase {
 
	protected final AbstractXMLMergeUtils xmlUtils;	
	private static final Integer SORT_TEST_VALUE = Integer.valueOf(13);
	protected final Logger log = Logger.getLogger(AbstractTestXMLMergeUtils.class);
	
	public AbstractTestXMLMergeUtils(AbstractXMLMergeUtils xmlUtils) throws XPathExpressionException {
		this.xmlUtils = xmlUtils;
	}
	
	//------------- Tests -------------
	
	// Test success
	public void testSuccess() throws Exception {
		TestDocument originalDocument = new TestDocument();
		TestDocument documentToMerge = new TestDocumentToMerge(originalDocument.getNoOfSittings() + 1);
		
		debugDocuments("testSuccess", originalDocument.document, documentToMerge.document);
		expectSuccess(originalDocument, documentToMerge);
	}

	// Test failure - Missing root node 
	public void testFailureMissingRootNode() throws Exception {
		TestDocument blankDocument = new InvalidDocument();
		TestDocument validDocument = new TestDocument();		
		
		expectMergeFailure(blankDocument.document, validDocument.document);	
		//if xhibit is fine and cpp isn't we expect success 
		expectOriginalDocument(validDocument.document, blankDocument.document);			
	}
	

	// Test success - Empty courts should not be merged
	public void testSuccessEmptyCourtRoom() throws Exception {
		TestDocument originalDocument = new TestDocument();
		TestDocument documentToMerge = new TestDocumentToMerge(originalDocument.getNoOfSittings() + 1) {
			private static final int EMPTY_COURT_HOUSE_NO = 99;
			@Override
			protected Element addCourtLists(Element rootNode) {
				Element courtListsNode = super.addCourtLists(rootNode);
				// Add an empty courtList
				addCourtList(courtListsNode, EMPTY_COURT_HOUSE_NO);
				return courtListsNode;
			}
			@Override
			protected void addChildren(Element parentNode, int courtHouseNo) {
				// Do not add child nodes (sittings) for the empty courthouse
				if (courtHouseNo != EMPTY_COURT_HOUSE_NO) {
					super.addChildren(parentNode, courtHouseNo);
				}
			}
		};
		
		debugDocuments("testSuccessEmptyCourtRoom", originalDocument.document, documentToMerge.document);
		expectSuccess(originalDocument, documentToMerge);
	}

	protected int expectedSortParentPosition() {
		return 1;
	}
	
	protected int expectedSortPosition() {
		return 2;
	}
		
	/**
	 * Method used in this test class to transform the Document to pure String so that we can use it to compare.
	 */
	protected String transformXml(Document doc) throws TransformerException {
		String s1 = xmlUtils.docToString(doc);
		s1 = s1.replaceAll("\\n","");
		s1 = s1.replaceAll("\\r", "");	
		if(xmlUtils instanceof DailyListXMLMergeUtils) {
			s1 = s1.substring(s1.indexOf("<cs:DailyList>"));
		} else if(xmlUtils instanceof FirmListXMLMergeUtils) {
			s1 = s1.substring(s1.indexOf("<cs:FirmList"));
		} else {
			s1 = s1.substring(s1.indexOf("<cs:WarnedList>"));
		}
		
		return s1;
	}
	//------------- Private/Protected Methods -------------
	
	protected void debugDocuments( String method, Document originalDocument, Document documentToMerge) throws Exception {
		log.debug(xmlUtils.docToString(originalDocument));
		log.debug(xmlUtils.docToString(documentToMerge));
	}
	
	protected void expectSuccess(TestDocument originalDocument, TestDocument documentToMerge) {
		try {
			Document mergedDocument = xmlUtils.merge(originalDocument.document, documentToMerge.document);
			log.debug(xmlUtils.docToString(mergedDocument));
			
			// Validate the process produced a merged document
			assertNotNull(mergedDocument);
						
			// Check the number of CourtHouses in the merged document
			NodeList courtHouseLists = mergedDocument.getElementsByTagName(AbstractListXMLMergeUtils.Tag.COURTLIST);
			int expectedNoOfCourtHouses = originalDocument.noOfCourtHouses < documentToMerge.noOfCourtHouses ? documentToMerge.noOfCourtHouses : originalDocument.noOfCourtHouses;
			int actualNoOfCourtHouses = courtHouseLists.getLength();
			assertEquals(expectedNoOfCourtHouses, actualNoOfCourtHouses);
			
			// Validate the sort list
			List<String> positionArray = Arrays.asList(xmlUtils.getNodePositionArray());
			Node courtList2 = courtHouseLists.item(1);
			assertNotNull(courtList2);
			Node sittingsNode = courtList2.getChildNodes().item(expectedSortParentPosition());
			assertNotNull(sittingsNode);
			Node sittingNode = sittingsNode.getChildNodes().item(expectedSortPosition());
			assertNotNull(sittingNode);
			Map<String, String> map = AbstractXMLMergeUtils.getNodeMapValues(positionArray, sittingNode);
			assertTrue(map.size() > 1);
			if (isWarned()) {
				String actualValue = map.get(positionArray.get(0));
				String expectedValue = generateDate(SORT_TEST_VALUE);
				assertEquals(expectedValue, actualValue);
			} else if (isFirm()) {
				String actualValue = map.get(positionArray.get(0));
				String expectedValue = SORT_TEST_VALUE.toString();
				assertEquals(expectedValue, actualValue);
			} else {
				String actualValue = map.get(positionArray.get(1));
				String expectedValue = generateTime(SORT_TEST_VALUE);
				assertEquals(expectedValue, actualValue);
			}
			
		} catch (Exception e) {
			fail("Unexpected Exception thrown: " + e.getClass()+ e);
		}		
	}
	
	private void expectMergeFailure(Document originalDocument, Document documentToMerge) {
		try {			
			xmlUtils.merge(originalDocument, documentToMerge);			
			fail("Merge succeeded - Expecting failure");
		} catch (Exception e) {
			if(e.getClass()!=IOException.class) {
				fail("Invalid Exception thrown: " + e.getClass());
			}
		}
	}
	
	private void expectOriginalDocument(Document originalDocument, Document documentToMerge) {
		try {			
			Document merged = xmlUtils.merge(originalDocument, documentToMerge);			
			assertEquals(merged,originalDocument);
		} catch (Exception e) {
			fail("Invalid Exception thrown: " + e.getClass());
		}
	}

	private String generateDate(Integer value) {
		Calendar today = Calendar.getInstance();
		String days = (value < 10 ? "0" : "") + value.toString();
		String months = Integer.toString(1);
		String years = Integer.toString(today.get(Calendar.YEAR));
		return days+"/"+ months+"/"+years;
	}
	
	private String generateTime(Integer value) {
		String hour = (value < 10 ? "0" : "") + value.toString();
		String minutes = Integer.toString(value + 11);
		String seconds = Integer.toString(value + 12);
		return hour+":"+ minutes+":"+seconds;
	}
	
	private boolean isFirm() {
		return xmlUtils.getRootNodes()[0].contains("Firm");
	}
	
	private boolean isWarned() {
		return xmlUtils.getRootNodes()[0].contains("Warned");
	}

	//------------- Test Documents -------------
	
	private class InvalidDocument extends TestDocument {
		@Override
		protected void addXML() {
			Element rootNode = addNode(null, "bob");
			addCourtLists(rootNode);
		}
	}
	
	protected class EmptyDocument extends TestDocument {
		@Override
		public int getNoOfSittings() {
			return 0;
		}
	}
	
	protected class EmptyDocumentToMerge extends TestDocumentToMerge {
		public EmptyDocumentToMerge(int startSittingNo) {
			super(startSittingNo);
		}
		@Override
		public int getNoOfSittings() {
			return 0;
		}
	}
	
	protected class TestDocumentToMerge extends TestDocument {

		public TestDocumentToMerge(int startSittingNo) {
			super(startSittingNo, 3);
		}		
		
		@Override
		protected void addChildren(Element parentNode, int courtHouseNo) {
			if (isFirm()) {
				super.addChildren(parentNode, courtHouseNo);
				if (courtHouseNo == 2) {
					super.addGroupNode(parentNode, courtHouseNo, 2, SORT_TEST_VALUE);
					super.addGroupNode(parentNode, courtHouseNo, 10, SORT_TEST_VALUE);
				}
			} else {
				if (courtHouseNo == 2) {
					super.addGroupNode(parentNode, courtHouseNo, 2, SORT_TEST_VALUE);
					if (isWarned()) {	
						super.addGroupNode(parentNode, courtHouseNo, 2, SORT_TEST_VALUE+1);
						super.addGroupNode(parentNode, courtHouseNo, 10, SORT_TEST_VALUE+1);
					} else {
						super.addGroupNode(parentNode, courtHouseNo, 10, SORT_TEST_VALUE);
					}
				}
				super.addChildren(parentNode, courtHouseNo);
			}
		}
	}
	
	protected class TestDocument {
		 
		private static final String COURTHOUSE = "CourtHouse";
		private static final String DESCRIPTION = "cs:Description";
		private static final int TWO_SITTINGS = 2;
		protected Document document;
		private int startSittingNo;
		protected int noOfCourtHouses;
		
		public TestDocument() {			
			this(1, 2);
		}
		
		public TestDocument(int startSittingNo, int noOfCourtHouses) {
			try {
				this.document = getDocBuilder().newDocument();
				this.startSittingNo = startSittingNo;
				this.noOfCourtHouses = noOfCourtHouses;
				addXML();				
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}

		public int getNoOfSittings() {
			return TWO_SITTINGS;
		}

		private String getRootNode() {
			if (isFirm()) {
				return "cs:FirmList";
			} else if (isWarned()) {
				return "cs:WarnedList";	
			}
			return "cs:DailyList";
		}

		protected String[] getChildNodeNames() {
			if (isWarned()) {
				return new String[] {WarnedListXMLMergeUtils.Tag.WITH_FIXED_DATE, WarnedListXMLMergeUtils.Tag.WITHOUT_FIXED_DATE};	
			}
			return new String[] {AbstractListXMLMergeUtils.Tag.SITTINGS};
		}
				
		protected void addXML() {
			Element rootNode = addNode(null, getRootNode());
			addCourtLists(rootNode);
		}
		
		protected Element addCourtLists(Element rootNode) {
			Element courtListsNode = addNode(rootNode, AbstractListXMLMergeUtils.Tag.COURTLISTS);
			for (int courtHouseNo = 1; courtHouseNo <= noOfCourtHouses; courtHouseNo++ ) {
				addCourtList(courtListsNode, courtHouseNo);				
			}			
			return courtListsNode;
		}
		
		protected void addCourtList(Element courtListsNode, Integer courtHouseNo) {
			Element courtListNode = addNode(courtListsNode, AbstractListXMLMergeUtils.Tag.COURTLIST);
			addCourtHouse(courtListNode, courtHouseNo);
			if (getChildNodeNames().length > 0) {
				for (String childNodeName : getChildNodeNames()) {
					Element childNode = addNode(courtListNode, childNodeName);
					addChildren(childNode, courtHouseNo);
				}
			}
		}
		
		private void addCourtHouse(Element courtListNode, Integer courtHouseNo) {
			Element courtHouseNode = addNode(courtListNode, AbstractListXMLMergeUtils.Tag.COURTHOUSE);
			Element courtHouseCodeNode = addNode(courtHouseNode, xmlUtils.getNodeMatchArray()[0]);
			courtHouseCodeNode.setTextContent(courtHouseNo.toString());
			Element courtHouseNameNode = addNode(courtHouseNode, xmlUtils.getNodeMatchArray()[1]);
			courtHouseNameNode.setTextContent(COURTHOUSE+courtHouseNo.toString());
		}
		
		protected void addChildren(Element parentNode, int courtHouseNo) {
			for (int i=0; i < getNoOfSittings(); i++) {
				addGroupNode(parentNode, courtHouseNo, Integer.valueOf(i + startSittingNo), Integer.valueOf(i + startSittingNo));
			}
		}
		
		protected void addGroupNode(Element parentNode, Integer courtHouseNo, Integer courtRoomNo, Integer sittingNo) {
			Element groupNode;
			String[] values = new String[xmlUtils.getNodePositionArray().length];
			if (isWarned()) {
				groupNode = addNode(parentNode, WarnedListXMLMergeUtils.Tag.FIXTURE);
				values[0] = generateDate(sittingNo);
				values[1] = courtRoomNo.toString();
			} else if (isFirm()) {
				groupNode = addNode(parentNode, AbstractListXMLMergeUtils.Tag.SITTING);
				values[0] = sittingNo.toString();
				values[1] = generateDate(courtHouseNo);
			} else {
				groupNode = addNode(parentNode, AbstractListXMLMergeUtils.Tag.SITTING);
				values[0] = courtRoomNo.toString();
				values[1] = generateTime(sittingNo);
			} 
			for (int nodeNo = 0; nodeNo < xmlUtils.getNodePositionArray().length; nodeNo++) {
				Element valueNode = addNode(groupNode, xmlUtils.getNodePositionArray()[nodeNo]);
				valueNode.setTextContent(values[nodeNo]);
			}
			Element descriptionNode = addNode(groupNode, DESCRIPTION);
			descriptionNode.setTextContent(COURTHOUSE+courtHouseNo.toString()+", room"+courtRoomNo.toString());
		}

		protected Element addNode(Element parentNode, String nodeName) {
			Element node = document.createElement(nodeName);
			if (parentNode == null) {
				document.appendChild(node);
			} else {
				parentNode.appendChild(node);				
			}
			return node;
		}	

		private DocumentBuilder getDocBuilder() throws ParserConfigurationException {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setIgnoringElementContentWhitespace(true);
			docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			return docBuilderFactory.newDocumentBuilder();
		}
	}
	
	/**
	 * Returns a document with externals not allowed.
	 * This was flagged up by sonarqube.
	 * @param xml1 
	 * @return Document
	 * @throws ParserConfigurationException
	 * @throws IOException 
	 * @throws SAXException 
	 */
	protected Document getDocBuilderWithNoExternals(String xml1) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xml1)));
	}
}
