package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

//JDK
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamber;
import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamberMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirmMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing_leg_rep.XhbHearingLegRep;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing_leg_rep.XhbHearingLegRepBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCounselValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRStartEndDates;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAdvocateCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefLegalRepresentativeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.SolicitorCriteria;

/**
 * <p>
 * Title: HRCounselValueHelper
 * </p>
 * <p>
 * Description: This will populate the counsels for the hearing record. It will
 * use the BisRef interface to get the data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg, Anthony Martin
 * @version 1.0
 */
public class HRCounselValueHelper implements HRBasicValueHelper {

    // logger
    private static Logger log = CSServices.getLogger(HRCounselValueHelper.class);

    private BisRefControllerLocal bisRefController;

    /**
     * Default constructor that will set the BisRefController.
     */
    public HRCounselValueHelper() {
        log.debug("HRCounselValueHelper() called");

        // Get the BisRefController
        this.bisRefController = ((BisRefControllerLocal) CSServices.getEJBServices().createLocalSession(
                BisRefControllerLocalHome.class));
        log.debug("HRCounselValueHelper() finished");
    }

    /**
     * This will take a refLegalRepID as an argument (primary key in
     * RefLegalRepresentative). It will call the BisRef interface to get the
     * appropriate Legal representative. It will then transform it to a
     * HRCounsel- Value
     * 
     * @param refLegalRepID
     *            Integer
     * @param legalRole
     *            String
     * @param solFirmOrRefLegalRep
     *            String
     * @param hearingId
     *            Integer
     * @return HRCounselValue
     * @throws HearingRecordException
     */
    public HRCounselValue getRefLegalRepresentative(
            Integer refLegalRepID, 
            String legalRole,
            String solFirmOrRefLegalRep, 
            Integer hearingId, 
            String substituteOrInstructed,
            Integer substitutedRefLegalRepId) 
    throws HearingRecordException {
        log.debug("HRCounselValueHelper.getLegalRepresentative(Integer refLegalRepID :" + refLegalRepID + ") called");

        HRCounselValue hrCounselValue = null;
        Collection advocates = null;
        Collection solicitors = null;

        // special handling for 'In Person'
        if (refLegalRepID == null) {
            hrCounselValue = buildHrCounselForInPersonOrNonAttendanceValue(legalRole, solFirmOrRefLegalRep);
        } else {
            // set the criteria for searching the legal representative
            RefLegalRepresentativeCriteria refLegalRepCriteria = getRefLegRepCriteria(refLegalRepID);

            try {
                // try to find the legal rep via the BisRefController. It
                // returns a
                // Collection but since we are searching for the Primary key
                // there
                // can only be one entry in the collection.
                Collection legalReps = bisRefController.findLegalRepresentatives(refLegalRepCriteria);
                Iterator it = legalReps.iterator();

                log.debug(">>>>>>>>>>>>> Found " + legalReps.size() + " number of Advocates/solicitors");

                if (legalReps.isEmpty()) {
                    // nothing to return so return the hrCounselValue that
                    // is null.
                    return hrCounselValue;
                }

                // just get the first value (no need to loop the iterator) as
                // there
                // will be only one
                RefLegalRepresentativeBasicValue legRepValue = (RefLegalRepresentativeBasicValue) it.next();

                log.debug("The Advocate/solicitor found is : " + legRepValue.toString());

                // start building the HRCounselValue with legrep data
                hrCounselValue = buildHrCounselForLegalRepValue(legRepValue, legalRole);
                hrCounselValue.setSolFirmOrRefLegalRep(solFirmOrRefLegalRep);

                if (hrCounselValue != null) {
                    // check if the leg rep is advocate or solicitor and get
                    // the right object.
                    if (legRepValue.getLegalRepType().equalsIgnoreCase(HearingRecordConstants.LEGAL_REP_TYPE_SOLICITOR)) {
                        // get the solicitor and build remaining part of the
                        // hrCounselvalue
                        log.debug("Found a solicitor will try to set the solicitor values");
                        solicitors = this.getRefSolicitor(refLegalRepID);
                        if (solicitors != null && solicitors.size() > 0) {
                            hrCounselValue = buildHrCounselForSolicitorValue(solicitors, hrCounselValue);
                        }
                    }
                    // there are barristers that are solictors so we need to
                    // look in the
                    // advocate/barrister as well - therefore check if
                    // solicotor is null or sixe == 0
                    if (legRepValue.getLegalRepType().equalsIgnoreCase(HearingRecordConstants.LEGAL_REP_TYPE_ADVOCATE) // for
                            // advocates
                            || (solicitors == null || solicitors.size() == 0)) // for
                    // solicitors
                    // recorded
                    // as
                    // barristers.
                    {
                        // get the advocate and build remaining part of the
                        // hrCounselvalue
                        log.debug("Found an advocate will try to set the Advocate values");
                        advocates = this.getRefAdvocate(refLegalRepID);
                        if (advocates != null) {
                            hrCounselValue = buildHrCounselForAdvocateValue(advocates, hrCounselValue);
                        }
                    }

                    /**
                     * Get the hearing attendances for the legal representative
                     * using hearing ID and legal representative ID For each
                     * record: - add an element to an array of start/end dates
                     * Save the array in HRCounselValue
                     */
                    Collection xhbHearingLegRepEntities = XhbHearingLegRepBeanHelper2.findByHearingAndRefLegRep(
                            new Integer(hearingId.intValue()), new Integer(refLegalRepID.intValue()));
                    HRStartEndDates[] startEndDates = new HRStartEndDates[xhbHearingLegRepEntities.size()];

                    Iterator xhbHearingLegRepIterator = xhbHearingLegRepEntities.iterator();
                    int x = 0;
                    while (xhbHearingLegRepIterator.hasNext()) {
                        XhbHearingLegRep item = (XhbHearingLegRep) xhbHearingLegRepIterator.next();
                        startEndDates[x] = new HRStartEndDates(item.getStartDate(), item.getEndDate());
                        x++;
                    }
                    hrCounselValue.setStartEndDatesArray(startEndDates);
                    hrCounselValue.setSubstituteOrInstructed(substituteOrInstructed);
                    hrCounselValue.setSubstitutedRefLegalRepId(substitutedRefLegalRepId);
                }
            } catch (BisRefControllerException ex) {
                // problems in the BisRef
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
            }
        }

        log.debug("HRCounselValueHelper.getLegalRepresentative(Integer refLegalRepID) finished");
        return hrCounselValue;
    }

