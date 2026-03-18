package uk.gov.courtservice.xhibit.client.courtlog.directions.caze;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ComboHelperVO;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.ItemChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.UpdateStateKeyListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsPanel;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.NumericValidatingDocumentDecorator;

/**
 * <p>
 * Title: Trial Time Estimate Panel
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
 * @version $Id: TimeEstimatePanel.java,v 1.30 2006/06/05 12:31:14 bzjrnl Exp $
 */
public class TimeEstimatePanel extends XDirectionsPanel {
    private static final Logger log = CSServices.getLogger(TimeEstimatePanel.class);

    private final String pdLookup = "E" + getEventType() + "_Time_Estimate_Options";

    private TitledBorder padBorder;

    private JTextField timeField = null;

    private JComboBox unitCombo = null;

    private Collection thisRestriction = null;

    private XhbDirectionsForCaseBasicValue dcv;

    public TimeEstimatePanel(DirectionsForCaseValue model) throws CSRecoverableException {
        setModel(model);
        init();
        stepActivate();
    }

    public TimeEstimatePanel(DirectionsForCaseValue model, String labelText) throws CSRecoverableException {
        this(model);
        padBorder.setTitle(labelText);
        this.setModified(false);
    }

    public Integer getEventType() {
        return PDHConstants.CASE_TRIALTIME;
    }

    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        getTimeField().setEnabled(enable);
        getUnitField().setEnabled(enable);
    }

    public void setModel(DirectionsForCaseValue newModel) {
        dcv = newModel.getDirectionsForCaseBasicValue();
    }

    public void moveModelToScreen() {
        if (dcv != null) {
            if (dcv.getTrialTimeEstimate() != null) {
                // Time field only takes positive integers.
                getTimeField().setText(String.valueOf(dcv.getTrialTimeEstimate().intValue()));
            } else {
                getTimeField().setText("");
            }

            if (dcv.getTrialTimeUnit() != null) {
                String xmlKey = PDHConstants.getKeyForDb(dcv.getTrialTimeUnit().toString(), getRestrictions());
                for (int i = 0, n = getUnitField().getItemCount(); i < n; i++) {
                    ComboHelperVO cvo = (ComboHelperVO) getUnitField().getItemAt(i);
                    if (cvo.getXmlValue().equals(xmlKey)) {
                        getUnitField().setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    public void moveScreenToModel() {
        if (dcv != null) {
            try {
                if (getTimeField().getText().trim().length() <= 0) {
                    dcv.setTrialTimeEstimate(null);
                    dcv.setTrialTimeUnit(null);
                } else {
                    Float timeInt = Float.valueOf(getTimeField().getText());
                    dcv.setTrialTimeEstimate(timeInt);
                    Integer i = new Integer(((ComboHelperVO) getUnitField().getSelectedItem()).getDbValue());
                    dcv.setTrialTimeUnit(i);
                }

                // Set the time to be today's date and time. This should never
                // be
                // null so we will always set it (creates and updates). This
                // is required for CREST form A where a
                // DirectionsForCaseBasicValue
                // doesn't exist.
                dcv.setDateTime(Calendar.getInstance().getTime());
            } catch (NumberFormatException ex) {
                log.info(ex, ex);
            }
        }
    }

    public void populateCRUD(HashMap crud, Integer defOnCaseId) {
        if ((dcv != null) && (dcv.getTrialTimeEstimate() != null) && getTimeField().isEnabled()
                && getUnitField().isEnabled()) {
            String eventCode = "E" + getEventType().toString();
            final Map timeOptions = new HashMap();

            timeOptions.put(eventCode + "_Time", String.valueOf(dcv.getTrialTimeEstimate().intValue()));
            timeOptions.put(pdLookup, ((ComboHelperVO) getUnitField().getSelectedItem()).getXmlValue());

            crud.put(eventCode + "_Time_Estimate", timeOptions);
        }
    }

    private Collection getRestrictions() {
        if (thisRestriction == null) {
            thisRestriction = RestrictionFinder.getRestrictingValues(PDHConstants.CASE_TRIALTIME.toString() + ".xsd",
                    pdLookup);
        }
        return thisRestriction;
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        padBorder = new TitledBorder(XHIBITConstant.getResource(XhibitBundles.Directions, "TimeEstimateBorder"));
        Border border2 = BorderFactory.createCompoundBorder(padBorder, BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setBorder(border2);

        this.add(getTimeField(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getUnitField(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(XHIBITConstant.getSpacer(), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private JTextField getTimeField() {
        if (timeField == null) {
            timeField = new JTextField();
            // use standard way to validate the entry
            // PR 57478 - limit trial time estimate to 3 digits
            timeField
                    .setDocument(new NumericValidatingDocumentDecorator(new LimitedTextValidatingDocumentDecorator(3)));
            Dimension d = new Dimension(40, XHIBITConstant.getLineHeight());
            timeField.setMinimumSize(d);
            timeField.setPreferredSize(d);
            timeField.addKeyListener(new UpdateStateKeyListener(this));
        }
        return timeField;
    }

    public String getTimeFieldText() {
        return timeField.getText();
    }

    @SuppressWarnings("unchecked")
	private JComboBox getUnitField() {
        if (unitCombo == null) {
            Vector v = new Vector();
            Iterator iter = getRestrictions().iterator();
            while (iter.hasNext()) {
                String item = (String) iter.next();
                ComboHelperVO cvo = new ComboHelperVO(item, XHIBITConstant.getResource(XhibitBundles.Directions, item
                        + "_DB"), XHIBITConstant.getResource(XhibitBundles.Directions, item));
                v.add(cvo);
            }
            Collections.sort(v, new TimeEstimateUnitComparator());
            unitCombo = new JComboBox(v);
            unitCombo.addItemListener(new ItemChangeListener(this));
        }
        return unitCombo;
    }

    public void stepValidate(boolean timeIsMandatory) throws CSValidationException {
        if (getTimeField().getText().length() > 0) {
            try {
                float f = Float.parseFloat(getTimeField().getText());
                if (f <= 0) {
                    throw new CSValidationException("validation.minexclusive",
                            new Object[] { padBorder.getTitle(), "0" }, "Time estimate of <= 0 enterered");
                }
            } catch (NumberFormatException ex) {
                getTimeField().requestFocus();
                throw new CSValidationException("validation.datatype", new Object[] { getTimeField().getText() },
                        "Trial time estimate not a valid float", ex);
            }
        } else if (timeIsMandatory) {
            getTimeField().requestFocus();
            throw new CSValidationException("validation.mandatoryfield", new Object[] { XHIBITConstant.getResource(
                    XhibitBundles.Directions, "TimeEstimateBorder") }, "Trial time estimate not a valid value");
        }
    }

    public void stepValidate() throws CSValidationException {
        stepValidate(false);
    }

    public void stepUpdateViewState() {
    }
    
	private static class TimeEstimateUnitComparator implements Comparator<ComboHelperVO> {

		private static final String DAYS_DBVALUE = "2"; 
		
		private Integer getPriority(ComboHelperVO object) {
			Integer result = Integer.valueOf(99);
			if (DAYS_DBVALUE.equals(object.getDbValue())) {
				result = Integer.valueOf(1);
			}  
			return result;
		}
		@Override
		public int compare(ComboHelperVO o1, ComboHelperVO o2) {
			// Sort by priority...
			Integer o1Priority = getPriority(o1);
			Integer o2Priority = getPriority(o2);
			Integer diff = o1Priority.compareTo(o2Priority);
			// ...then Sort by db value order
			if (Integer.valueOf(0).equals(diff)) {
				diff = o1.getDbValue().compareTo(o2.getDbValue());
			}
			return diff;
		}
	}
}
