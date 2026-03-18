package uk.gov.courtservice.xhibit.business.services.formatting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public abstract class AbstractListXMLMergeUtils extends AbstractXMLMergeUtils {		
	
	private static final String MERGE_TYPE = "LISTS";
	public static interface Tag {
		static final String LIST_HEADER = "cs:ListHeader";
		static final String CROWN_COURT = "cs:CrownCourt";
		static final String START_DATE = "cs:StartDate";
		static final String END_DATE = "cs:EndDate";
		static final String COURTHOUSE_CODE = "cs:CourtHouseCode";
		static final String COURTHOUSE_NAME = "cs:CourtHouseName";
		static final String COURTHOUSE = "cs:CourtHouse";
		static final String COURTLISTS = "cs:CourtLists";
		static final String COURTLIST = "cs:CourtList";
		static final String COURTROOM_NUMBER = "cs:CourtRoomNumber";
		static final String SITTINGS = "cs:Sittings";
		static final String SITTING = "cs:Sitting";
		static final String SITTING_AT = "cs:SittingAt";
	}
	private static final String DATE_FORMAT_MASK = "yy-MM-dd";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_MASK);
	
	public AbstractListXMLMergeUtils(String[] rootNodes) throws XPathExpressionException {
		super(rootNodes);
	}

	@Override
	public String getMergeType () {
		return MERGE_TYPE;
	}
	
	@Override 
	public String[] getNodeMatchArray() {
		return new String[] {Tag.COURTHOUSE_CODE, Tag.COURTHOUSE_NAME};
	}

	@Override 
	public String[] getNodePositionArray() {
		return new String[] {Tag.COURTROOM_NUMBER, Tag.SITTING_AT};
	}

	public Date getListStartDateFromDocument(final Document document) {
		Date result = null;
		if (document != null) {
			NodeList listHeaderNodes = document.getElementsByTagName(Tag.LIST_HEADER);
			if (listHeaderNodes != null && listHeaderNodes.getLength() > 0) {
				List<String> nodes = Arrays.asList(new String[] {Tag.START_DATE});
				Map<String,String> nodeMap = getNodeMapValues(nodes, listHeaderNodes.item(0));
				String dateAsString = nodeMap.get(Tag.START_DATE); 
				result = parseDate(dateAsString);
			}
		}
		return result;
	}
	
	public Date getListEndDateFromDocument(final Document document) {
		Date result = null;
		if (document != null) {
			NodeList listHeaderNodes = document.getElementsByTagName(Tag.LIST_HEADER);
			if (listHeaderNodes != null && listHeaderNodes.getLength() > 0) {
				List<String> nodes = Arrays.asList(new String[] {Tag.END_DATE});
				Map<String,String> nodeMap = getNodeMapValues(nodes, listHeaderNodes.item(0));
				String dateAsString = nodeMap.get(Tag.END_DATE);
				result = parseDate(dateAsString);
			}
		}
		return result;
	}
	
	public String getCourtHouseCodeFromDocument(final Document document) {
		String result = null;
		if (document != null) {
			NodeList listHeaderNodes = document.getElementsByTagName(Tag.CROWN_COURT);
			if (listHeaderNodes != null && listHeaderNodes.getLength() > 0) {
				List<String> nodes = Arrays.asList(new String[] {Tag.COURTHOUSE_CODE});
				Map<String,String> nodeMap = getNodeMapValues(nodes, listHeaderNodes.item(0));
				result = nodeMap.get(Tag.COURTHOUSE_CODE);
				
			}
		}
		return result;
	}
	
	
	private Date parseDate(String dateAsString) {
		try {
			return dateFormat.parse(dateAsString);
		} catch (ParseException e) {
			return null;
		}
	}
}
