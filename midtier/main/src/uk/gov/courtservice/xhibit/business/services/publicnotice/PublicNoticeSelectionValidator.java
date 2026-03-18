package uk.gov.courtservice.xhibit.business.services.publicnotice;

import java.util.Properties;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;

/**
 * <p>
 * Title: Takes care of Validation rules for the Public Notices
 * </p>
 * <p>
 * Description: Currently there is only one rule to do with the number of
 * configured Displayable public notices that can be selected to be at
 * Activation Level "true" at once. But in the future any other rules can be
 * plugged in here.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author: Pat Fox.
 */

public class PublicNoticeSelectionValidator implements PublicNoticeConstants {

    private static final Logger log = CSServices.getLogger(PublicNoticeSelectionValidator.class);

    private static Properties m_configProperties;

    static {
        // loads the properties
        getPublicNoticeProperties();
    }

    /**
     * Constructor for the PublicNoticeWorkFlow object
     */
    private PublicNoticeSelectionValidator() {
    }

    /**
     * This method will check against a set of rules that specify which Public
     * notices can be selected at once. If the rule is broken then
     * PublicNoticeInvalidSelectionException is thrown.
     * 
     * @param displayablePublicNoticeValues
     *            Description of the Parameter
     * @exception PublicNoticeInvalidSelectionException
     *                Description of the Exception
     */
    public static void validateSelection(DisplayablePublicNoticeValue[] displayablePublicNoticeValues)
            throws PublicNoticeInvalidSelectionException {
        int l_maximumNoticesAllowedActive = Integer.parseInt(m_configProperties.getProperty(MAX_NUMBER_ALLOWED_ACTIVE));

        if (log.isDebugEnabled()) {
            log.debug("10 : Entering validateSelection  " + displayablePublicNoticeValues.length);
        }

        int l_numberSelectedAsAcive = 0;

        // walk through and see if number selected is greater the
        for (int i = (displayablePublicNoticeValues.length - 1); i >= 0; i--) {

            if (displayablePublicNoticeValues[i].getIsActive() == true) {
                l_numberSelectedAsAcive++;
            }
        }

        // if number exceeded throw an exception.
        if (l_numberSelectedAsAcive > l_maximumNoticesAllowedActive) {

            PublicNoticeInvalidSelectionException l_publicNoticeInvalidSelectionException = new PublicNoticeInvalidSelectionException(
                    new Integer(l_maximumNoticesAllowedActive), "Too Many PublicNotices Selected"
                            + l_numberSelectedAsAcive);

            CSServices.getDefaultErrorHandler().handleError(l_publicNoticeInvalidSelectionException,
                    PublicNoticeSelectionValidator.class);
            throw l_publicNoticeInvalidSelectionException;
        }

        if (log.isDebugEnabled()) {
            log.debug("50 : Exiting validateSelection number selected" + l_numberSelectedAsAcive);
        }
    }

    /**
     * Gets the publicNoticeProperties attribute of the
     * PublicNoticeSelectionValidator object
     */
    private static void getPublicNoticeProperties() {

        if (log.isDebugEnabled()) {
            log.debug("Entering getPublicNoticeProperties() ");
        }

        try {
            m_configProperties = CSServices.getConfigServices().getProperties(PROPERTIESFILENAME);
        } catch (CSConfigurationException e) {
            CSServices.getDefaultErrorHandler().handleError(e, PublicNoticeSelectionValidator.class,
                    "Cannot find properties file: " + PROPERTIESFILENAME);
            throw new EJBException("Xhibit_Messaging.Unable_To_Locate_Properties" + e.getMessage(), e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Exiting getPublicNoticeProperties() ");
        }
    }

}
