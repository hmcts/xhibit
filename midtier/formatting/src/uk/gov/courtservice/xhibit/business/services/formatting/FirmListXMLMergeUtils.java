package  uk.gov.courtservice.xhibit.business.services.formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class FirmListXMLMergeUtils extends AbstractListXMLMergeUtils {
	
	private static final Logger log = Logger.getLogger(FirmListXMLMergeUtils.class);

	public static interface Attribute {
		static final String SITTING_DATE = "SittingDate";
	}
	
	public static interface Tag extends AbstractListXMLMergeUtils.Tag {
		static final String HEARING = "cs:Hearing";
		static final String HEARING_DATE = "cs:HearingDate";
		static final String RESERVE_LIST = "cs:ReserveList";
		static final String TOP_NODE = "cs:FirmList";
	}

	public FirmListXMLMergeUtils() throws XPathExpressionException {
		super(new String[] {"FirmList/CourtLists/CourtList/Sittings","FirmList/ReserveList/Hearing"});
	}
	
	@Override 
	public String[] getNodePositionArray() {
		return new String[] {Tag.COURTROOM_NUMBER, Tag.HEARING_DATE, Tag.SITTING_AT};
	}

	@Override
	protected int compareNode2Null(final String nodeName) {
		// Make sure null sittingAt gets placed at the end
		if (Tag.SITTING_AT.equals(nodeName)) {
			return GREATER_THAN;
		}
		return super.compareNode2Null(nodeName);
	}

	@Override
	protected int compareNode1Null(final String nodeName) {
		// Make sure null sittingAt gets placed at the end
		if (Tag.SITTING_AT.equals(nodeName)) {
			return LESS_THAN;
		}
		return super.compareNode1Null(nodeName);
	}

	protected String getUniqueKey(Node node) {
		return getNodeMapValues(Arrays.asList(new String[]{Tag.HEARING_DATE}),node).get(Tag.HEARING_DATE);  
	}
	
	protected String getSittingAttribute(Node node) {
		if(node.hasAttributes()) {
			return node.getAttributes().getNamedItem("SittingDate").getNodeValue();
		} else {
			return null;
		}
	}

	@Override
	protected void mergeNode(Document baseDocument, Node node, Node nodeToMerge) { 
		//i.e. is there a reserved node in cpp but nothing in xhibit
		if(isReservedNode(nodeToMerge) && Tag.TOP_NODE.equals(node.getNodeName()) ){ 
			Node childNodeToMerge = baseDocument.importNode(nodeToMerge, true);
			//append this reserved node to the cpp list
			node.appendChild(childNodeToMerge);
			
		}
		else if (isReservedNode(node)) {			
			Node reservedListNode = getParentNodeByType(node, Tag.RESERVE_LIST);
			Node childNodeToMerge = baseDocument.importNode(nodeToMerge, true);
			Node insertBeforeNode = getChildNodeBeforeInsert(node, childNodeToMerge);
			if (insertBeforeNode != null) {	
				insertBeforeNode = getParentNodeByType(insertBeforeNode, Tag.HEARING);
				reservedListNode.insertBefore(childNodeToMerge, insertBeforeNode);
			} else {
				reservedListNode.appendChild(childNodeToMerge);
			}
		} else {
			String nodeUniqueKey = getUniqueKey(node);
			Node courtListsNode = node.getParentNode().getParentNode();
			Node courtNodeToMerge = cloneSiblingNode(nodeToMerge, Tag.COURTHOUSE);
			Node sittingsNodeToMerge = nodeToMerge.cloneNode(false);
			for (int i = 0; i < nodeToMerge.getChildNodes().getLength(); i++) {
				Node childNodeToMerge = baseDocument.importNode(nodeToMerge.getChildNodes().item(i), true);
				if (!allowEmptyParentNodes() && isEmptyParentNode(childNodeToMerge)) {
					continue;
				} 
				String nodeToMergeUniqueKey = getUniqueKey(childNodeToMerge);
				boolean newNodeRequired = nodeUniqueKey != null && nodeToMergeUniqueKey != null && !nodeToMergeUniqueKey.equals(nodeUniqueKey);
				if ((nodeToMergeUniqueKey != null && nodeUniqueKey != nodeToMergeUniqueKey) || nodeUniqueKey == null) {
					nodeUniqueKey = nodeToMergeUniqueKey;
				}
				if (newNodeRequired) {
					appendNewCourtList(baseDocument, courtListsNode, courtNodeToMerge, sittingsNodeToMerge, nodeUniqueKey, childNodeToMerge);
				} else {
					Node insertBeforeNode = getChildNodeBeforeInsert(node, childNodeToMerge);
					if (insertBeforeNode != null) {
						node.insertBefore(childNodeToMerge, insertBeforeNode);
					} else {
						appendNode(node, childNodeToMerge);
					}
				}
			}
		}
	} 
	
	private void appendNewCourtList(Document baseDocument, Node courtListsNode, Node courtNodeToMerge,  Node sittingsNodeToMerge, String sittingDate, Node childNodeToMerge) {
		Node newCourtListNode = baseDocument.importNode(courtNodeToMerge.cloneNode(true), true);
		setNodeAttribute(newCourtListNode, Attribute.SITTING_DATE, sittingDate);
		// Create new <Sittings> Tag
		Node newSittingsListNode = baseDocument.importNode(sittingsNodeToMerge.cloneNode(true), true);
		// Attach the tags to the new structure
		Node insertBeforeNode = getCourtNodeNodeBeforeInsert(courtListsNode, newCourtListNode);
		if (insertBeforeNode != null) {
			courtListsNode.insertBefore(newCourtListNode, insertBeforeNode);
		} else {
			appendNode(courtListsNode, newCourtListNode);
		}
		appendNode(newCourtListNode, newSittingsListNode);
		appendNode(newSittingsListNode, childNodeToMerge);
	}
	
	protected Node getCourtNodeNodeBeforeInsert(Node courtListsNode, Node courtNodeToMerge) {
		Node result = null;
		String sittingDateToMerge = getSittingDateFromCourtNode(courtNodeToMerge);
		if (sittingDateToMerge != null) {
			for (int nodeNo = 0; nodeNo < courtListsNode.getChildNodes().getLength(); nodeNo++) {
				Node courtListNode = courtListsNode.getChildNodes().item(nodeNo);
				String sittingDate = getSittingDateFromCourtNode(courtListNode);
				if (sittingDate != null && sittingDateToMerge.compareTo(sittingDate) < 0) {
					sittingDateToMerge = sittingDate;
					result = courtListNode;
					break;
				}
			}
		}
		return result;
	}
	
	private String getSittingDateFromCourtNode(Node courtListNode) {
		String sittingDate = null;
		if (courtListNode != null && Node.ELEMENT_NODE == courtListNode.getNodeType()) {
			List<String> mapArray = Arrays.asList(new String[] {Tag.HEARING_DATE});
			sittingDate = getNodeAttribute(courtListNode, Attribute.SITTING_DATE);
			if (sittingDate == null) {
				Map<String,String> map = getNodeMapValues(mapArray,courtListNode);
				if (map.size() > 0) {
					sittingDate = map.get(Tag.HEARING_DATE);
				}
			}
		}
		return sittingDate;
	}
	
	@Override
	protected Node getChildNodeBeforeInsert(Node parentNode, Node childNodeToMerge) {
		if (isReservedNode(parentNode) && isReservedNode(childNodeToMerge)) {
			if (getNodePositionForSorting(parentNode, childNodeToMerge) < 0) {
				return parentNode;
			}
			return null;
		}
		return super.getChildNodeBeforeInsert(parentNode, childNodeToMerge);
	}
	
	@Override
	protected boolean isNodeMatchForMerge(final Node node1, final Node node2) {
		// Check if this is a reserved list
		boolean isMatch = isReservedNode(node1) && isReservedNode(node2);
		if (!isMatch) {
			// Check if the court matches
			isMatch = super.isNodeMatchForMerge(node1, node2);
			// Check the sitting dates of the two courts
			if (isMatch) {
				String nodeSittingDate = getSittingDateFromCourtNode(node1.getParentNode());
				String nodeToMergeSittingDate =	getSittingDateFromCourtNode(node2.getParentNode());	
				if (nodeSittingDate != null && nodeToMergeSittingDate != null) {
					isMatch = nodeToMergeSittingDate.equals(nodeSittingDate);
				}
			}
		}
		return isMatch;
	}	

	@Override
	protected boolean addNode(Document baseDocument, Node node, Node nodeToMerge) {
		
		//if to merge in is reserved return as that is done at the end
		if (isReservedNode(nodeToMerge)) {
			return false;
		}
//if the last xhibit is a reserved but we are trying to merge in an xhibit then we want to 
		// set node to a courtlist
		if(isReservedNode(node)) {
			node = node.getParentNode().getPreviousSibling();
		}
		
		Node courtListNode = getParentNodeByType(node, Tag.COURTLIST);
		Node courtListsNode = courtListNode != null ? courtListNode.getParentNode() : null;
		if(courtListsNode==null && node.getNodeType() == Node.ELEMENT_NODE && Tag.COURTLISTS.equals(node.getNodeName())){
			courtListsNode = node;
		}
		Node courtListToMerge = null;
		Node insertBeforeNode = null;
		if (courtListsNode != null) {
			courtListToMerge = getParentNodeToMerge(baseDocument, nodeToMerge);
			insertBeforeNode = getCourtNodeNodeBeforeInsert(courtListsNode, courtListToMerge);
		}
		//meaning either insertBeforeNode is not null or it is null i.e. it needs to be at the end
		//eg. would come back a null if xhibit had 14th for haverfordwest and cpp was 15th
		//for swansea
		if (courtListsNode != null) {
			courtListsNode.insertBefore(courtListToMerge, insertBeforeNode);
			return true;
			
		}
		else {
			return super.addNode(baseDocument, node, nodeToMerge);
		}
	}

	@Override
	protected boolean isEmptyParentNode(Node node) {
		boolean isEmpty = super.isEmptyParentNode(node);
		if (isEmpty) {
			// Check if this is a reservelist
			isEmpty = !isReservedNode(node);
		}
		return isEmpty;
	}

	private boolean isReservedNode(final Node node) {
		boolean isReserved = getParentNodeByType(node, Tag.RESERVE_LIST) != null;
		return isReserved;
	}
	
	public Document sortFirmCourtLists(Document doc)  {
		try {
		String pathToMatchOn = "FirmList/CourtLists/CourtList";
		XPathExpression rootNodeExpression = XPathFactory.newInstance().newXPath().compile(pathToMatchOn);
		
		
		List<Node> results = new ArrayList<Node>();
			NodeList nodeList = (NodeList) rootNodeExpression.evaluate(doc, XPathConstants.NODESET);
			for (int nodeNo = 0; nodeNo < nodeList.getLength(); nodeNo++) {
				//doc.removeChild(nodeList.item(nodeNo));
				results.add(nodeList.item(nodeNo));
			}
			Node courtListsNode = results.get(0).getParentNode();
			
			//remove the children 
			while(courtListsNode.hasChildNodes()) {
				courtListsNode.removeChild(courtListsNode.getFirstChild());
			}
			
			//sort the court list items
			results = sortCourtLists(results);
			
			//add all nodes back in order
			for(int i=0;i<results.size();i++) {
				//only add back in if not blank 
				Node childNodeToMerge = doc.importNode(results.get(i), true);
				if(!isEmptyParentNode(childNodeToMerge)) {
					appendNode(courtListsNode, childNodeToMerge);
				}
			}
			//no need to error now as it'll get thrown out during processing
		} catch(XPathExpressionException e) {
			log.error("unable to sort cpp document as no courtlist items found ");
		}
			return doc;
	}

	/**
	 * Sorts the court list items depending on their sitting date
	 * @param results original array
	 * @return the sorted results
	 */
	private List<Node> sortCourtLists(List<Node> results) {
		
		Comparator<Node> courtListComparator = new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				String o1Date = getSittingAttribute(o1);
				String o2Date = getSittingAttribute(o2);
				if(o1Date!=null && o2Date!=null) {
					return o1Date.compareTo(o2Date);
				} else if(o2Date == null) {
					return -1;
				} else {
					return 1;
				}
			}
		};
		
		Collections.sort(results, courtListComparator);	
		return results;
	}
}