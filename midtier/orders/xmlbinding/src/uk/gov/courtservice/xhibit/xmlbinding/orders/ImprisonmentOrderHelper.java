package uk.gov.courtservice.xhibit.xmlbinding.orders;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourt;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourtHome;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DeportationReasons;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DeportationSection;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.IOCommittingCourt;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ImprisonmentOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.ImprisonmentOrderCommonStructure;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * ImprisonmentOrderStructure< /p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Populates the ImprisonmentOrderStructure for 'Commit to Young Offender
 * Institution' and 'Order for Imprisonment'.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public class ImprisonmentOrderHelper {
    private static final Logger log = CSServices.getLogger(ImprisonmentOrderHelper.class);

    private static final RefCourtHome refCourtHome;

    static {
        refCourtHome = (RefCourtHome) CSServices.getServiceLocator().getLocalHome(RefCourtHome.class);
    }

    /**
     * Utility method to populate an ImprisonmentOrderStructure from the rest of
     * the Xhibit entities.
     * 
     * @param ios
     *            The ImprisonmentOrderStructure to be populated (pass by
     *            reference).
     * @param docEntity
     *            The Defendant on case for which to populate the order.
     * @param typeCode
     *            The type code of the order.
     * @throws OrderXMLException
     *             When there is a problem in population.
     */
    public static void populateImprisonmentOrder(ImprisonmentOrderCommonStructure iocs, DefendantOnCase docEntity,
            String typeCode) throws OrderXMLException {
        Case caseEntity = docEntity.getCaze();
        
       if (typeCode.equals("IMPO") || typeCode.equals("COMY"))
       {
           //Updates the IOS to include the deporttion reasons entered for the defendant on Case.
           DeportationSection deportationReasons = getDeportationReasons(docEntity);
                   
           ((ImprisonmentOrderStructure) iocs).setDeportationSection(deportationReasons);
           
       }
        
        
        if (caseEntity.getCaseType().equals("S")) // is a commital for
        // sentence.
        {
            setCommitalForSentence(iocs, caseEntity);
        } else // should be a commital for trial. T.
        {
            setCommittalForTrial(iocs, docEntity, caseEntity, typeCode);
        }
        
    }
    
    /**
     * Description: Retrieves the deportation reason from the defendant on case record and 
     * returns a populated the deportation section
     * @param DefendantOnCase
     * @return DeportationSection
     */
    private static DeportationSection getDeportationReasons(DefendantOnCase doc){
        
        DeportationReasons deportationReasons = new DeportationReasons();
        DeportationSection deportationSection = new DeportationSection();
        
        //Retreive deportation reason for defendant on case
        if(doc.getCustodial() != null){
            deportationReasons.setReason("custodial");
            }
        if(doc.getSuspended() != null){
            deportationReasons.setReason("suspended");
            }
        if(doc.getRecommendedDeportation() != null){
            deportationReasons.setReason("recommendedDeportation");
            }
        if(doc.getSeriousDrugOffence() != null){
            deportationReasons.setReason("seriousDrugOffence");
            }
        
        //If a selected value is passed, make the section enabled.
        if (deportationReasons.getReason() != null){
            deportationSection.setSelected(true);
        }
        else{
            deportationSection.setSelected(false);
            deportationReasons.setReason("custodial");
        }
        
        //populate new deportation section with retrieved reasons
        deportationSection.setDeportationReasons(deportationReasons);
        
        return deportationSection;
    }

    /**
     * Setup relevant xml for commital for trial.
     * 
     * @param ios
     *            The imprisonment order structure to populate.
     * @param docEntity
     *            The defendant on case to populate from.
     * @param relevantDOOs
     *            The relevant defendant on offence entities
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    private static void setCommittalForTrial(ImprisonmentOrderCommonStructure iocs, DefendantOnCase docEntity,
            Case caseEntity, String disposalCode) throws OrderXMLException {
        // Create Castor Date. Note getConvictionDate allways returns a value.
        iocs.setConvictionDate(new Date(GenericOrderHelper.getConvictionDate(caseEntity.getCourtId(), disposalCode,
                docEntity.getDefendantOnCaseId())));
    }

    /**
     * Setup relevant xml for commital for sentence.
     * 
     * @param ios
     *            The imprisonment order structure to populate.
     * @param caseEntity
     *            The case to populate from.
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    private static void setCommitalForSentence(ImprisonmentOrderCommonStructure iocs, 
            Case caseEntity) throws OrderXMLException {

        // Check if date exists in the database, if not date set to current
        java.util.Date convictionDate = caseEntity.getMagConvictionDate();
        if (convictionDate == null) {
            convictionDate = new java.util.Date();
        }
        log.debug("Set ConvictionDate : " + convictionDate);
        iocs.setConvictionDate(new Date(convictionDate));

        // Set up committing court.
        IOCommittingCourt ioCommittingCourt = iocs.getIOCommittingCourt();
        if (ioCommittingCourt == null) {
            ioCommittingCourt = new IOCommittingCourt();
            iocs.setIOCommittingCourt(ioCommittingCourt);
        }

        // retrieve information on committing court.
        try {
            RefCourt refCourtEntity = refCourtHome.findByPrimaryKey(caseEntity.getRefCourtId());

            CourtHouseHelper.populateCourtHouse(ioCommittingCourt, refCourtEntity);

            // Committing court is selected.
            ioCommittingCourt.setSelected(true);
            log.debug("<<<>>> Court Found <<<>>> " + refCourtEntity.getCourtFullName());
        } catch (FinderException ex) {
            // This is now okay as we allow the user to select
            // the committing court if none is found
            log.debug("<<<>>> COURT NOT FOUND <<<>>>");
            ioCommittingCourt.setSelected(false);
        }
    }
}