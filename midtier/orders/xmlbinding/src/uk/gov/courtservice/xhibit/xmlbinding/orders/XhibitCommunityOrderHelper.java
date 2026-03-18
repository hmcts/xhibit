package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourt;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourtHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreach;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBeanHelper2;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Breach;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.COCommittingCourt;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CourtHouse;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OriginatingCourt;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.XHIBITCommunityOrderStructure;

/**
 * <p/> Title: Helper class that populates the Castor bound java-XML object for
 * the XHIBITCommunityOrderStructure
 * </p>
 * <p/> Description:
 * </p>
 * <p/> Populates the XHIBITCommunityOrderStructure for 'Community Punishment
 * and Rehabilitation', 'Community Rehabilitation' and 'Community Punishment'
 * orders.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bob Boothby
 * @version $Revision: 1.35 $
 */
//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class XhibitCommunityOrderHelper {
    // set up logger
    private static final Logger log = CSServices.getLogger(XhibitCommunityOrderHelper.class);

    // Assuming we can share instance of CourtHome across all instances.
    private static final RefCourtHome refCourtHome;

    // Statically initialise the required home interface.
    // Should be done for all home interfaces used.

    static {
        refCourtHome = (RefCourtHome) CSServices.getServiceLocator().getLocalHome(RefCourtHome.class);
    }

    /**
     * Utility method to populate an XHIBITCommunityOrderStructure from the rest
     * of the Xhibit entities.
     * 
     * @param cos
     *            The XHIBITCommunityOrderStructure to be populated (pass by
     *            reference).
     * @param docEntity
     *            The defendant on case EB for which to populate the order.
     * @param rehab
     *            Whether this Community order incorporates rehabilitation.
     * @param typeCode
     *            The type code of the order.
     * @throws OrderXMLException
     *             When there is a problem in population.
     */

    public static void populateXhibitCommunityOrder(XHIBITCommunityOrderStructure cos, DefendantOnCase docEntity,
            boolean punishment, boolean rehab, String typeCode) throws OrderXMLException {
        populateCaseDetails(cos, docEntity, typeCode);
    }

    /**
     * Populate the community order structure with all relevant case details
     * 
     * @param cos
     *            the community order structure
     * @param docEntity
     *            the defendant on case
     * @param typeCode
     *            the order type
     * @param caseEntity
     *            the case
     * @throws OrderXMLException
     */
    private static void populateCaseDetails(XHIBITCommunityOrderStructure cos, DefendantOnCase docEntity,
            String typeCode) throws OrderXMLException {
        // get case details for defendant
        Case caseEntity = docEntity.getCaze();

        // There will be at most 1 breach
        Collection breachCollection = XhbBreachBeanHelper2.findByCourtIdOrderCodeDefendantOnCaseId(caseEntity
                .getCourtId(), typeCode, docEntity.getDefendantOnCaseId());
        Iterator breaches = breachCollection.iterator();
        if (breaches.hasNext()) {
            populateDetailsForBreach(cos, docEntity, caseEntity, (XhbBreach) breaches.next());
        } else {
            populateDetailsForNoBreach(cos, docEntity, caseEntity, typeCode);
        }
    }

    /**
     * Populate details depending on whether the case is breach or not
     * 
     * @param cos
     *            the community order structure
     * @param docEntity
     *            the defendant on case
     * @param caseEntity
     *            the case
     * @param doo
     *            the defendant on offence
     * @param offenceLength
     *            the length of the offence array
     * @param breaches
     *            the list of breaches
     * @param breachExist
     *            if a breach exists
     * @throws OrderXMLException
     */
    private static void populateDetailsForBreach(XHIBITCommunityOrderStructure cos, DefendantOnCase docEntity,
            Case caseEntity, XhbBreach xhbBreach) throws OrderXMLException {

        Breach breach = cos.getBreach();

        // create the originating court if it does not exist
        OriginatingCourt coOriginatingCourt = breach.getOriginatingCourt();
        if (coOriginatingCourt == null) {
            coOriginatingCourt = new OriginatingCourt();
            breach.setOriginatingCourt(coOriginatingCourt);
        }

        populateOriginatingCourt(coOriginatingCourt, xhbBreach);

        // set breach as selected
        breach.setSelected(true);

        // retrieve information on committing court.
        try {
            CourtHouseHelper.populateCourtHouse(coOriginatingCourt.getCourtHouse(), refCourtHome
                    .findByPrimaryKey(caseEntity.getRefCourtId()));
        } catch (FinderException ex) {
            // This is now okay as we allow the user to select
            // the committing court if none is found
            log.info("REF COURT NOT FOUND " + caseEntity.getRefCourtId());

        }
    }

    /**
     * Process the details if the case is not a breach
     * 
     * @param cos
     *            the community order structure
     * @param docEntity
     *            the defendant on case
     * @param caseEntity
     *            the case
     * @throws OrderXMLException
     */
    private static void populateDetailsForNoBreach(XHIBITCommunityOrderStructure cos, DefendantOnCase docEntity,
            Case caseEntity, String typeCode) throws OrderXMLException {
        // Set up committing court i.e. no breaches exist.
        COCommittingCourt coCommittingCourt = cos.getCOCommittingCourt();
        if (coCommittingCourt == null) {
            cos.setCOCommittingCourt(new COCommittingCourt());
            coCommittingCourt = cos.getCOCommittingCourt();
        }

        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        // ParsePosition pos = new ParsePosition(0);

        // Timestamp committingDate = caseEntity.getMagConvictionDate();

        // java.util.Date settingDate = null;

        // if no date then set to today's date
        cos.getCOCommittingCourt().setDate(new Date(new java.util.Date()));

        // Committing court is selected.
        // coCommittingCourt.setSelected(true);

        // retrieve information on committing court.
        RefCourt refCourtEntity = null;
        try {
            refCourtEntity = refCourtHome.findByPrimaryKey(caseEntity.getRefCourtId());
            // As the CoCommittingCourt also includes a Date element the
            // CourtHouse
            // structure needs to be extracted before passing to helper to
            // populate details
            CourtHouse coCourtHouse = coCommittingCourt.getCourtHouse();
            CourtHouseHelper.populateCourtHouse(coCourtHouse, refCourtEntity);
        } catch (FinderException ex) {
            // This is now okay as we allow the user to select
            // the committing court if none is found
            log.info("REF COURT NOT FOUND " + caseEntity.getRefCourtId());
            cos.getCOCommittingCourt().setSelected(false);
        }

        if (caseEntity.getCaseType().equals("S")) {
            if (null != refCourtEntity) {
                cos.getCOCommittingCourt().setSelected(true);
            }
        } else {
            // case type T
            cos.getTrial().setSelected(true);
            setTrialConvictionDate(cos, docEntity, caseEntity, typeCode);
        }
    }

    /**
     * Setup relevant xml for trial conviction date.
     * 
     * @param cos
     *            The community order structure to populate.
     * @param docEntity
     *            The defendant on case to populate from.
     * @param relevantDOOs
     *            The relevant defendant on offence entities
     */
    private static void setTrialConvictionDate(XHIBITCommunityOrderStructure cos, DefendantOnCase docEntity,
            Case caseEntity, String disposalCode) {
        cos.getTrial().setConvictionDate(
                new Date(GenericOrderHelper.getConvictionDate(caseEntity.getCourtId(), disposalCode, docEntity
                        .getDefendantOnCaseId())));
    }

    /**
     * Create an original court
     */
    private static void populateOriginatingCourt(OriginatingCourt coOriginatingCourt, XhbBreach xhbBreach) {
        // Set up originating court details i.e. breaches exist
        java.util.Date sentenceDate = xhbBreach.getOriginalSentenceDate();
        if (sentenceDate == null) {
            sentenceDate = new java.util.Date();
        }

        log.debug("Set OriginatingDate : " + sentenceDate);
        coOriginatingCourt.setDate(new Date(sentenceDate));
    }
}
