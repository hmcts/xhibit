package uk.gov.courtservice.xhibit.business.services.publicnotice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XMLServices;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DefinitivePublicNoticeStatusValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: Based on CourtLogSubscription Event Type it's the responsiblity of
 * this class to Manipulate the PublicNotices if Required.
 * </p>
 * 
 * <p>
 * Description:This class will manipulate the selection so that the notices
 * being displayed are being correct .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Pat Fox
 * @created 17 February 2003
 */
public class PublicNoticeSelectionManipulator implements PublicNoticeConstants {

    private static final Logger log = CSServices.getLogger(PublicNoticeSelectionManipulator.class);

    private static PublicNoticeXmlHelper m_publicNoticeXmlHelper = PublicNoticeXmlHelper.getInstance();

    // Constants for Prosecution Case Court Log event - Bug 53354
    private static final int EVENT_ID_TRIAL_PROSECUTION_CASE = 20903;

    private static final int EVENT_ID_TRIAL_PROSECUTION_CASE_TV_LINK = 2090301;

    private static final int EVENT_ID_TRIAL_PROSECUTION_CASE_VIDEO_LINK = 2090302;

    private static final String XPATH_EVENT_ID_20903 = "event/E20903_Prosecution_Case_Options/E20903_PCO_Type/text()";

    protected static final String XPATH_EVENT_ID_20903_OPTION_TV_LINK = "E20903_Prosecution_Case_TV_Link_in_Progress";

    protected static final String XPATH_EVENT_ID_20903_OPTION_VIDEO_LINK = "E20903_Prosecution_Case_Video_Being_Played";

    // End of constants for Prosecution Case Court Log event - Bug 53354

    // Constants for Respondent Case Court Log event (Appeal) - Bug 54926 &
    // 54987
    private static final int EVENT_ID_APPEAL_RESPONDENT_CASE = 20602;

    private static final int EVENT_ID_APPEAL_RESPONDENT_CASE_TV_LINK = 2060201;

    private static final int EVENT_ID_APPEAL_RESPONDENT_CASE_VIDEO_LINK = 2060202;

    private static final String XPATH_EVENT_ID_20602 = "event/E20602_Respondent_Case_Opened/E20602_RCO_Type/text()";

    private static final String XPATH_EVENT_ID_20602_OPTION_TV_LINK = "E20602_TV_Link_In_Progress";

    private static final String XPATH_EVENT_ID_20602_OPTION_VIDEO_LINK = "E20602_Video_Being_Played";

    private static final String XPATH_EVENT_ID_20602_OPTION_NONE = "E20602_Respondent_Case_Opened";

    // End of constants for Respondent Case Court Log event (Appeal) - Bug
    // 54926
    // & 54987

    /**
     * Constructor for the PublicNoticeWorkFlow object
     */
    private PublicNoticeSelectionManipulator() {
    }

    /**
     * This method manipulates the Configured Public notices Activation Status
     * based on the array of Displayable Public Notices passed in. The
     * Manipulation actions are configured through the XML file.
     * 
     * @param courtLogSubscriptionValue
     *            Description of the Parameter
     * @return Description of the Return Value
     * @throws PublicNoticeException
     *             Description of the Exception
     */
    public static boolean manipulateSelection(CourtLogSubscriptionValue courtLogSubscriptionValue)
            throws PublicNoticeException {

        if (log.isDebugEnabled()) {
            log.debug("10 : Entering manipulateSelection courtLogSubscriptionValue.getCourtRoomId() "
                    + courtLogSubscriptionValue.getCourtRoomId());
        }

        // get the court court room Id
        Integer l_xhbCourtRoomId = courtLogSubscriptionValue.getCourtRoomId();

        // get the actual event type id
        Integer l_xhbEventType = getCourtLogEventType(courtLogSubscriptionValue);

        // get which definitive notice to update
        if (log.isDebugEnabled()) {
            log.debug(" 20 : l_xhbCourtRoomId = " + l_xhbCourtRoomId + " l_xhbEventType =" + l_xhbEventType);
        }

        boolean l_updateCarriedOut = manipulateSelection(l_xhbCourtRoomId, l_xhbEventType);

        log.debug("30 : Exiting manipulateSelection");

        return l_updateCarriedOut;
    }

