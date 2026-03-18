package uk.gov.courtservice.xhibit.client.results.authorise;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthoriseWarning;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;


/**
 * <p>
 * Title: DisposalDateWarningPanel
 * </p>
 * <p>
 * Description: Panel to display disposals which have a 'result date'
 * on a non-hearing day.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class DisposalDateWarningPanel extends XPanel {
    
    private static final long serialVersionUID = 1L;
    
    
    public DisposalDateWarningPanel(AuthoriseWarning[] warnings) 
    throws CSRecoverableException{       
        this.setLayout(new GridBagLayout());
        jbInit(warnings);        
    }
    
    /**
     * Initialise GUI components
     * @param AuthoriseCheckValue[] values
     * @throws CSRecoverableException
     */
    private void jbInit(AuthoriseWarning[] warnings) 
    throws CSRecoverableException {
        add(getWarningTextPanel(), getGridConstraints(0));
        add(getWarningsTable(warnings), getGridConstraints(1));
    }
    
    
    private JScrollPane getWarningsTable(AuthoriseWarning[] warnings) {
        JTable myTable = XTableFactory.getInstance().createDefaultTable(
                new DisposalDateWarningTableModel(warnings));
        
        JScrollPane scrollPane = new JScrollPane(myTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(700, XHIBITConstant.getLineHeight() * 7));
        return scrollPane;
    }
    
    
    private GridBagConstraints getGridConstraints(int gridy) {
        return 
            new GridBagConstraints(0, gridy, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
    }
    
    
    private JPanel getWarningTextPanel() {
        JPanel panel = new JPanel(new GridLayout(3,1));
        panel.add(getLabel("results.authorise.warningdialog.label.1"));
        panel.add(getLabel("results.authorise.warningdialog.label.2"));
        panel.add(getLabel("results.authorise.warningdialog.label.3"));
        return panel;
    }
    
    
    private JLabel getLabel(String resourceKey) {
        return new JLabel(
                ResourceBundleHelper.getResource(
                        XhibitBundles.CaseProgressResources, 
                        resourceKey));
    }
    
    @Override
    public void stepActivate() throws CSRecoverableException {
        // empty
    }

    @Override
    public void stepDeactivate() throws CSRecoverableException {
        //empty
    }

    @Override
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // empty
    }

    @Override
    public void stepInitialise() throws CSRecoverableException {
        // empty
    }

    @Override
    public void stepUpdateViewState() throws CSRecoverableException {
        // empty
    }

    @Override
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // empty
    }
}
