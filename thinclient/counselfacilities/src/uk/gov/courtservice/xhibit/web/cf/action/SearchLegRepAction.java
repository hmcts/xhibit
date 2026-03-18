package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.cf.services.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.business.cf.services.FindLegalRepresentativeTableRowModel;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAdvocateCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.SolicitorCriteria;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;

/**
 * <p>
 * Title: Default Action
 * </p>
 * <p>
 * Description: The default action for the application.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @version $Revision: 1.20 $
 * 
 */
public class SearchLegRepAction extends TerminalCookieAction {

    /**
     * The log4j logger
     */

    private static final Logger log = CSServices.getLogger(SearchLegRepAction.class);

    /**
     * Request parameters
     */
    public static final String LEGAL_REP_RADIO = "legalRepTypeRadio";

    public static final String BARRADIO = "BARRADIO";

    public static final String SOLRADIO = "SOLRADIO";

    public static final String LEGAL_REPRESENTATIVE_NAME = "fullNameText";

    public static final String FIRST_NAME = "firstNameText";

    public static final String SURNAME = "surnameText";

    public static final String CHAMBERS_NAME = "chambersNameText";

    public static final String TRX_CODE = "trxCode";

    /**
     * Response parameters
     */
    public static final String RESPONSE = "identifylegrep";

    public static final String COLLECTION_NAME = "legalRepCollection";

    /**
     * Empty default constructor
     */
    public SearchLegRepAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("home");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }
            String firstName = "";
            String surName = "";
            String legRepName = "";
            String chambersName = "";
            String trxCode = "";
            Collection matches = new Vector();

            BisRefControllerBeanBusinessDelegate bisRefdelegate = BisRefControllerBeanBusinessDelegate.DelegateFactory
                    .getInstance();

            /**
             * Get Form Fields data from session
             */
            // String radioButton = ( String )
            // actionEnvironment.getSessionParameter( (String )
            // actionEnvironment.getRequestParameter( LEGAL_REP_RADIO ) );
            Enumeration enumeration = actionEnvironment.getRequestParameterNames();
            log.debug("start listing request parameter names");
            while (enumeration.hasMoreElements()) {
                log.debug("requestParameterName " + enumeration.nextElement());
                log.debug("---");
            }

            log.debug("end listing request parameter names");

            String radioButton = (String) actionEnvironment.getRequestParameter(LEGAL_REP_RADIO);

            if (radioButton.equals(BARRADIO)) {
                firstName = (String) actionEnvironment.getRequestParameter(FIRST_NAME);
                surName = (String) actionEnvironment.getRequestParameter(SURNAME);
            } else if (radioButton.equals(SOLRADIO)) {
                legRepName = (String) actionEnvironment.getRequestParameter(LEGAL_REPRESENTATIVE_NAME);
                chambersName = (String) actionEnvironment.getRequestParameter(CHAMBERS_NAME);
            }

            /*
             * No longer required due to PR54873 chambersName = (String )
             * actionEnvironment.getRequestParameter( CHAMBERS_NAME ) ;
             */
            trxCode = (String) actionEnvironment.getRequestParameter(TRX_CODE);

            /**
             * Get Collection of FindLegalRepresentativeTableRowModel and store
             * it in the session
             */
            if (radioButton.equals(BARRADIO)) {
                RefAdvocateCriteria criteria = new RefAdvocateCriteria();

                if (firstName != null && !("".equals(firstName)))
                    criteria.setFirstName(firstName + "%");
                if (surName != null && !("".equals(surName)))
                    criteria.setSurname(surName + "%");

                /*
                 * No longer required due to PR54873 if ( chambersName != null &&
                 * !("".equals( chambersName ) ) ) criteria.setChamberFirmName(
                 * chambersName + "%" );
                 */

                // PR55541 requires courtid to be set in criteria
                criteria.setCourtId(String.valueOf(getCourtId(actionEnvironment)));

                criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);

                try {
                    Iterator iter = bisRefdelegate.findAdvocates(criteria).iterator();

                    while (iter.hasNext()) {
                        // Only call accessor methods on the
                        // RefAdvocateComplexValue
                        final RefAdvocateComplexValue item = (RefAdvocateComplexValue) iter.next();

                        FindLegalRepresentativeTableRowModel trm = new FindLegalRepresentativeTableRowModel();
                        trm.setLegalRepId(item.getLegalRepId());
                        trm.setFirstName(item.getFirstName());
                        trm.setSurname(item.getSurname());
                        trm.setFullName(CounselFacilitiesHelper.getSurnameFirstName(item.getFirstName(), item
                                .getSurname()));
                        trm.setChambersName(item.getFirmName());
                        trm.setAddressLine01(item.getAddress1());
                        trm.setAddressLine02(item.getAddress2());
                        trm.setTown(item.getTown());
                        trm.setCounty(item.getCounty());
                        trm.setPostCode(item.getPostcode());
                        trm.setChambersId(item.getRefChamberId());
                        trm.setLegalRepType(radioButton);

                        matches.add(trm);
                    }
                } catch (BisRefControllerException brce) {
                    log.error("Exception : " + brce + ", method name : internalPerformAction");
                }
            }

            if (radioButton.equals(SOLRADIO)) {
                SolicitorCriteria criteria = new SolicitorCriteria();
                criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
                if (legRepName != null && !("".equals(legRepName)))
                    criteria.setCrestSolicitorName(legRepName + "%");
                if (chambersName != null && !("".equals(chambersName)))
                    criteria.setSolicitorFirmName(chambersName + "%");

                // PR55541 requires courtid to be set in criteria
                criteria.setCourtId(String.valueOf(getCourtId(actionEnvironment)));

                try {
                    Collection col = bisRefdelegate.findSolicitors(criteria);
                    Iterator iter = col.iterator();

                    while (iter.hasNext()) {
                        SolicitorComplexValue item = (SolicitorComplexValue) iter.next();
                        RefSolicitorFirmComplexValue firm = item.getFirm();

                        FindLegalRepresentativeTableRowModel trm = new FindLegalRepresentativeTableRowModel();
                        trm.setLegalRepId(item.getLegalRepId());
                        trm.setFullName(item.getCrestSolicitorName());
                        trm.setChambersName(firm.getSolicitorFirmName());
                        trm.setAddressLine01(firm.getAddress1());
                        trm.setAddressLine02(firm.getAddress2());
                        trm.setTown(firm.getTown());
                        trm.setCounty(firm.getCounty());
                        trm.setPostCode(firm.getPostcode());
                        trm.setChambersId(item.getFirmId());
                        trm.setLegalRepType(radioButton);

                        matches.add(trm);
                    }
                } catch (BisRefControllerException brce) {
                    log.error("Exception : " + brce + ", method name : internalPerformAction");
                }
            }

            /**
             * Set the session with the Collection
             */
            String id = KeyFactory.getInstance().nextKey();

            actionEnvironment.setSessionParameter(COLLECTION_NAME, matches);
            actionEnvironment.setRequestParameter(LEGAL_REP_RADIO, radioButton);
            actionEnvironment.setRequestParameter(LEGAL_REPRESENTATIVE_NAME, legRepName);
            actionEnvironment.setRequestParameter(FIRST_NAME, firstName);
            actionEnvironment.setRequestParameter(SURNAME, surName);
            actionEnvironment.setRequestParameter(CHAMBERS_NAME, chambersName);
            actionEnvironment.setRequestParameter(TRX_CODE, trxCode);

            actionEnvironment.setResponseName(RESPONSE);
        }
    }

}
