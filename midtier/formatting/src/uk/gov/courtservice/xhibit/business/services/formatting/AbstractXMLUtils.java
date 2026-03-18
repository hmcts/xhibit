package uk.gov.courtservice.xhibit.business.services.formatting;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public abstract class AbstractXMLUtils {
	
	public static DocumentBuilder getDocBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		return docBuilderFactory.newDocumentBuilder();
	}

}