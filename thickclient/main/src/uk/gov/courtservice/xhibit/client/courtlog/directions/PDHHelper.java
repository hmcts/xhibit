package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: Helper class to carry out complex re-usable code.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */
public class PDHHelper {
    private XhibitApplicationController _xac;

    /**
     * 
     * @param xac
     * @throws CSUnrecoverableException
     */
    public PDHHelper(XhibitApplicationController xac) throws CSUnrecoverableException {
        if (xac == null)
            throw new CSUnrecoverableException("XAC passed in is null. Can not function");
        _xac = xac;
    }

    /**
     * Creates and returns empty DirectionsForCaseValue
     * 
     * @return DirectionsForCaseValue
     */
    public DirectionsForCaseValue getEmptyDirectionsForCaseValue() {
        DirectionsForCaseValue defCaseVO = new DirectionsForCaseValue();
        defCaseVO.setDirectionsForCaseBasicValue(new XhbDirectionsForCaseBasicValue());
        Integer caseId = _xac.getApplicationCaseModel().getCaseId();
        Integer scheduledHearingId = _xac.getApplicationCaseModel().getScheduledHearingId();
        defCaseVO.getDirectionsForCaseBasicValue().setCaseId(caseId);
        // defCaseVO.getDirectionsForCaseBasicValue().setScheduledHearingId(scheduledHearingId);
        return defCaseVO;
    }

    /**
     * Returns a populated a DirectionsForCaseValue using CourtLogCRUDValue
     * passed in.
     * 
     * @param crud
     * @return DirectionsForCaseValue
     */
    public DirectionsForCaseValue getPopulatedDirectionsForCase(CourtLogCRUDValue crud) {
        DirectionsForCaseValue dcv = getEmptyDirectionsForCaseValue();
        populateModel(dcv, crud);
        return dcv;
    }

    /***************************************************************************
     * Creates an instance of a DirectionsForCaseBasicValue using the parameters
     * passed in.
     * 
     * @param dc
     * @param crud
     */
    public void populateModel(DirectionsForCaseValue dc, CourtLogCRUDValue crud) {
        XhbDirectionsForCaseBasicValue dcb = dc.getDirectionsForCaseBasicValue();
        dcb.setCaseId(crud.getCaseId());
        // dcb.setScheduledHearingId(crud.getScheduledHearingId());
        dcb.setDateTime(crud.getEntryDate());
        dcb.setFreetext(crud.getEntryFreeText());

        HashMap crudValues = (HashMap) crud.getProperty("Directions_By_Case_Options");

        // P&D Form
        String eventCode = "E" + PDHConstants.CASE_PDFORM.toString();
        if (crudValues.containsKey(eventCode + "_P_And_D")) {
            String pad = (String) crudValues.get(eventCode + "_P_And_D");
            String padForm = XHIBITConstant.getResource(XhibitBundles.Directions, pad + "_DB");
            dcb.setHasPanddForm(padForm);
        }

        // Directions
        eventCode = "E" + PDHConstants.CASE_DIRECTIONS.toString();
        if (crudValues.containsKey(eventCode + "_Directions")) {
            String directions = (String) crudValues.get(eventCode + "_Directions");
            dcb.setDirectionsText(directions);
        }

        // Trial Time Estimate
        eventCode = "E" + PDHConstants.CASE_TRIALTIME.toString();
        if (crudValues.containsKey(eventCode + "_Time_Estimate")) {
            HashMap time = (HashMap) crudValues.get(eventCode + "_Time_Estimate");
            if (time.containsKey(eventCode + "_Time")) {
                try {
                    String timeEst = (String) time.get(eventCode + "_Time");
                    if (timeEst != null) {
                        dcb.setTrialTimeEstimate(Float.valueOf(timeEst));
                    }
                } catch (NumberFormatException ex) {
                    XHIBITConstant.error("time estimate from XML not convertable to Integer", ex);
                    // continue
                }
            }

            if (time.containsKey(eventCode + "_Time_Estimate_Options")) {
                try {
                    String timeUnit = (String) time.get(eventCode + "_Time_Estimate_Options");
                    String timeUnitDb = XHIBITConstant.getResource(XhibitBundles.Directions, timeUnit + "_DB");
                    Integer i = new Integer(timeUnitDb);
                    dcb.setTrialTimeUnit(i);
                } catch (NumberFormatException ex) {
                    XHIBITConstant.error("time unit DB from XML not convertable to Integer", ex);
                    // continue
                }
            }
        }
    }

