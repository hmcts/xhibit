package uk.gov.courtservice.xhibit.test.business.services.publicnotice;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeInvalidSelectionException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeSelectionValidator;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;

/**
 * <p>
 * Title: Test Class for testing the Validator Component.
 * </p>
 * <p>
 * Description: see title
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author tzxbys
 */
public class PublicNoticeSelectionValidatorTest extends TestCase {
    /**
     * Logger log
     */
    Logger log = CSServices.getLogger(PublicNoticeSelectionValidatorTest.class);

    /**
     * Constructor for the PublicNoticeControllerWorkFlowTest object
     * 
     * @param s
     *            Description of the Parameter
     */
    public PublicNoticeSelectionValidatorTest(String s) {
        super(s);
    }

    /**
     * The JUnit setup method
     * 
     * @throws Exception
     *             Description of the Exception
     */
    protected void setUp() throws Exception {

    }

    /**
     * The teardown method for JUnit
     * 
     * @throws Exception
     *             Description of the Exception
     */
    protected void tearDown() throws Exception {

    }

    /**
     * Validates that Activation level( isActive Flag) is correct for the Array
     * of Displayable Public Notices.
     * 
     * 1. Creates a set of Public notices
     * 
     * 2. set the Activation level to true for five of them
     * 
     * 3. invokes the validator
     * 
     * 4. should not cause an exception.
     * 
     * @throws Exception
     *             Description of the Exception
     */
    public void testValidateSelection() throws Exception {

        log.debug("Entering Test Method testValidateSelection() ");

        DisplayablePublicNoticeValue[] l_displayablePublicNoticeValue = new DisplayablePublicNoticeValue[10];

        for (int i = 0; i < 10; i++) {

            l_displayablePublicNoticeValue[i] = new DisplayablePublicNoticeValue();
            l_displayablePublicNoticeValue[i].setIsActive(false);
        }

        l_displayablePublicNoticeValue[1].setIsActive(true);
        l_displayablePublicNoticeValue[2].setIsActive(true);
        l_displayablePublicNoticeValue[3].setIsActive(true);
        l_displayablePublicNoticeValue[4].setIsActive(true);
        l_displayablePublicNoticeValue[5].setIsActive(true);

        PublicNoticeSelectionValidator
                .validateSelection(l_displayablePublicNoticeValue);

        log.debug("Exiting Test Method testValidateSelection() ....");

    }

    /**
     * testValidateSelectionException Validates that Activation level( isActive
     * Flag) is correct for the Array of Displayable Public Notices.
     * 
     * 1. Creates a set of Public notices
     * 
     * 2. set the Activation level to true for Six of them
     * 
     * 3. invokes the validator
     * 
     * 4. should cause a PublicNoticeInvalidSelectException . Why? currently
     * property file is Set to Allow a maximum of 5 Displayable public Notices
     * for each Court Room.
     * 
     * @throws Exception -
     */
    public void testValidateSelectionException() throws Exception {
        log.debug("Entering Test Method testValidateSelectionException() ");

        DisplayablePublicNoticeValue[] l_displayablePublicNoticeValue = new DisplayablePublicNoticeValue[10];

        for (int i = 0; i < 10; i++) {

            l_displayablePublicNoticeValue[i] = new DisplayablePublicNoticeValue();
            l_displayablePublicNoticeValue[i].setIsActive(false);
        }

        l_displayablePublicNoticeValue[1].setIsActive(true);
        l_displayablePublicNoticeValue[2].setIsActive(true);
        l_displayablePublicNoticeValue[3].setIsActive(true);
        l_displayablePublicNoticeValue[4].setIsActive(true);
        l_displayablePublicNoticeValue[5].setIsActive(true);
        l_displayablePublicNoticeValue[6].setIsActive(true);

        try {
            PublicNoticeSelectionValidator
                    .validateSelection(l_displayablePublicNoticeValue);
            fail("Should have thrown an Exception");
        } catch (PublicNoticeInvalidSelectionException ex) {

        }

        log.debug("Exiting Test Method testValidateSelectionException() ....");
    }

}