package uk.gov.courtservice.xhibit.client.search;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: XHIBIT2 XHIBITSearchPanel
 * </p>
 * <p>
 * Description: super class of the xhibit search panels (criteria, results,
 * details)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version $Revision: 1.3 $
 */
public class XHIBITSearchPanel extends XPanel {
    protected final Logger log = CSServices.getLogger(getClass());

    protected GridBagConstraints gbc;

    protected JLabel stepTitle;

    protected JLabel stepDescription;

    protected XHIBITSearch xsSearch;

    public XHIBITSearchPanel(XHIBITSearch xsSearch) {
        super();
        this.xsSearch = xsSearch;
        /*
         * try { //this.jbInit(); - children must do this in their jbInit; }
         * catch(Exception e) { XHIBITConstant.handleError(e); }
         */
    }

    protected void jbInit() {
        // removeAll();
        setLayout(new GridBagLayout());
        setName(XHIBITConstant.getResource(XhibitBundles.XhibitSearch, "xs.gen.name"));

        gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.nonContainerInsets, 0, 0);
        stepTitle.setFont(stepTitle.getFont().deriveFont(java.awt.Font.BOLD));
        doAdd(stepTitle, gbc);

        gbc = new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.nonContainerInsets, 0, 0);
        doAdd(stepDescription, gbc);
    }

    protected void doAdd(JComponent jc, GridBagConstraints o) {
        add(jc, o);
    }

    // Empty methods declared here so that you do not need to implement
    // in children of this class if not required.
    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }

    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }

    public void stepValidate() throws uk.gov.courtservice.framework.exception.CSRecoverableException,
            uk.gov.courtservice.framework.services.validation.CSValidationException {
    }

    public void stepUpdateViewState() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }

    public void stepDeinitialise(boolean update) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }

    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }
}