    /**
     * Returns a set of empty directions in a DirectionsForDefendantValue object
     * 
     * @return DirectionsForDefendantValue
     */
    public DirectionsForDefendantValue getEmptyDirectionsForDefendantValue() {
        DirectionsForDefendantValue dirDefVO = new DirectionsForDefendantValue();
        dirDefVO.setDirectionsForDefendantBasicValue(new XhbDirectionsForDefendantBasicValue());
        return dirDefVO;
    }

    /**
     * Returns a populated DirectionsForDefendantValue using the
     * CourtLogCRUDValue passed in.
     * 
     * @param crud
     * @return DirectionsForDefendantValue
     */
    public DirectionsForDefendantValue getPopulatedDirectionsForDefendant(CourtLogCRUDValue crud) {
        DirectionsForDefendantValue ddv = getEmptyDirectionsForDefendantValue();
        populateModel(ddv, crud);
        return ddv;
    }

    /**
     * Populates a DirectionsForDefendantBasicValue using the partamters passed
     * in.
     * 
     * @param dd
     * @param crud
     */
    public void populateModel(DirectionsForDefendantValue dd, CourtLogCRUDValue crud) {
        XhbDirectionsForDefendantBasicValue ddb = dd.getDirectionsForDefendantBasicValue();
        ddb.setDefendantOnCaseId(crud.getDefendantOnCaseId());
        ddb.setDateTime(crud.getEntryDate());
        ddb.setFreetext(crud.getEntryFreeText());

        HashMap crudValues = (HashMap) crud.getProperty("Direction_By_Defendant_Options");

        // Def Identified
        String eventCode = "E" + PDHConstants.DEF_IDENTIFICATION.toString();
        if (crudValues.containsKey(eventCode + "_Identification")) {
            String ident = (String) crudValues.get(eventCode + "_Identification");
            String identDb = XHIBITConstant.getResource(XhibitBundles.Directions, ident + "_DB");
            ddb.setIsIdentified(identDb);
        }

        // Certificate of Attendance
        eventCode = "E" + PDHConstants.DEF_CERTATTENDANCE.toString();
        if (crudValues.containsKey(eventCode + "_Certificate_Of_Attendance")) {
            String coa = (String) crudValues.get(eventCode + "_Certificate_Of_Attendance");
            String coaDb = XHIBITConstant.getResource(XhibitBundles.Directions, coa + "_DB");
            ddb.setCertAttendance(coaDb);
        }

        // Bail or Custody
        eventCode = "E" + PDHConstants.DEF_BAIL.toString();
        if (crudValues.containsKey(eventCode + "_Bail_Or_Custody")) {
            HashMap bcMap = (HashMap) crudValues.get(eventCode + "_Bail_Or_Custody");
            String bc = (String) bcMap.get(eventCode + "_Bail_Or_Custody_Options");
            String bcDb = XHIBITConstant.getResource(XhibitBundles.Directions, bc + "_DB");
            ddb.setBailStatus(bcDb);
            if (bcMap.containsKey(eventCode + "_Bail_Or_Custody_Conditions")) {
                ddb.setNewBailConditions((String) bcMap.get(eventCode + "_Bail_Or_Custody_Conditions"));
            }
        }

        // Arraignment
        eventCode = "E" + PDHConstants.DEF_ARRAIGNMENT.toString();
        if (crudValues.containsKey(eventCode + "_Arraignment")) {
            boolean arraign = crudValues.get(eventCode + "_Arraignment").equals("true"); // ((Boolean)crudValues.get(eventCode
                                                                                            // +
            // "_Arraignment")).booleanValue();
            if (arraign) {
                ddb.setArraigned(XHIBITConstant.getResource(XhibitBundles.Directions, "Arraigned_DB"));
            } else {
                ddb.setArraigned(XHIBITConstant.getResource(XhibitBundles.Directions, "NotArraigned_DB"));
            }
        }

        // Form B
        eventCode = "E" + PDHConstants.DEF_FORMB.toString();
        if (crudValues.containsKey(eventCode + "_Form_B")) {
            HashMap formbMap = (HashMap) crudValues.get(eventCode + "_Form_B");
            String formB = (String) formbMap.get(eventCode + "_Form_B_Options");
            String formBDb = XHIBITConstant.getResource(XhibitBundles.Directions, formB + "_DB");
            ddb.setFiledFormB(formBDb);
            if (formbMap.containsKey(eventCode + "_To_Be_Filed_Date")) {
                try {
                    String listDate = (String) formbMap.get(eventCode + "_To_Be_Filed_Date");
                    ddb.setToBeFiledBy(XDateFormat.parseAsDate(listDate));
                } catch (Exception ex) {
                    XHIBITConstant.error("Problem getting Form B To Be Filed By", ex);
                    // continue
                }
            }
        }
    }

