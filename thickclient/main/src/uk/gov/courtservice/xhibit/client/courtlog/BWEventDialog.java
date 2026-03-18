package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author J Parthiban
 * @version 1.0
 */
public class BWEventDialog extends XDialog {
	
    public BWEventDialog(Frame frame, MediumEventMLModel model) throws CSRecoverableException {
    	
        super(frame, "", true);
        
        if (model.isInEditMode()) {
            setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelEdit"));
        } else {
            setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelSelectDefendant"));
        }

        addBodyPanel(new BWEventPanel(this, model));
        pack();
   
    }
  
}
