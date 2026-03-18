package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.caze.DirectionsPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.caze.PadPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.caze.TimeEstimatePanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.ArraingmentPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.BailCustodyPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.CertOfAttendPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.FormBPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.defendant.IdentificationPanel;
import uk.gov.courtservice.xhibit.client.courtlog.util.CourtLogAuditPanel;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: DirectionSingleEventPanel allows the modification of a Directions
 * event
 * </p>
 * <p>
 * Description: These may be directions for case or single directions for
 * defendant events
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
public class DirectionSingleEventPanel extends DirectionsParentPanel {
    XhibitApplicationController _xac;

    XDirectionsPanel _currentBody;

    CourtLogCRUDValue _thisCrud;

    CourtLogAuditPanel lap = null;

    /**
     * The xac contains case/defendant information. The CourtLogCRUDValue
     * contains the event information
     * 
     * @param xac
     * @param crud
     * @throws CSRecoverableException
     */
    public DirectionSingleEventPanel(XhibitApplicationController xac, CourtLogCRUDValue crud)
            throws CSRecoverableException {
        _xac = xac;
        _thisCrud = crud;
        init(crud);
    }

    private void init(CourtLogCRUDValue crud) throws CSRecoverableException {
        this.setLayout(new GridBagLayout());
        _currentBody = getRequiredPanel(crud);

        if (_currentBody != null) {
            this.add(_currentBody, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        }
        this.add(getLogAuditPanel(crud), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        stepActivate();
    }

    private CourtLogAuditPanel getLogAuditPanel(CourtLogCRUDValue crud) throws CSRecoverableException {
        List values = CourtLogAuditPanel.getValues(crud.getCaseId());
        lap = new CourtLogAuditPanel(CourtLogAuditPanel.DATEPICKER_AND_TIME, values, crud.getScheduledHearingId());
        lap.getDatePicker().getDropDownDate().addItemListener(new ItemChangeListener(this));

        lap.setTimeDefault(crud.getEntryDate());
        return lap;
    }

    private XDirectionsPanel getRequiredPanel(CourtLogCRUDValue crud) throws CSRecoverableException {
        Integer eventType = crud.getEventType();

        if (eventType.equals(PDHConstants.CASE_DIRECTIONS)) {
            return new DirectionsPanel(getDirectionsForCaseModel(crud));
        } else if (eventType.equals(PDHConstants.CASE_PDFORM)) {
            return new PadPanel(getDirectionsForCaseModel(crud));
        } else if (eventType.equals(PDHConstants.CASE_TRIALTIME)) {
            return new TimeEstimatePanel(getDirectionsForCaseModel(crud));
        } else if (eventType.equals(PDHConstants.DEF_ARRAIGNMENT)) {
            return new ArraingmentPanel(new DirectionsForDefendantValue[] { getDirectionsForDefendantModel(crud) });
        } else if (eventType.equals(PDHConstants.DEF_BAIL)) {
            return new BailCustodyPanel(new DirectionsForDefendantValue[] { getDirectionsForDefendantModel(crud) });
        } else if (eventType.equals(PDHConstants.DEF_CERTATTENDANCE)) {
            return new CertOfAttendPanel(new DirectionsForDefendantValue[] { getDirectionsForDefendantModel(crud) });
        } else if (eventType.equals(PDHConstants.DEF_FORMB)) {
            return new FormBPanel(new DirectionsForDefendantValue[] { getDirectionsForDefendantModel(crud) });
        } else if (eventType.equals(PDHConstants.DEF_IDENTIFICATION)) {
            return new IdentificationPanel(new DirectionsForDefendantValue[] { getDirectionsForDefendantModel(crud) });
        } else {
            return null;
        }
    }

    private PDHHelper _pdhHelper = null;

    private PDHHelper getPdhHelper() {
        if (_pdhHelper == null) {
            _pdhHelper = new PDHHelper(_xac);
        }
        return _pdhHelper;
    }

    private DirectionsForCaseValue getDirectionsForCaseModel(CourtLogCRUDValue crud) {
        return getModel(crud).getDirectionsForCaseValue();
    }

    private DirectionsForDefendantValue getDirectionsForDefendantModel(CourtLogCRUDValue crud) {
        return (DirectionsForDefendantValue) getModel(crud).getDirectionsForDefendantValue().toArray()[0];
    }

    private DirectionsValue _directionsValue;

    private DirectionsValue getModel(CourtLogCRUDValue crud) {
        PDHHelper pdhHelp = getPdhHelper();
        if (_directionsValue == null)
            _directionsValue = new DirectionsValue();

        if (PDHConstants.isDirectionForCaseEvent(crud.getEventType())
                && (_directionsValue.getDirectionsForCaseValue() == null)) {
            DirectionsForCaseValue dcv = pdhHelp.getPopulatedDirectionsForCase(crud);
            _directionsValue.setDirectionsForCaseValue(dcv);
        }
        if (PDHConstants.isDirectionForDefendantEvent(crud.getEventType())
                && (_directionsValue.getDirectionsForDefendantValue() == null || _directionsValue
                        .getDirectionsForDefendantValue().isEmpty())) {
            DirectionsForDefendantValue ddv = pdhHelp.getPopulatedDirectionsForDefendant(crud);
            Collection c = new ArrayList(1);
            c.add(ddv);
            _directionsValue.setDirectionsForDefendantValue(c);
        }
        return _directionsValue;
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        super.stepDeinitialise(update);
        if (update) {
            // Create CRUD
            _thisCrud.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
            HashMap options = new HashMap();
            _currentBody.populateCRUD(options, _thisCrud.getDefendantOnCaseId());
            _thisCrud.setEntryDate(lap.getDateTime().getTime());
            if (_thisCrud.getId() == null) {
                _thisCrud.setId(new Integer(0));
            }

            ApplicationCaseModel acm = _xac.getApplicationCaseModel();

            if (PDHConstants.isDirectionForCaseEvent(_thisCrud.getEventType())) {
                _thisCrud.setProperty("Directions_By_Case_Options", options);

                CourtLogCRUDValue[] crudArray = new CourtLogCRUDValue[] { _thisCrud };
                DirectionsForCaseValue dcv = getDirectionsForCaseModel(_thisCrud);
                XhbDirectionsForCaseBasicValue dcbv = dcv.getDirectionsForCaseBasicValue();
                dcv.setCourtLogCRUDValues(crudArray);
                dcbv.setCaseId(acm.getCaseId());
                // dcbv.setScheduledHearingId(lap.getScheduledHearingId());
                dcbv.setDateTime(lap.getDateTime().getTime());
            }

            if (PDHConstants.isDirectionForDefendantEvent(_thisCrud.getEventType())) {
                DefendantBasicValue dbv = getPdhHelper().findDefendantBasicValueByDefOnCaseId(
                        _thisCrud.getDefendantOnCaseId());
                options.put("Defendant_Name", PDHConstants.buildDefendantName(dbv));
                _thisCrud.setProperty("Direction_By_Defendant_Options", options);

                CourtLogCRUDValue[] crudArray = new CourtLogCRUDValue[] { _thisCrud };
                DirectionsForDefendantValue ddv = getDirectionsForDefendantModel(_thisCrud);
                XhbDirectionsForDefendantBasicValue ddbv = ddv.getDirectionsForDefendantBasicValue();
                ddv.setCourtLogCRUDValue(crudArray);
                ddbv.setDateTime(lap.getDateTime().getTime());
            }

            // call saveDirections or updateDirections on midtier.
            XhibitDelegateHelper.getDirectionsDelegate().saveDirections(getModel(_thisCrud));
        }
    }

    public void stepValidate() throws CSValidationException {
        // Time estimate for trial
        if (_thisCrud.getEventType().equals(PDHConstants.CASE_TRIALTIME) && _currentBody instanceof TimeEstimatePanel) {
            ((TimeEstimatePanel) _currentBody).stepValidate(true);
        }

        // Directions for case
        if (_thisCrud.getEventType().equals(PDHConstants.CASE_DIRECTIONS) && _currentBody instanceof DirectionsPanel) {
            ((DirectionsPanel) _currentBody).stepValidate(true);
        }
    }
}