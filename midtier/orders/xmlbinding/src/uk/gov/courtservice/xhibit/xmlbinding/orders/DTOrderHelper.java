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
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DetentionAndTrainingOrderStructure;


/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * Detention&TrainingOrderStructure< /p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Populates the Detention&TrainingOrderStructure 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Brian Hingston
 * @version 1.0
 */

public class DTOrderHelper {
    private static final Logger log = CSServices.getLogger(DTOrderHelper.class);

    private static final RefCourtHome refCourtHome;

    static {
        refCourtHome = (RefCourtHome) CSServices.getServiceLocator().getLocalHome(RefCourtHome.class);
    }

    /**
     * Utility method to populate an DTOrderStructure from the rest of
     * the Xhibit entities.
     * 
     * @param dtos
     *            The DetentionandTrainingOrderStructure to be populated (pass by
     *            reference).
     * @param docEntity
     *            The Defendant on case for which to populate the order.
     * @param typeCode
     *            The type code of the order.
     * @throws OrderXMLException
     *             When there is a problem in population.
     */
    public static void populateDetentionAndTrainingOrder(DetentionAndTrainingOrderStructure dtos, DefendantOnCase docEntity,
            String typeCode) throws OrderXMLException {
        Case caseEntity = docEntity.getCaze();
        
               
        
        if (caseEntity.getCaseType().equals("S")) // is a commital for
        // sentence.
        {
            setCommitalForSentence(dtos, caseEntity);
        } else // should be a commital for trial. T.
        {
            setCommittalForTrial(dtos, docEntity, caseEntity, typeCode);
        }
        
    }
    
    

    /**
     * Setup relevant xml for commital for trial.
     * 
     * @param dtos
     *            The detention & training order structure to populate.
     * @param docEntity
     *            The defendant on case to populate from.
     * @param relevantDOOs
     *            The relevant defendant on offence entities
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    private static void setCommittalForTrial(DetentionAndTrainingOrderStructure dtos, DefendantOnCase docEntity,
            Case caseEntity, String disposalCode) throws OrderXMLException {
        // Create Castor Date. Note getConvictionDate allways returns a value.
        dtos.setConvictionDate(new Date(GenericOrderHelper.getConvictionDate(caseEntity.getCourtId(), disposalCode,
                docEntity.getDefendantOnCaseId())));
    }

    /**
     * Setup relevant xml for commital for sentence.
     * 
     * @param dtos
     *            The detention & training order structure to populate.
     * @param caseEntity
     *            The case to populate from.
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    private static void setCommitalForSentence(DetentionAndTrainingOrderStructure dtos, 
            Case caseEntity) throws OrderXMLException {

        // Check if date exists in the database, if not date set to current
        java.util.Date convictionDate = caseEntity.getMagConvictionDate();
        if (convictionDate == null) {
            convictionDate = new java.util.Date();
        }
        log.debug("Set ConvictionDate : " + convictionDate);
        dtos.setConvictionDate(new Date(convictionDate));

        // Set up committing court.
        IOCommittingCourt dtoCommittingCourt = dtos.getIOCommittingCourt();
        if (dtoCommittingCourt == null) {
            dtoCommittingCourt = new IOCommittingCourt();
            dtos.setIOCommittingCourt(dtoCommittingCourt);
        }

        // retrieve information on committing court.
        try {
            RefCourt refCourtEntity = refCourtHome.findByPrimaryKey(caseEntity.getRefCourtId());

            CourtHouseHelper.populateCourtHouse(dtoCommittingCourt, refCourtEntity);

            // Committing court is selected.
            dtoCommittingCourt.setSelected(true);
            log.debug("<<<>>> Court Found <<<>>> " + refCourtEntity.getCourtFullName());
        } catch (FinderException ex) {
            // This is now okay as we allow the user to select
            // the committing court if none is found
            log.debug("<<<>>> COURT NOT FOUND <<<>>>");
            dtoCommittingCourt.setSelected(false);
        }
    }
}