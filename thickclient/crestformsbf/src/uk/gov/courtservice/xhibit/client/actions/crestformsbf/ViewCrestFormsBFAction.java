package uk.gov.courtservice.xhibit.client.actions.crestformsbf;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.crestformsbf.ViewCrestFormsBFController;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: The Crest Form B - F View Action
 * </p>
 * <p>
 * Description: Open up the crest form b - f controller (dialog).
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */
public class ViewCrestFormsBFAction extends XAction {

    /*
     * Resource bundle key prefixs
     */
    private static final String VIEW_KEY_PREFIX = "ViewCrestFormsBF";

    /**
     * Construct the action populating the details from the actions resource
     * bundle
     */
    public ViewCrestFormsBFAction() {
        super(VIEW_KEY_PREFIX);
    }

    /**
     * This method is called when an Action is performed, open up a crest form b -
     * f controller (dialog).
     * </p>
     * 
     * @param e
     *            the action event that trigured this method to be called
     */
    public void xActionPerformed(ActionEvent e) throws Exception {
        ViewCrestFormsBFController vcfm = new ViewCrestFormsBFController((XhibitApplicationController) getController());
        vcfm.setVisible(true);
    }
}