    /**
     * Method to set the search criteria for the RefLegalRepresentative
     * 
     * @param refLegalRepID
     *            Integer
     * @return RefLegalRepresentativeCriteria
     */
    private RefLegalRepresentativeCriteria getRefLegRepCriteria(Integer refLegalRepID) {
        log.debug("HRCounselValueHelper.getRefLegRepCriteria(Integer refLegalRepID) finished");

        RefLegalRepresentativeCriteria criteria = new RefLegalRepresentativeCriteria();
        criteria.setPrimaryKey(refLegalRepID);

        log.debug("HRCounselValueHelper.getRefLegRepCriteria(Integer refLegalRepID) finished");
        return criteria;
    }

    /**
     * This will build the HRCounselValue with values from the
     * RefLegalRepresentativeBasicValue
     * 
     * @param legRepValue
     *            RefLegalRepresentativeBasicValue
     * @param legalRole
     *            String
     * @return HRCounselValue
     */
    private HRCounselValue buildHrCounselForLegalRepValue(RefLegalRepresentativeBasicValue legRepValue, String legalRole) {
        log.debug("HRCounselValueHelper.buildHrCounselForLegalRepValue("
                + "RefLegalRepresentativeBasicValue legRepValue) called");
        HRCounselValue hrValue = new HRCounselValue(legRepValue.getId());
        hrValue.setFirstName(legRepValue.getFirstName());
        hrValue.setLegalRepType(legRepValue.getLegalRepType());
        hrValue.setMiddleName(legRepValue.getMiddleName());
        hrValue.setSurname(legRepValue.getSurname());
        hrValue.setLegalRole(legalRole);
        log.debug("BasicValue >>>>>>>>>>>>>>>>: " + legRepValue.toString());
        log.debug("HRCounselValueHelper.buildHrCounselForLegalRepValue("
                + "RefLegalRepresentativeBasicValue legRepValue) finished");
        return hrValue;
    }

