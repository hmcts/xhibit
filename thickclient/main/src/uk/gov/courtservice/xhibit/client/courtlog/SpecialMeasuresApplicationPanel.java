package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Special Measures Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class SpecialMeasuresApplicationPanel extends CourtLogEventPanel {

    private static final long serialVersionUID = 1L;
    
    private static final String YES = "yes";
    private static final String NO = "no";
    
    private static final String XML_ELEMENT_ORDERS_FOR_SCREENS_GRANTED =  "E20934_Order_For_Screens_Granted";
    private static final String XML_ELEMENT_EVIDENCE_BY_LIVE_LINK = "E20934_Evidence_By_Live_Link";
    private static final String XML_ELEMENT_EVIDENCE_TO_BE_GIVEN_IN_PRIVATE = "E20934_Evidence_To_Be_Given_In_Private";
    private static final String XML_ELEMENT_REMOVAL_OF_WIGS_AND_GOWNS = "E20934_Removal_Of_Wigs_And_Gowns";
    private static final String XML_ELEMENT_VIDEO_RECORDED_EVIDENCE_IN_CHIEF = "E20934_Video_Recorded_Evidence_In_Chief";
    private static final String XML_ELEMENT_AIDS_TO_COMMUNICATION = "E20934_Aids_To_Communication";
    
   
    private static final String MESSAGE_ORDERS_FOR_SCREENS_GRANTED =  "Order for screens granted";
    private static final String MESSAGE_EVIDENCE_BY_LIVE_LINK = "Evidence by live link";
    private static final String MESSAGE_EVIDENCE_TO_BE_GIVEN_IN_PRIVATE = "Evidence to be given in private";
    private static final String MESSAGE_REMOVAL_OF_WIGS_AND_GOWNS = "Removal of wigs and gowns";
    private static final String MESSAGE_VIDEO_RECORDED_EVIDENCE_IN_CHIEF = "Video recorded evidence in chief";
    private static final String MESSAGE_AIDS_TO_COMMUNICATION = "Aids to communication";

    private static final String XML_ELEMENT_EVENT_SPECIFIC_TEXT = "EventSpecificText";
    private static final String XML_ELEMENT_SPECIAL_MEASURES_APPLICATION_OPTIONS = 
        "E20934_Special_Measures_Application_Options";

    private final SpecialMeasuresApplicationModel model;

    private JCheckBox[] checkBoxes = {
            new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox()
    };
    
    private JLabel[] checkBoxLabels = {
            new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel()
    };
    
    private static final String[][] mappings = {
            {SpecialMeasuresApplicationModel.ORDERS_FOR_SCREENS_GRANTED, 
                XML_ELEMENT_ORDERS_FOR_SCREENS_GRANTED,
                MESSAGE_ORDERS_FOR_SCREENS_GRANTED},
            {SpecialMeasuresApplicationModel.EVIDENCE_BY_LIVE_LINK, 
                XML_ELEMENT_EVIDENCE_BY_LIVE_LINK,
                MESSAGE_EVIDENCE_BY_LIVE_LINK},
            {SpecialMeasuresApplicationModel.EVIDENCE_TO_BE_GIVEN_IN_PRIVATE, 
                XML_ELEMENT_EVIDENCE_TO_BE_GIVEN_IN_PRIVATE,
                MESSAGE_EVIDENCE_TO_BE_GIVEN_IN_PRIVATE},
            {SpecialMeasuresApplicationModel.REMOVAL_OF_WIGS_AND_GOWNS, 
                XML_ELEMENT_REMOVAL_OF_WIGS_AND_GOWNS,
                MESSAGE_REMOVAL_OF_WIGS_AND_GOWNS},
            {SpecialMeasuresApplicationModel.VIDEO_RECORDED_EVIDENCE_IN_CHIEF, 
                XML_ELEMENT_VIDEO_RECORDED_EVIDENCE_IN_CHIEF,
                MESSAGE_VIDEO_RECORDED_EVIDENCE_IN_CHIEF},
            {SpecialMeasuresApplicationModel.AIDS_TO_COMMUNICATION, 
                XML_ELEMENT_AIDS_TO_COMMUNICATION,
                MESSAGE_AIDS_TO_COMMUNICATION},
            };
    
    

    public SpecialMeasuresApplicationPanel(final XDialog parent, final SpecialMeasuresApplicationModel model) 
    throws CSRecoverableException {
        super(parent, model);
        this.model = model;

        stepInitialise();
        jbInit();
    }

    private void jbInit() {
        final JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());
        CompoundBorder border2 = 
            BorderFactory.createCompoundBorder(
                    new TitledBorder(""), 
                    BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.setBorder(border2);

        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);

        for (int i = 0; i < mappings.length; i++) {
            checkBoxLabels[i].setText(
                    getResource(getSpecialMeasureName(i) + "Label"));
            checkBoxes[i].addChangeListener(
                    new ChangeListener() {
                        public void stateChanged(ChangeEvent changeEvent) {
                            stepUpdateViewStateHandleExceptions();
                        }
                    });
        }
        
        for (int i = 0; i < checkBoxes.length; i++) {
            gbc.gridy = i;
            gbc.gridx = 0;
            panel.add(checkBoxes[i], gbc);
            gbc.gridx = 1;
            panel.add(checkBoxLabels[i], gbc);
        }

        this.add(getPanelTitle(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(panel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    public void stepInitialise() throws CSRecoverableException {
        super.stepInitialise();
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();
    }

    /**
     * Life-cycle method that is executed when the screen is destroyed. This is
     * generally as a result of the user clicking the OK/Cancel buttons. It
     * constructs a CourtLogCRUDValue for each record to be added/updated and
     * calls the appropriate method on the business delegate.
     * 
     * @param update -
     *            true if the user clicked the OK button
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            if (model.isInEditMode()) {
                super.stepDeinitialise(update);
            } else {
                super.getCourtLogEventLevelPanel().stepDeinitialise(update);
                CourtLogCRUDValue crudValue = createCRUDFromModel();
                getCLCDelegate().newEntry(crudValue);
            }
        }
    }

    /**
     * Copy the model into XML property map
     */
    protected void populateCRUDProperties(Map propertyMap) {
        HashMap<String, String> options = new HashMap<String, String>();
        
        StringBuffer eventSpecificText = new StringBuffer();
        boolean firstItem = true;
        
        for (int i = 0; i < mappings.length; i++) {
            final boolean selected = 
                model.getSpecialMeasuresApplication(getSpecialMeasureName(i));
            
            options.put(getSpecialMeasureXMLElementName(i), (selected ? YES : NO));
            
            if (selected) {
                if (firstItem) {
                    firstItem = false; // append delimiter next time
                    eventSpecificText.append(getSpecialMeasureEventSpecificText(i));
                } else {
                    eventSpecificText.append(", "); // append delimiter
                    eventSpecificText.append(getSpecialMeasureEventSpecificText(i));
                }
            }
        }
        
        propertyMap.put(XML_ELEMENT_EVENT_SPECIFIC_TEXT, eventSpecificText.toString());
        propertyMap.put(XML_ELEMENT_SPECIAL_MEASURES_APPLICATION_OPTIONS, options);
        log.debug("PropertySet: " + propertyMap.get(XML_ELEMENT_SPECIAL_MEASURES_APPLICATION_OPTIONS));
    }

    /**
     * Populate the model from the XML property map
     */
    protected void populateModelProperties(Map propertyMap) {
        HashMap options = (HashMap) propertyMap.get(XML_ELEMENT_SPECIAL_MEASURES_APPLICATION_OPTIONS);
        
        for (int i = 0; i < mappings.length; i++) {
            final String specialMeasureName = getSpecialMeasureName(i);
            final boolean selected = 
                options.containsKey(getSpecialMeasureXMLElementName(i))
                && options.get(getSpecialMeasureXMLElementName(i)).equals(YES);
            model.setSpecialMeasuresApplication(specialMeasureName, selected);
        }
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        super.stepUpdateViewState();
    }

    protected boolean isMandatoryFieldsCompleted() {
        
        for (int i = 0; i < mappings.length; i++) {
            if (checkBoxes[i].isSelected()) {
                // At least one special measure has to be selected
                return true;
            }
        }
        
        return false;
    }
    
    private String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.SpecialMeasures, key);
    }
 
    /**
     * Make the GUI display state match the model
     */
    protected void moveModelToScreen() {
        super.moveModelToScreen();

        for (int i = 0; i < mappings.length; i++) {
            final boolean selected = 
                model.getSpecialMeasuresApplication(
                    getSpecialMeasureName(i));
            checkBoxes[i].setSelected(selected);
        }
    }
    
    /**
     * Make the model match GUI display state
     */
    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        for (int i = 0; i < mappings.length; i++) {
            model.setSpecialMeasuresApplication(
                    getSpecialMeasureName(i), checkBoxes[i].isSelected());
        }
    }
    
    private String getSpecialMeasureName(int i) {
        return mappings[i][0];
    }
    
    private String getSpecialMeasureXMLElementName(int i) {
        return mappings[i][1];
    }
    
    private String getSpecialMeasureEventSpecificText(int i) {
        return mappings[i][2];
    }
}
