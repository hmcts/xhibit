package  uk.gov.courtservice.xhibit.business.services.formatting;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.gov.courtservice.xhibit.business.services.formatting.FirmListXMLMergeUtils.Tag;

public abstract class AbstractXMLMergeUtils extends AbstractXMLUtils{

	/**
	 * Class constants.
	 */
	private static final String[] dailyListRootNodes = new String[]{"DailyList/CourtLists"};
	private static final String[] firmListRootNodes = new String[]{"FirmList/CourtLists","FirmList/ReserveList"};
	private static final String[] warnedListRootNodes = new String[]{"WarnedList/CourtLists/CourtList"};
	private static final String IWP = "IWP";
	private static final String INVALID_AMOUNT_OF_DOCS = "Not enough documents to merge";
	private static final String INVALID_FIRST_DOC = "Primary document does not contain any root nodes";
	private static final String COURT_ROOM_NAME = "courtroomname";
	private static final String CASES = "cases";
	private static final String COURT = "Court ";
	private static final String CROWN_COURT = "Crown Court ";
	private static final String COURTHOUSE_NAMEPREFIX = "at ";
	private static final String EMPTY_STRING = "";
	protected static final int LESS_THAN = -1;
	protected static final int EQUAL_TO = 0;
	protected static final int GREATER_THAN = 1;
	private static final Logger log = Logger.getLogger(AbstractXMLMergeUtils.class);

	private final String[] rootNodes;	
	private final XPathExpression[] rootNodeExpression;
	
	private XPathExpression[] listRootNodeExpression = null;

	public abstract String[] getNodeMatchArray();
	public abstract String[] getNodePositionArray();
	public abstract String getMergeType();

	public AbstractXMLMergeUtils(final String[] rootNodes) throws XPathExpressionException {
		this.rootNodes = rootNodes;
		this.rootNodeExpression = new XPathExpression[rootNodes.length];
		for (int nodeNo = 0; nodeNo < rootNodes.length; nodeNo++) {
			this.rootNodeExpression[nodeNo] = XPathFactory.newInstance().newXPath().compile(rootNodes[nodeNo]);
		} 
	}
		
	public Document merge(final File... files) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException { 
		DocumentBuilder docBuilder = getDocBuilder();
		Document[] documents = new Document[files.length];
		for (int i = 0; i < files.length; i++) {
			documents[i] = docBuilder.parse(files[i]);
		}
		return merge(documents);
	}

