package  uk.gov.courtservice.xhibit.business.services.formatting;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class WarnedListXMLMergeUtils extends AbstractListXMLMergeUtils {
	
	public static interface Tag extends AbstractListXMLMergeUtils.Tag {
		static final String CASE_NUMBER = "cs:CaseNumber";
		static final String FIXED_DATE = "cs:FixedDate";
		static final String FIXTURE = "cs:Fixture";
		static final String WITH_FIXED_DATE = "cs:WithFixedDate";
		static final String WITHOUT_FIXED_DATE = "cs:WithoutFixedDate";
	}
	
	public WarnedListXMLMergeUtils() throws XPathExpressionException {
		super(new String[] {
				"WarnedList/CourtLists/CourtList/WithFixedDate", 
				"WarnedList/CourtLists/CourtList/WithoutFixedDate"});
	}
	
	@Override 
	public String[] getNodePositionArray() {
		return new String[] {Tag.FIXED_DATE, Tag.CASE_NUMBER};
	}
	
	@Override
	protected int compareNode2Null(final String nodeName) {
		// Make sure null sittingAt gets placed at the end
		if (Tag.FIXED_DATE.equals(nodeName)) {
			return GREATER_THAN;
		}
		return super.compareNode2Null(nodeName);
	}

	@Override
	protected int compareNode1Null(final String nodeName) {
		// Make sure null sittingAt gets placed at the end
		if (Tag.FIXED_DATE.equals(nodeName)) {
			return LESS_THAN;
		}
		return super.compareNode1Null(nodeName);
	}
	
	@Override
	protected void mergeNode(Document baseDocument, Node node, Node nodeToMerge) {	
		Node childNodeToMerge = baseDocument.importNode(nodeToMerge, true);
		Node insertBeforeNode = getChildNodeBeforeInsert(node.getParentNode(), childNodeToMerge);
		if (insertBeforeNode != null) {
			List<String> validNodes = Arrays.asList(new String[] {Tag.WITH_FIXED_DATE, Tag.WITHOUT_FIXED_DATE});
			Map<String, String> validMap = getNodeMapValues(validNodes,childNodeToMerge);
			if (validMap.size() != 0) {
				node.getParentNode().insertBefore(childNodeToMerge, insertBeforeNode);
			}
		} else {
			appendNode(node.getParentNode(), childNodeToMerge);
		}
	}
}
