package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title:
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

public class DirectionsSingleEventDialog extends XDialog {

    public DirectionsSingleEventDialog(Frame frame, XhibitApplicationController xac, CourtLogCRUDValue crud)
            throws CSRecoverableException {
        super(frame, "", true);
        boolean isInEditMode = (crud.getId() != null && crud.getId().intValue() > 0);
        if (isInEditMode) {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelEdit"));
        } else {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelAdd"));
        }
        addBodyPanel(new DirectionSingleEventPanel(xac, crud));
        pack();
    }
}