	public Document merge(final Document...documents) throws IOException, XPathExpressionException {
		if (documents.length < 2) {
			throw new IOException(INVALID_AMOUNT_OF_DOCS);
		}
		
		// Use the first document as the base for the merge
		Document baseDocument = documents[0];
		// Get the list of Nodes on the original document
		List<Node> nodes = getNodeList(baseDocument);
		
		// Get the list of Nodes on the documents to be merged 
		List<Node> nodesToMerge = new ArrayList<Node>();
		for (int docNo = 1; docNo < documents.length; docNo++) {
			nodesToMerge.addAll(getNodeList(documents[docNo]));		
		}
		
		//first check if there are any cpp to merge in, if not no point going any further 
		// and just send back the original document 
		if (nodesToMerge.isEmpty()) {
			return baseDocument;
		}
		
		//means we have a blank xhibit document 
		if (nodes.isEmpty()) {
			//we should never get to this point as xhibit will always have all courtlists 
			//in there regardless of whether there's anything going on or not so throw exception
			//as something has gone wrong.
			if(getMergeType().equals(IWP)) {
				throw new IOException(INVALID_FIRST_DOC);
			} else {
				String [] listRootNodes = null;
				if(isDailyList()) {
					listRootNodes = dailyListRootNodes;
				}else if(isFirmList()) {
					listRootNodes = firmListRootNodes;
				} else if(isWarnedList()){
					listRootNodes = warnedListRootNodes;
				} else {
					throw new IOException(INVALID_FIRST_DOC);
				}
				
				//replace the blank courtlists with the new court list
				this.listRootNodeExpression = new XPathExpression[listRootNodes.length];
				for (int nodeNo = 0; nodeNo < listRootNodes.length; nodeNo++) {
					this.listRootNodeExpression[nodeNo] = XPathFactory.newInstance().newXPath().compile(listRootNodes[nodeNo]);
				} 
				
				
				List<Node> nodesToMergeAtParentLevel = new ArrayList<Node>();
				for (int docNo = 1; docNo < documents.length; docNo++) {
					nodesToMergeAtParentLevel.addAll(getCPPNodeList(documents[docNo]));		
				}		
				if (nodesToMergeAtParentLevel.isEmpty()) {
					throw new IOException("Child document does not contain any root nodes");
				}
				nodes = getCPPNodeList(baseDocument);
				if(!nodes.isEmpty() ) {
					if(listRootNodes==warnedListRootNodes){
						mergeBlankWarnedList(baseDocument, nodes, nodesToMerge);
					} else if(listRootNodes==firmListRootNodes){
						//merge any non blank courtlists
						for(int i=0; i<nodesToMergeAtParentLevel.size();i++) {
							Node n = nodesToMergeAtParentLevel.get(i);
							if(getParentNodeByType(n, Tag.RESERVE_LIST) != null) {
								mergeNode(baseDocument,  nodes.get(0).getParentNode(), n);
							} else {
								mergeNode(baseDocument,  nodes.get(0), n);
							}
						}
					} else {
						mergeNode(baseDocument,  nodes.get(0), nodesToMergeAtParentLevel.get(0));
					}
				} else {
					throw new IOException(INVALID_FIRST_DOC);
				}

			}
		} else {
			//if warned list we need to check if any of the courtList has no with/without
			if(isWarnedList()) {
				List<Node> parentNodes = new ArrayList<Node>();
				for(int n=0;n<nodes.size();n++) {
					//get all parent nodes
					Node parent = nodes.get(n).getParentNode();
					if(!parentNodes.contains(parent)) {
						parentNodes.add(parent);
					}
				}
				this.listRootNodeExpression = new XPathExpression[warnedListRootNodes.length];
				for (int nodeNo = 0; nodeNo < warnedListRootNodes.length; nodeNo++) {
					this.listRootNodeExpression[nodeNo] = XPathFactory.newInstance().newXPath().compile(warnedListRootNodes[nodeNo]);
					List<Node> nodesToMerge2 = new ArrayList<Node>();
					for (int docNo = 1; docNo < documents.length; docNo++) {
						nodesToMerge2.addAll(getCPPNodeList(documents[docNo]));		
					}		
					List<Node>allCPPListNodesInXhibit = getCPPNodeList(baseDocument);
					
					if(!allCPPListNodesInXhibit.isEmpty() ) {
						for(int i=0;i<allCPPListNodesInXhibit.size();i++) {
							//if its not in the parent node it means it's blank and doesn't have any with/withouts so in this case we need to manually add 
							//it in.
							if(!parentNodes.contains(allCPPListNodesInXhibit.get(i))) {
								for(int j=0;j<nodesToMerge2.size();j++) {
									if(compareNodes(getNodeMatchArray(), allCPPListNodesInXhibit.get(i),nodesToMerge2.get(j), getMergeType()) == 0) {	
										//get all with/withouts of cpp 
										NodeList allWithWithoutCpp = nodesToMerge2.get(j).getChildNodes();
										for(int i2=0; i2<allWithWithoutCpp.getLength();i2++) {
											//insert all with/withouts below the last child node of xhibit 
											Node childNodeToMerge = allWithWithoutCpp.item(i2);
											if(childNodeToMerge.getNodeName().contains(WarnedListXMLMergeUtils.Tag.WITH_FIXED_DATE) ||childNodeToMerge.getNodeName().contains(WarnedListXMLMergeUtils.Tag.WITHOUT_FIXED_DATE)){
												//insert this node after last child.
												Node tempChildNodeToMerge = baseDocument.importNode(childNodeToMerge, true);												
												allCPPListNodesInXhibit.get(i).appendChild(tempChildNodeToMerge);
												if(nodesToMerge.contains(childNodeToMerge)){
												nodesToMerge.remove(childNodeToMerge);
												}

											}
										}
									}
								}
							}
						}
					}
				} 
			}
			// Do the merge	
			for (Node node : nodes) {
				List<Node> nodesMerged = new ArrayList<Node>();
				for (int nodeNo = 0; nodeNo < nodesToMerge.size(); nodeNo++) {
					Node nodeToMerge = nodesToMerge.get(nodeNo);
					if (isNodeMatchForMerge(node, nodeToMerge)) {
						mergeNode(baseDocument, node, nodeToMerge);
						nodesMerged.add(nodeToMerge);
					}
				}
				
				// Remove any successfully merged				
				for (int nodeNo = nodesToMerge.size(); nodeNo > 0; nodeNo--) {
					Node nodeToMerge = nodesToMerge.get(nodeNo-1);
					if (nodesMerged.contains(nodeToMerge)) {
						nodesToMerge.remove(nodeNo-1);
					}
				}
			}			
		
			//IF IWP then replace datetime
			if(getMergeType().equals(IWP)){
				baseDocument = replaceDateTime(baseDocument);
			} else {
				addRemainderNodes(baseDocument, nodes, nodesToMerge);
			}
			
			//If we have got here and the reserved list hasn't been added
			//from cpp we know xhibit can't have reserved so add it at the end
			if(isFirmList()) {
				for(int i=0;i<nodesToMerge.size();i++) {
					if(getParentNodeByType(nodesToMerge.get(i), Tag.RESERVE_LIST) != null) {
						mergeNode(baseDocument, getFirmNode(baseDocument), nodesToMerge.get(i).getParentNode());
						break;
					}
				}
			}
		}

		return baseDocument;
	} 	
	