    /**
     * Returns a collection of DefendantOnCaseBasicValues
     * 
     * @return Collection
     */
    public Collection getDefsOnCase() {
        Collection defsOnCase = _xac.getApplicationCaseModel().getScheduledHearingValue()
                .getDefendantOnCaseBasicValues();
        return defsOnCase;
    }

    /**
     * Finds and returns a DefendantBasicValue using the defendantId parameter
     * 
     * @param defendantId
     * @return DefendantBasicValue
     */
    public DefendantBasicValue findDefendantBasicValue(Integer defendantId) {
        Iterator iter = _xac.getApplicationCaseModel().getScheduledHearingValue().getDefendantsOnCase().iterator();
        while (iter.hasNext()) {
            DefendantBasicValue item = (DefendantBasicValue) iter.next();
            if (item.getId().equals(defendantId))
                return item;
        }
        return null;
    }

    /**
     * Finds and returns a DefendantBasicValue using the defendantOnCaseId
     * parameter
     * 
     * @param defendantOnCaseId
     * @return DefendantBasicValue
     */
    public DefendantBasicValue findDefendantBasicValueByDefOnCaseId(Integer defendantOnCaseId) {
        if (getDefsOnCase() != null) {
            Iterator iter = getDefsOnCase().iterator();
            while (iter.hasNext()) {
                DefendantOnCaseBasicValue item = (DefendantOnCaseBasicValue) iter.next();
                if (item.getId().equals(defendantOnCaseId))
                    return findDefendantBasicValue(item.getDefendantID());
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Finds and returns a DefendantOnCaseBasicValue using the defendantId
     * parameter
     * 
     * @param defendantId
     * @return DefendantOnCaseBasicValue
     */
    public DefendantOnCaseBasicValue findDefOnCase(Integer defendantId) {
        if (getDefsOnCase() != null) {
            Iterator iter = getDefsOnCase().iterator();
            while (iter.hasNext()) {
                DefendantOnCaseBasicValue item = (DefendantOnCaseBasicValue) iter.next();
                if (item.getDefendantID().equals(defendantId))
                    return item;
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Returns an array of DefendantBasicValue[] for all defendants listed on
     * the case.
     * 
     * @return DefendantBasicValue[]
     */
    public DefendantBasicValue[] getListedDefendants() {
        Collection defs = _xac.getApplicationCaseModel().getScheduledHearingValue().getDefendantsOnCase();
        DefendantBasicValue[] listedDefendants = new DefendantBasicValue[defs.size()];
        defs.toArray(listedDefendants);
        return listedDefendants;
    }

}