    /**
     * Build the parts of the HrCounsel with the Advocate data
     * 
     * @param advocates
     *            Collection
     * @param value
     *            HRCounselValue
     * @return HRCounselValue
     * @throws HearingRecordRetrievalException
     */
    private HRCounselValue buildHrCounselForAdvocateValue(Collection advocates, HRCounselValue value)
            throws HearingRecordRetrievalException {
        HRCounselValue hrValue = value;
        try {
            log.debug("HRCounselValueHelper.buildHrCounselForAdvocateValue() called");

            if (advocates.isEmpty())
                return hrValue;

            Iterator it = advocates.iterator();

            // don't have an advocate local so the only option is as below

            // have an advocate basic value
            // get the chamber id from the advocate basic value
            // use a chamber maintainer to find a chamber basic value
            // find the address id from the chamber basic value
            RefAdvocateBasicValue basicValue = (RefAdvocateBasicValue) it.next();
            RefChamberMaintainer chamberMaintainer = new RefChamberMaintainer();
            log.debug("chamberMaintainer looking up the chamber with a chamberID of " + basicValue.getRefChamberId());
            RefChamber chamberLocal = chamberMaintainer.findByPrimaryKey(basicValue.getRefChamberId());

            RefChamberBasicValue chamberBasic = chamberMaintainer.getBasicValue(chamberLocal);

            AddressMaintainer addressMaintainer = new AddressMaintainer();
            Address addressLocal = addressMaintainer.findByPK(chamberBasic.getAddressId());
            AddressBasicValue addressBasicValue = addressMaintainer.getAddressBasicValue(addressLocal);

            hrValue.setAddressBasicValue(addressBasicValue);
            log.debug("BasivValue >>>>>>>>>>>>>>>>: " + basicValue.toString());
            hrValue.setBarNumber(basicValue.getbarNo());
            hrValue.setRefAdvocateOrSolicitorID(basicValue.getId());
            hrValue.setRefChamberOrSolicitorFirmID(basicValue.getRefChamberId());

            log.debug("HRCounselValueHelper.buildHrCounselForAdvocateValue() finished");
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordRetrievalException("", ex.getMessage(), ex);
        }
        return hrValue;
    }

    /**
     * Build the parts of the HrCounsel with the solicitor data
     * 
     * @param solicitors
     *            Collection
     * @param value
     *            HRCounselValue
     * @return HRCounselValue
     * @throws HearingRecordRetrievalException
     */
    private HRCounselValue buildHrCounselForSolicitorValue(Collection solicitors, HRCounselValue value)
            throws HearingRecordRetrievalException {
        log.debug("HRCounselValueHelper.buildHrCounselForSolicitorValue() called");

        HRCounselValue hrValue = value;
        try {

            if (solicitors.isEmpty())
                return hrValue;

            Iterator it = solicitors.iterator();
            SolicitorBasicValue solicitorBasicValue = (SolicitorBasicValue) it.next();

            Integer solicitorId = solicitorBasicValue.getId();
            log.debug(">>>>>>>>>>>>>>>SOLICITOR ID ======>" + solicitorId.intValue());

            // have an solicitor basic value
            // get the firm id from the solicitor basic value
            // use a firm maintainer to find a firm basic value
            // find the address id from the firm basic value
            // use a firm maintainer to get a firm basic value
            Integer firmId = solicitorBasicValue.getFirmId();
            log.debug(">>>>>>>>>>>>>>>FIRM ID ===========>" + firmId.intValue());
            RefSolicitorFirmMaintainer firmMaintainer = new RefSolicitorFirmMaintainer();
            RefSolicitorFirm firmLocal = firmMaintainer.findByPrimaryKey(firmId);
            RefSolicitorFirmBasicValue firmBasicValue = firmMaintainer.getBasicValue(firmLocal);

            AddressMaintainer addressMaintainer = new AddressMaintainer();
            Address addressLocal = addressMaintainer.findByPK(firmBasicValue.getAddressId());
            AddressBasicValue addressBasicValue = addressMaintainer.getAddressBasicValue(addressLocal);

            hrValue.setAddressBasicValue(addressBasicValue);
            log.debug("BasicValue >>>>>>>>>>>>>>>>: " + solicitorBasicValue.toString());
            hrValue.setBarNumber(null);
            hrValue.setRefAdvocateOrSolicitorID(solicitorBasicValue.getId());
            hrValue.setRefChamberOrSolicitorFirmID(solicitorBasicValue.getFirmId());

            log.debug("HRCounselValueHelper.buildHrCounselForSolicitorValue() finished");
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordRetrievalException("", ex.getMessage(), ex);
        }
        return hrValue;
    }

