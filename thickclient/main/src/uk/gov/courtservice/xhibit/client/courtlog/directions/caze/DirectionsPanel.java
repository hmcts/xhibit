package uk.gov.courtservice.xhibit.client.courtlog.directions.caze;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.UpdateStateKeyListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.UTF8LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextAreaFactory;

/**
 * <p>
 * Title: Directions Text Form Panel
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

public class DirectionsPanel extends XDirectionsPanel {
    /**
     * Constant used to represent the maximum number of characters that can be
     * entered into the directions text field
     * 
     * @since Bug X55066
     */
    private static final int DIRECTIONS_TEXT_LIMIT = 255;

    private JScrollPane directionsScroll;

    private JTextArea directionsText;

    private XhbDirectionsForCaseBasicValue dcv;

    public DirectionsPanel(DirectionsForCaseValue model) throws CSRecoverableException {
        setModel(model);
        init();
        stepActivate();
    }

    public Integer getEventType() {
        return PDHConstants.CASE_DIRECTIONS;
    }

    public void setModel(DirectionsForCaseValue newModel) {
        dcv = newModel.getDirectionsForCaseBasicValue();
    }

    public void moveModelToScreen() {
        if ((dcv != null) && (dcv.getDirectionsText() != null)) {
            getDirectionsField().setText(dcv.getDirectionsText());
        }
    }

    public void moveScreenToModel() {
        if (dcv != null) {
            dcv.setDirectionsText(truncate(getDirectionsField().getText(), DIRECTIONS_TEXT_LIMIT));
        }
    }

    public void populateCRUD(HashMap crud, Integer defOnCaseId) {
        if (getDirectionsField().getText().length() > 0) {
            crud.put("E" + getEventType() + "_Directions", getDirectionsField().getText());
        }
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        TitledBorder padBorder = new TitledBorder(XHIBITConstant.getResource(XhibitBundles.Directions,
                "DirectionsBorder"));
        Border border2 = BorderFactory.createCompoundBorder(padBorder, BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setBorder(border2);

        this.add(getDirectionsScroll(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private JTextArea getDirectionsField() {
        if (directionsText == null) {
            directionsText = JTextAreaFactory.getTextArea();
            directionsText.addKeyListener(new UpdateStateKeyListener(this));
        }

        return directionsText;
    }

    public String getDirectionsText() {
        return directionsText.getText();
    }

    private JScrollPane getDirectionsScroll() {
        if (directionsScroll == null) {
            final Dimension defaultSize = new Dimension(100, XHIBITConstant.getLineHeight() * 3);

            directionsScroll = new JScrollPane(getDirectionsField());
            directionsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            directionsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            directionsScroll.setMinimumSize(defaultSize);
            directionsScroll.setPreferredSize(defaultSize);
        }

        return directionsScroll;
    }

    public void stepValidate() throws CSValidationException {
        stepValidate(false);
    }

    public void stepValidate(boolean directionsTextIsMandatory) throws CSValidationException {
        if (getDirectionsField().getText().trim().length() == 0) {
            if (directionsTextIsMandatory) {
                getDirectionsField().requestFocus();
                throw new CSValidationException("validation.mandatoryfield", new Object[] { XHIBITConstant.getResource(
                        XhibitBundles.Directions, "DirectionsBorder") },
                        "Directions text is mandatory when editing the event");
            }
        }
    }

    public void stepUpdateViewState() {
    }
    
    /**
     * Truncates the "param" string to a maximum of "maxLength" utf8 characters
     * @param param
     * @param maxLength
     * @return the truncated string
     */
    private String truncate( String param, int maxLength ) {
        UTF8LimitedTextValidatingDocumentDecorator doc = new UTF8LimitedTextValidatingDocumentDecorator( maxLength );
        
        String returnString = param;
        while( !doc.validate( returnString ) ) {
            returnString = returnString.substring( 0, returnString.length() - 1 );
        }
        
        return returnString; 
    }
}
