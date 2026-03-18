package uk.gov.courtservice.xhibit.courtlog.probation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBeanHelper2;
import uk.gov.courtservice.xhibit.business.ps.services.PSController;
import uk.gov.courtservice.xhibit.business.ps.services.PSControllerHome;
import uk.gov.courtservice.xhibit.courtlog.probation.exceptions.ProbationCourtLogProcessingException;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author qzd3k3
 */
public class ProbationCourtLogHelper {
    private Logger log = CSServices.getLogger(ProbationCourtLogHelper.class);

    private PSController controller;

    protected static final String XPATH_FOR_DATE = "/event/E30200_Long_Adjourn_Options/E30200_LAO_Date/text()";

    protected static final String EXPECTED_NODE = "/event/E30200_Long_Adjourn_Options/E30200_LAO_PSR_Required";

    protected static final String XPATH_FOR_LAO_TYPE = "/event/E30200_Long_Adjourn_Options/E30200_LAO_Type/text()";

    protected static final String EXPECTED_EVENT_TYPE = "E30200_Adjourned_for_Pre_Sentence_Report_to_date_on_enter_defendant's_name";

    protected static final String XPATH_FOR_DEFENDANT_ON_CASE_ID = "event/defendant_on_case_id/text()";
   
    public ProbationCourtLogHelper() {
        controller = (PSController) CSServices.getEJBServices().createRemoteSession(PSControllerHome.class);
    }

    public void processRequest(CourtLogSubscriptionValue value) throws IOException, SAXException,
            ParserConfigurationException, TransformerException, ProbationCourtLogProcessingException {
        log.info("processRequest - Begin");
        
        String logEntry = value.getCourtLogViewValue().getLogEntry();
        
        Integer courtRoomID = value.getCourtRoomId();
        Integer schHearingId = value.getScheduledHearingId();

        // Get Document Builder Factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document result = builder.parse(new ByteArrayInputStream(logEntry.getBytes()));
        org.w3c.dom.Element root = result.getDocumentElement();
        listNodes(root, "");

        if (result == null) {
            throw new ProbationCourtLogProcessingException("PSR_XXX",
                    "Could not create document for Court Log Entry : " + logEntry);
        }

        // check to see if the PSR is required - get he node value first
        Node psrRequired = XPathAPI.selectSingleNode(result, EXPECTED_NODE);
        if (psrRequired == null) {
            log.debug("PSR not required - no action as " + EXPECTED_NODE + " does not exist");
            return;
        }

        // get the value of the E30200_LAO_PSR_Required element
        String psrRequiredValue = psrRequired.getFirstChild().getNodeValue();
        if (!psrRequiredValue.equals("true")) {
            log.debug("PSR not required - no action.");
            return;
        }

        // Now get the defendant as a string value
        Node defendantNode = XPathAPI.selectSingleNode(result, XPATH_FOR_DEFENDANT_ON_CASE_ID);
        if (defendantNode == null) {
            throw new ProbationCourtLogProcessingException("PSR_XXX",
                    "Could not find the defendant-on-case using the XPath:" + XPATH_FOR_DEFENDANT_ON_CASE_ID);
        }

        String defendantValue = defendantNode.getNodeValue();
        if (defendantValue == null) {
            throw new ProbationCourtLogProcessingException("PSR_XXX",
                    "Could not find the defendant-on-case using the XPath:" + XPATH_FOR_DEFENDANT_ON_CASE_ID);
        }

        // Get the value of the Long Adjournment date element.  
        // This does not always exist.
        Date longAdjournDate = null;
        Node longAdjournDateNode = XPathAPI.selectSingleNode(result, XPATH_FOR_DATE);
        if (longAdjournDateNode == null) {
            log.debug(XPATH_FOR_DATE + " does not exist");
        } else {
            String longAdjournDateString = longAdjournDateNode.getNodeValue();
            if (longAdjournDateString != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    longAdjournDate = dateFormat.parse(longAdjournDateString);
                } catch (ParseException pe) {
                    log.error("processRequest: Unable to convert " + longAdjournDateString + " to a date!", pe);
                }
            }
        }
        
        Integer courtSiteId = value.getCourtSiteId();
        Integer caseId = value.getCourtLogViewValue().getCaseId();
        Integer defendantOnCaseId = new Integer(defendantValue);
        String courtSiteShortName = getCourtSiteShortName(courtSiteId);
        String courtRoomName;

        if (courtSiteShortName != null && courtSiteShortName.length() > 0) {
            courtRoomName = courtSiteShortName + " - " + getCourtRoomDisplayName(courtRoomID);
        } else {
            courtRoomName = getCourtRoomDisplayName(courtRoomID);
        }

        if (log.isDebugEnabled()) {
            log.debug("Create PSR With following values :: ");
            log.debug("PSR CL Helper CaseID :" + caseId);
            log.debug("PSR CL Helper CourtSiteID :" + courtSiteId);
            log.debug("PSR CL Helper DefOnCaseId :" + defendantOnCaseId);
            log.debug("PSR CL Helper CourtSiteShortName :" + courtSiteShortName);
            log.debug("PSR CL Helper CourtRoomName :" + courtRoomName);
            log.debug("PSR CL Helper Scheduled Hearing ID : " + schHearingId);
            log.debug("PSR CL Helper Long Adjournment Date : " + longAdjournDate);
        }
        controller.createPSRRequest(caseId, defendantOnCaseId, courtRoomName, schHearingId, longAdjournDate);
    }

    /**
     * Method to display values in a document
     *
     * @param node
     * @param indent
     */
    private static void listNodes(Node node, String indent) {
        System.out.println(indent + " " + node);
        NodeList list = node.getChildNodes();
        if (list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {
                listNodes(list.item(i), indent + " ");
            }
        }
    }

    /**
     * Method to return the Court Room name for the given Court Room ID
     *
     * @param courtRoomID
     * @return
     */
    private String getCourtRoomDisplayName(Integer courtRoomID) {
        return XhbCourtRoomBeanHelper2.findByPrimaryKey(courtRoomID).getDisplayName();
    }

    /**
     * Method to return a short name for a particular court site. Short name is
     * to differentiate between Court sites, e.g. parent/satellite
     *
     * @param courtSiteId
     *            Integer - the primary key
     * @return String the short name for a court site.
     */
    private String getCourtSiteShortName(Integer courtSiteId) {
        return XhbCourtSiteBeanHelper2.findByPrimaryKey(courtSiteId).getShortName();
    }
}