	private Node getFirmNode(Document baseDocument) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList)xpath.evaluate("FirmList",baseDocument,XPathConstants.NODESET);
		if(nodes.getLength()>0) {
			return nodes.item(0);
		}

		return null;
	}
	/**
	 * Merges non blank Warned list 
	 * CPP docs with blank Xhibit docs
	 * @param nodesToMerge 
	 * @param nodes 
	 */
	private void mergeBlankWarnedList(Document baseDocument, List<Node> nodes, List<Node> nodesToMerge) {
		//loop over all nodes 
		for(int i=0;i<nodes.size();i++) {
			//loop over all nodes to merge to find the correct court list to merge into
			for(int j=0;j<nodesToMerge.size();j++) {
				if(compareNodes(getNodeMatchArray(), nodes.get(i),nodesToMerge.get(j).getParentNode(), getMergeType()) == 0) {
					//insert this node after last child.
					Node childNodeToMerge = baseDocument.importNode(nodesToMerge.get(j), true);
					nodes.get(i).appendChild(childNodeToMerge);
				}
			}
		}
		
	}
	private void addRemainderNodes(Document baseDocument, List<Node> nodes, List<Node> nodesToMerge) {
		List<Node> nodesImported = new ArrayList<Node>();
		// Add any remainders in the list to the document
		for (int nodeNo = 0; nodeNo < nodesToMerge.size(); nodeNo++) {
			Node nodeToMerge = nodesToMerge.get(nodeNo);
			Node node = nodes.get(nodes.size()-1);
			// Check we aren't merging a child of an already merged parent
			if (!nodesImported.contains(nodeToMerge.getParentNode())) {
				if (addNode(baseDocument, node, nodeToMerge)) {
					nodesImported.add(nodeToMerge.getParentNode());
				}
			}
		}
	}
	
	
	
	/**
	 * Method used in IWP that replaces the date section with the date/time that the
	 * merge is done
	 * @param baseDocument
	 * @return the transformed base document
	 * @throws XPathExpressionException
	 */
	private Document replaceDateTime(Document baseDocument) throws XPathExpressionException {
		Calendar cal = Calendar.getInstance();
		int date = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int year = cal.get(Calendar.YEAR);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList)xpath.evaluate("//datetimestamp",baseDocument,XPathConstants.NODESET);
				
		for(int j=0; j<nodes.getLength();j++) {
			NodeList childNodes = nodes.item(j).getChildNodes();
			for(int i=0; i<childNodes.getLength();i++) {
				if(childNodes.item(i).getNodeName().equals("dayofweek")) {
					childNodes.item(i).setTextContent(returnDay(cal));				
				} else if(childNodes.item(i).getNodeName().equals("date")) {
					childNodes.item(i).setTextContent(String.format("%02d", date));
				} else if(childNodes.item(i).getNodeName().equals("month")) {
					childNodes.item(i).setTextContent(returnMonth(cal));
				} else if(childNodes.item(i).getNodeName().equals("year")) {
					childNodes.item(i).setTextContent(Integer.toString(year));
				} else if(childNodes.item(i).getNodeName().equals("hour")) {
					childNodes.item(i).setTextContent(String.format("%02d", hour));
				} else if(childNodes.item(i).getNodeName().equals("min")) {
					childNodes.item(i).setTextContent(String.format("%02d", min));
				}					
			}
		}						
		return baseDocument;
	}
	public String[] getRootNodes() {
		return this.rootNodes;
	}
	
	protected List<Node> getNodeList(Document document) throws XPathExpressionException {
		List<Node> results = new ArrayList<Node>();
		for (int rootNodeNo = 0; rootNodeNo < rootNodeExpression.length; rootNodeNo++) {
			NodeList nodeList = (NodeList) rootNodeExpression[rootNodeNo].evaluate(document, XPathConstants.NODESET);
			for (int nodeNo = 0; nodeNo < nodeList.getLength(); nodeNo++) {
				results.add(nodeList.item(nodeNo));
			}
		}
		return results;
	}
	
	protected List<Node> getCPPNodeList(Document document) throws XPathExpressionException {
		List<Node> results = new ArrayList<Node>();
		for (int rootNodeNo = 0; rootNodeNo < listRootNodeExpression.length; rootNodeNo++) {
			NodeList nodeList = (NodeList) listRootNodeExpression[rootNodeNo].evaluate(document, XPathConstants.NODESET);
			for (int nodeNo = 0; nodeNo < nodeList.getLength(); nodeNo++) {
				results.add(nodeList.item(nodeNo));
			}
		}
		return results;
	}
	
	protected Node getParentNodeToMerge(Document baseDocument, Node nodeToMerge) {
		Node parentNodeToMerge = nodeToMerge.getParentNode();		
		return baseDocument.importNode(parentNodeToMerge, true); 
	}

	protected boolean addNode(Document baseDocument, Node node, Node nodeToMerge) {
		Node parentNode = node.getParentNode();
		Node grandParent = parentNode.getParentNode();
		Node parentNodeToMerge = getParentNodeToMerge(baseDocument, nodeToMerge);
		if (allowEmptyParentNodes() || !isEmptyParentNode(parentNodeToMerge)) {
			if (grandParent.getNodeType() == Node.DOCUMENT_NODE) {
				parentNode.appendChild(parentNodeToMerge);
			} else {
				grandParent.appendChild(parentNodeToMerge);
			}
		}
		return true;
	}
	
	protected boolean isEmptyParentNode(Node node) {
		String childTag = isWarnedList() ? WarnedListXMLMergeUtils.Tag.FIXTURE : AbstractListXMLMergeUtils.Tag.SITTING;
		return !hasChildNode(node, childTag);
	}

	protected boolean allowEmptyParentNodes() {
		return false;
	}

	protected void mergeNode(Document baseDocument, Node node, Node nodeToMerge) {		
		for (int i = 0; i < nodeToMerge.getChildNodes().getLength(); i++) {			
			Node childNodeToMerge = baseDocument.importNode(nodeToMerge.getChildNodes().item(i), true);
			Node insertBeforeNode = getChildNodeBeforeInsert(node, childNodeToMerge);
			if (insertBeforeNode != null) {
				//if its iwp
				if (getMergeType().equals(IWP)) {
					replaceIWP(childNodeToMerge, insertBeforeNode, node);
				}
				else {
					node.insertBefore(childNodeToMerge, insertBeforeNode);
				}
			} else {
				appendNode(node, childNodeToMerge);
			}
		}
	} 

	private void replaceIWP(Node childNodeToMerge, Node insertBeforeNode, Node node) {
		String courtroomnumber1 = getNodeMapValues(Arrays.asList(COURT_ROOM_NAME),childNodeToMerge).get(COURT_ROOM_NAME);
		String courtroomnumber2 = getNodeMapValues(Arrays.asList(COURT_ROOM_NAME),insertBeforeNode).get(COURT_ROOM_NAME);

		if(courtroomnumber1!=null && courtroomnumber2!=null && courtroomnumber1.equals(courtroomnumber2)) {
			//if there is no cases information in the original xml then we replace with the newer xml
			//otherwise we do nothing and keep the xml as is.
			String cases = getNodeMapValues(Arrays.asList(CASES),insertBeforeNode).get(CASES);
			
			//or if the time in cpp is after
			if(cases==null || cases.equals("") || isCPPAfterXhibit(insertBeforeNode, childNodeToMerge)){			
				node.replaceChild(childNodeToMerge, insertBeforeNode);
			} 			
		} 
		
	}
	/**
	 * Returns true  is cpp is after xhibit in iwp and can therefore be replaced
	 * @param insertBeforeNode
	 * @param childNodeToMerge
	 * @return
	 */
	private boolean isCPPAfterXhibit(Node insertBeforeNode, Node childNodeToMerge) {
		
		String timeOnInsert = getNodeMapValues(Arrays.asList("time"),childNodeToMerge).get("time");
		String dateOnInsert = getNodeMapValues(Arrays.asList("date"),childNodeToMerge).get("date");
		Calendar cppTime = setUpCalendar(timeOnInsert, dateOnInsert);
		
		String timeOnOriginal = getNodeMapValues(Arrays.asList("time"),insertBeforeNode).get("time");
		String dateOnOriginal = getNodeMapValues(Arrays.asList("date"),insertBeforeNode).get("date");
		Calendar xhibitTime = setUpCalendar(timeOnOriginal, dateOnOriginal);
		
		if(xhibitTime!=null && cppTime !=null ) {
			log.debug(xhibitTime.getTime() +" after "+cppTime.getTime()+" ?");
			return cppTime.after(xhibitTime);
		}
		
		return false;
	}
	
	/**
	 * Create a calendar object with specific date and time.
	 * @param timeOnOriginal
	 * @param dateOnOriginal
	 * @return
	 */
	private Calendar setUpCalendar(String time, String date) {
		
		if(time!=null && date!=null && !time.equals("") && !date.equals("")) {
			String [] timeSplit = time.split(":");
			String [] dateSplit = date.split("/");
			
			Calendar c = Calendar.getInstance();
			c.set(Integer.parseInt(dateSplit[2]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[0]), Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]), 0);
			return c;
		}
		
		
		return null;
	}
	protected boolean hasChildNode(Node node, String nodeName) {
		Map<String,String> map = getNodeMapValues(Arrays.asList(nodeName),node);
		 return map.size() > 0;
	}

	protected void appendNode(Node node, Node childNodeToMerge) {
		// If the last child is a text spacer and so is the next one, 
		// clear it to avoid formatting issues
		if (isBlankTextNode(node.getLastChild()) && isBlankTextNode(childNodeToMerge)) {
			node.getLastChild().setTextContent(node.getLastChild().getTextContent().trim());
		}
		if(!getMergeType().equals(IWP)) {
			node.appendChild(childNodeToMerge);
		}
	}

	protected void setNodeAttribute(Node node, String attributeName, String attributeValue) { 
		((Element) node).setAttribute(attributeName, attributeValue);
	}
	
	protected String getNodeAttribute(Node node, String attributeName) { 
		return ((Element) node).getAttribute(attributeName);
	}

	protected Node cloneSiblingNode(Node sittingNode, String tagName) {
		Node parentNode = sittingNode.getParentNode();
		Node result = parentNode.cloneNode(false);
		for (int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
			Node childNode = parentNode.getChildNodes().item(i);
			boolean isMatch = getNodeMapValues(Arrays.asList(tagName),childNode).get(tagName) != null;
			if (isMatch) {
				result.appendChild(childNode);
			}
		}
		return result;
	}
	
	//are we processing a warned list
	private boolean isWarnedList() {
		return this.getClass().isAssignableFrom(WarnedListXMLMergeUtils.class);
	}
	
	//are we processing a daily list
	private boolean isDailyList() {
		return this.getClass().isAssignableFrom(DailyListXMLMergeUtils.class);
	}
	
	//are we processing a firmlist
	private boolean isFirmList() {
		return this.getClass().isAssignableFrom(FirmListXMLMergeUtils.class);
	}
	
	protected static boolean isBlankTextNode(final Node node) {
		return node != null && node.getNodeType() == Node.TEXT_NODE && EMPTY_STRING.equals(node.getTextContent().trim());
	}
	
	public String docToString(final Document doc) throws TransformerException  {		
		StringWriter write = new StringWriter();
		getTransformer().transform(new DOMSource(doc), new StreamResult(write));
		return write.getBuffer().toString();
	}

	private static Transformer getTransformer() throws TransformerConfigurationException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		return transformerFactory.newTransformer();
	}

	public static Map<String,String> getNodeMapValues(final List<String> nodeNames, final Node node) {
		return getNodeMapValues(new LinkedHashMap<String,String>(), nodeNames, node);
	}

	public static Map<String,String> getNodeMapValues(final Map<String,String> map, final List<String> nodeNames, final Node node) {
		Map<String,String> results = map;
		// Check ChildNodes
		if (node.hasChildNodes()) {
			for (int i =0; i < node.getChildNodes().getLength(); i++) {
				Node childNode = node.getChildNodes().item(i);
				results.putAll(getNodeMapValues(results, nodeNames, childNode));
			}
		}
		// Check this Node
		if (node.getNodeType() == Node.ELEMENT_NODE && nodeNames.contains(node.getNodeName()) && results.get(node.getNodeName()) == null) {
			// Add the Name and the value to the map of values to match on 
				results.put(node.getNodeName(), node.getTextContent());
		} 
		return results;
	}
	
	//----- Matching -----
	
	protected boolean isNodeMatchForMerge(final Node node1, final Node node2) {
		return compareNodes(getNodeMatchArray(), node1.getParentNode(), node2.getParentNode(), getMergeType()) == 0;
	}
		
	//----- Sorting / Positioning -----
	
	protected Node getChildNodeBeforeInsert(Node parentNode, Node childNodeToMerge) {
		Node node = null; 
		boolean isWithFixedDateToMerge = isWarnedList() && getNodeMapValues(Arrays.asList(WarnedListXMLMergeUtils.Tag.WITH_FIXED_DATE), childNodeToMerge).size() != 0;
		boolean isWithoutFixedDateToMerge = isWarnedList() && getNodeMapValues(Arrays.asList(WarnedListXMLMergeUtils.Tag.WITHOUT_FIXED_DATE), childNodeToMerge).size() != 0;
		
		for (int nodeNo = 0; nodeNo < parentNode.getChildNodes().getLength(); nodeNo++) {	
			Node childNode = parentNode.getChildNodes().item(nodeNo); 
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				if(getMergeType().equals(IWP)) {
					if (getNodePositionForSorting(childNode, childNodeToMerge) <= 0) {
						node = childNode;
						break;
					}
				} else if (isWarnedList()) {
					boolean isWithFixedDateChild = isWarnedList() && getNodeMapValues(Arrays.asList(WarnedListXMLMergeUtils.Tag.WITH_FIXED_DATE), childNode).size() != 0 ;
					boolean isWithoutFixedDateChild = isWarnedList() && getNodeMapValues(Arrays.asList(WarnedListXMLMergeUtils.Tag.WITHOUT_FIXED_DATE), childNode).size() != 0 ;
					
					// We've passed the point to merge, so merge here
					if ((isWithFixedDateToMerge && isWithoutFixedDateChild) || (((isWithFixedDateToMerge && isWithFixedDateChild) ||
							 (isWithoutFixedDateToMerge && isWithoutFixedDateChild)) && getNodePositionForSorting(childNode, childNodeToMerge) < 0)){
						node = childNode;
						break;
					}
				} else if (getNodePositionForSorting(childNode, childNodeToMerge) < 0) {
					node = childNode;
					break;
				}
			}
		}
		return node;
	}

	protected int getNodePositionForSorting(final Node node1, final Node node2) {
		return compareNodes(getNodePositionArray(), node1, node2, getMergeType());
	}

	protected int compareNode1Null(final String nodeName) {
		return GREATER_THAN;
	}

	protected int compareNode2Null(final String nodeName) {
		return LESS_THAN;
	}

	// ----- Common -----

	protected Node getParentNodeByType(final Node node, String nodeName) {
		Node result = null;
		if (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE && nodeName.equals(node.getNodeName())) {
				result = node;
				return result;
			}
			if (node.getParentNode() != null && Node.ELEMENT_NODE == node.getNodeType()) {
				result = getParentNodeByType(node.getParentNode(), nodeName);
			} 
		}
		return result; 
	}
	
	private int compareNodes(final String[] filterArray, final Node node1, final Node node2, String mergeType) {		
		List<String> nodesToMatchList = Arrays.asList(filterArray);
		Map<String,String> node1Map = getNodeMapValues(nodesToMatchList, node1);		
		Map<String,String> node2Map = getNodeMapValues(nodesToMatchList, node2);
		return compareNodes(node1Map, node2Map, mergeType, nodesToMatchList);
	}
	
	private int compareNodes(Map<String,String> map1, Map<String,String> map2, String mergeType, List<String> nodesToMatchList) {
		int nodeDiff = EQUAL_TO;
		// Check if we have nodes to match
		
			// Loop through the nodes and check the values match
			for (String nodeName : nodesToMatchList) {
				String node1Value = map1.get(nodeName);
				String node2Value = map2.get(nodeName);
				if (isNumeric(node1Value) && isNumeric(node2Value)) {
					nodeDiff = Integer.valueOf(node2Value).compareTo(Integer.valueOf(node1Value));
				} else if (node1Value != null && !"".equals(node1Value) && (node2Value == null || "".equals(node2Value))) {
					nodeDiff = compareNode2Null(nodeName);
				} else if ((node1Value == null || "".equals(node1Value)) && node2Value != null && !"".equals(node2Value)) {
					nodeDiff = compareNode1Null(nodeName);
				} else if ((node1Value == null || "".equals(node1Value)) && (node2Value == null || "".equals(node2Value) )) {
					nodeDiff = EQUAL_TO;
				} else {
					if(mergeType.equals(IWP)){
						if (node1Value.startsWith(COURT)&& node2Value.startsWith(COURT)) {
							String node1ValueNew = node1Value.replace(COURT, "");
							String node2ValueNew = node2Value.replace(COURT, "");
							//to stop a number format exception, if they have entered an invalid courtroom it won't get merged 
							//anyway so it doesn't really matter about the sorting
							if(isNumeric(node1ValueNew) && isNumeric(node2ValueNew)){
								nodeDiff = Integer.valueOf(node2ValueNew).compareTo(Integer.valueOf(node1ValueNew));
							} else {
								nodeDiff = node2Value.compareTo(node1Value);
							}
						} else if(node1Value.toLowerCase().startsWith(CROWN_COURT.toLowerCase()) && node2Value.toLowerCase().startsWith(CROWN_COURT.toLowerCase())) {
							String node1ValueNew = node1Value.replace(CROWN_COURT, "");
							String node2ValueNew = node2Value.replace(CROWN_COURT, "");
							//to stop a number format exception, if they have entered an invalid courtroom it won't get merged 
							//anyway so it doesn't really matter about the sorting
							if(isNumeric(node1ValueNew) && isNumeric(node2ValueNew)){
								nodeDiff = Integer.valueOf(node2ValueNew).compareTo(Integer.valueOf(node1ValueNew));
							} else {
								nodeDiff = node2Value.compareTo(node1Value);
							}
						} else {
							nodeDiff = node2Value.compareTo(node1Value);
						}
					} else if (isWarnedList() && AbstractListXMLMergeUtils.Tag.COURTHOUSE_NAME.equals(nodeName)) {
						node1Value = removeCourtHousePrefix(node1Value);
						node2Value = removeCourtHousePrefix(node2Value);
						nodeDiff = node2Value.compareTo(node1Value);
					} else {
						nodeDiff = node2Value.compareTo(node1Value);
					}
				}
				log.debug(nodeName+" - "+node1Value+ " vs "+ node2Value);
				if (nodeDiff != EQUAL_TO) {
					nodeDiff = nodeDiff < 0 ? LESS_THAN : GREATER_THAN; 
					break;
				}
			}
		
		return nodeDiff ;
	}	
	
    private String removeCourtHousePrefix(String nodeValue) {
    	if (nodeValue.startsWith(COURTHOUSE_NAMEPREFIX)) {
    		return nodeValue.replace(COURTHOUSE_NAMEPREFIX, EMPTY_STRING);
	    }
	    return nodeValue;
    }

	/**
	 * Is the value to compare a number?
	 * @param value String to check
	 * @return true if it's a number
	 */
	private static boolean isNumeric(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Returns the day in String format.
	 * @param calendar
	 * @return day
	 */
	public static String returnDay(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.MONDAY:
          return "Monday";
        case Calendar.TUESDAY:
        	return "Tuesday";
        case Calendar.WEDNESDAY:
        	return "Wednesday";
        case Calendar.THURSDAY:
        	return "Thursday";
        case Calendar.FRIDAY:
        	return "Friday";
        case Calendar.SATURDAY:
        	return "Saturday";
        case Calendar.SUNDAY:
        	return "Sunday";
        default:
        	return null;
        }
    }
	
	/**
	 * Returns the month in string forward.
	 * @param calendar
	 * @return month
	 */
	public static String returnMonth(Calendar calendar) {
        switch (calendar.get(Calendar.MONTH)) {
        case Calendar.JANUARY:
            return "January";
        case Calendar.FEBRUARY:
        	return "February";
        case Calendar.MARCH:
        	return "March";
        case Calendar.APRIL:
        	return "April";
        case Calendar.MAY:
        	return "May";
        case Calendar.JUNE:
        	return "June";
        case Calendar.JULY:
        	return "July";
        case Calendar.AUGUST:
        	return "August";
        case Calendar.SEPTEMBER:
        	return "September";
        case Calendar.OCTOBER:
        	return "October";
        case Calendar.NOVEMBER:
        	return "November";
        case Calendar.DECEMBER:
        	return "December";
        default:
        	return null;
        }
    }
}