    /**
     * Only legalRole and solFirmOrRefLegalRep are set when the representation
     * is defendant in person or non-attendance. The other data is irrelevant.
     * 
     * @param legalRole
     * @return
     */
    private HRCounselValue buildHrCounselForInPersonOrNonAttendanceValue(String legalRole, String solFirmOrRefLegalRep) {
        HRCounselValue hrValue = new HRCounselValue();

        hrValue.setLegalRole(legalRole);
        hrValue.setSolFirmOrRefLegalRep(solFirmOrRefLegalRep);

        return hrValue;
    }
    
    /**
     * This cannot be implemented assume we get this with the advocate with the
     * ref leg.
     * 
     * @param refLegalRepID
     *            Integer
     * @return Collection of RefAdvocateBasicValues
     * @throws HearingRecordException
     */
    private Collection getRefAdvocate(Integer refLegalRepID) throws HearingRecordException {
        log.debug("HRCounselValueHelper.getRefAdvocate(Integer refLegalRepID) called");

        Vector values = null;

        // set the criteria
        RefAdvocateCriteria criteria = this.getRefAdvocateCriteria(refLegalRepID);
        try {
            Collection advocates = bisRefController.findAdvocates(criteria);
            Iterator it = advocates.iterator();

            if (advocates.isEmpty()) {
                // nothing to return so return the value that is null.
                return values;
            }
            values = new Vector();

            while (it.hasNext()) {
                RefAdvocateBasicValue value = (RefAdvocateBasicValue) it.next();
                values.addElement(value);
            }
        } catch (BisRefControllerException ex) {
            // problems in the BisRef
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }
        log.debug("HRCounselValueHelper.getRefAdvocate(Integer refLegalRepID) finished");
        return values;
    }

    /**
     * will search for solicitors via bisref.
     * 
     * @param refLegalRepID
     *            Integer
     * @return Collection of SolicitorBasicValue
     * @throws HearingRecordException
     */
    private Collection getRefSolicitor(Integer refLegalRepID) throws HearingRecordException {
        log.debug("HRCounselValueHelper.getRefSolicitor(Integer refLegalRepID) called");

        Vector values = null;

        // set the criteria
        SolicitorCriteria criteria = this.getSolicitorCriteria(refLegalRepID);

        try {
            Collection solicitors = bisRefController.findSolicitors(criteria);
            Iterator it = solicitors.iterator();

            if (solicitors.isEmpty()) {
                // nothing to return so return the value that is null.
                return values;
            }
            values = new Vector();

            while (it.hasNext()) {
                SolicitorBasicValue basicValue = (SolicitorBasicValue) it.next();
                values.addElement(basicValue);
            }
        } catch (BisRefControllerException ex) {
            // problems in the BisRef
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }

        log.debug("HRCounselValueHelper.getRefSolicitor(Integer refLegalRepID) finished");
        return values;
    }

    /**
     * This will set the criteria for the solicitor search
     * 
     * @param refLegalRepID
     *            Integer
     * @return SolicitorCriteria
     */
    private SolicitorCriteria getSolicitorCriteria(Integer refLegalRepID) {
        log.debug("HRCounselValueHelper.getSolicitorCriteria(Integer refLegalRepID) Started");
        SolicitorCriteria criteria = new SolicitorCriteria();
        criteria.setRefLegalRepId(refLegalRepID.toString());
        log.debug("HRCounselValueHelper.getSolicitorCriteria(Integer refLegalRepID) finished");
        return criteria;
    }

    /**
     * This will set the criteria for the advocate search
     * 
     * @param refLegalRepID
     *            Integer
     * @return RefAdvocateCriteria
     */
    private RefAdvocateCriteria getRefAdvocateCriteria(Integer refLegalRepID) {
        log.debug("HRCounselValueHelper.getRefAdvocateCriteria(Integer refLegalRepID) Started");
        RefAdvocateCriteria criteria = new RefAdvocateCriteria();
        criteria.setLegalRepId(refLegalRepID.toString());
        log.debug("HRCounselValueHelper.getRefAdvocateCriteria(Integer refLegalRepID) finished");
        return criteria;
    }

}