    /**
     * This gets the Manipulator map from the XMLHelper . This Hash contains
     * lists of { definitive notice ID, Activation Status } that is keyed on the
     * specified courtlog event. If Courtlog Event Type is not contained in the
     * keys of the hashmap then no manipulation is carried out. If it the Court
     * Log Event Type is within the keys then the manipulation is carried out
     * based on the list of statusChange objects returned for that key.
     * 
     * @param l_xhbCourtRoomId
     *            Description of the Parameter
     * @param l_xhbEventType
     *            Description of the Parameter
     * @return Description of the Return Value
     * @throws PublicNoticeException
     *             Description of the Exception
     */
    private static boolean manipulateSelection(Integer l_xhbCourtRoomId, Integer l_xhbEventType)
            throws PublicNoticeException {

        log.debug("10 : Entering manipulateSelection l_xhbCourtRoomId " + l_xhbCourtRoomId.intValue());

        // get the HashMap that contains all the status changes for the
        // courtlogEventType

        HashMap theManipulatorMap = m_publicNoticeXmlHelper.getManipulatorMap();

        // If there is not list for eventType don't do anything
        if (theManipulatorMap.containsKey(l_xhbEventType)) {

            // Retrieve the list of status changes and carry each one out.
            ArrayList theListOfStatusChanges = (ArrayList) theManipulatorMap.get(l_xhbEventType);
            Iterator theListOfStatusChangesIterator = theListOfStatusChanges.iterator();

            if (log.isDebugEnabled()) {
                log.debug("11 : Number of Status changes :" + theListOfStatusChanges.size());
            }

            while (theListOfStatusChangesIterator.hasNext()) {

                DefinitivePublicNoticeStatusValue l_statusChangeValue = (DefinitivePublicNoticeStatusValue) theListOfStatusChangesIterator
                        .next();

                log.debug("15 : Changing status of  DefPN :" + l_statusChangeValue.getDefinitivePublicNoticeId()
                        + " to " + l_statusChangeValue.getIsActive());

                updateActivationStatusOnConfiguredPublicNotice(l_xhbCourtRoomId, l_statusChangeValue);

            }

            log.debug("39 : Exiting manipulateSelection with update");

            // updated
            return true;
        }

        if (log.isDebugEnabled()) {
            log.debug("40 : Exiting manipulateSelection without update");
        }
        // did not need to update
        return false;
    }

    private static void updateActivationStatusOnConfiguredPublicNotice(Integer l_xhbCourtRoomId,
            DefinitivePublicNoticeStatusValue l_statusChangeValue) throws PublicNoticeException {
        PublicNoticeMaintainer.updateActiveStatus(l_xhbCourtRoomId, l_statusChangeValue, false);
    }

    /*
     * This method allows any events that have a number of possible option types
     * within the events XML entry to be determined and a unique event id
     * assigned for the purposes of showing the correct public notices
     */
    private static Integer getCourtLogEventType(CourtLogSubscriptionValue CLSValue) {
        String methodName = "getCourtLogEventType - ";
        log.debug(methodName + "entry");

        int eventType = CLSValue.getCourtLogViewValue().getEventType().intValue();
        log.debug(methodName + "eventType: " + eventType);

        // Manipulate any events that have more than one possible option type
        if (eventType == EVENT_ID_TRIAL_PROSECUTION_CASE) {

            XMLServices xmlServices = XMLServicesImpl.getInstance();

            // Obtain the option type using the XPath functionality in the
            // framework
            String optionType = xmlServices.getXpathValueFromXmlString(CLSValue.getCourtLogViewValue().getLogEntry(),
                    XPATH_EVENT_ID_20903);
            log.debug(methodName + "optionType: " + optionType);

            if (optionType.equals(XPATH_EVENT_ID_20903_OPTION_VIDEO_LINK)) {
                eventType = EVENT_ID_TRIAL_PROSECUTION_CASE_VIDEO_LINK;
            } else if (optionType.equals(XPATH_EVENT_ID_20903_OPTION_TV_LINK)) {
                eventType = EVENT_ID_TRIAL_PROSECUTION_CASE_TV_LINK;
            } else {
                log.warn(methodName + " Unexpected option type returned : " + optionType);
            }
        }
        // Bug 54926 & 54987
        if (eventType == EVENT_ID_APPEAL_RESPONDENT_CASE) {

            XMLServices xmlServices = XMLServicesImpl.getInstance();

            // Obtain the option type using the XPath functionality in the
            // framework
            String optionType = xmlServices.getXpathValueFromXmlString(CLSValue.getCourtLogViewValue().getLogEntry(),
                    XPATH_EVENT_ID_20602);
            log.debug(methodName + "optionType: " + optionType);

            if (optionType.equals(XPATH_EVENT_ID_20602_OPTION_VIDEO_LINK)) {
                eventType = EVENT_ID_APPEAL_RESPONDENT_CASE_VIDEO_LINK;
            } else if (optionType.equals(XPATH_EVENT_ID_20602_OPTION_TV_LINK)) {
                eventType = EVENT_ID_APPEAL_RESPONDENT_CASE_TV_LINK;
            } else if (optionType.equals(XPATH_EVENT_ID_20602_OPTION_NONE)) {
                // No public notice is required in this case
            } else {
                log.warn(methodName + " Unexpected option type returned : " + optionType);
            }
        }

        log.debug(methodName + "exit - eventType: " + eventType);
        return new Integer(eventType);
    }
}