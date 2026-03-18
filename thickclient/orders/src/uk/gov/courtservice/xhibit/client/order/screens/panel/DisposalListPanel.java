package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;

/**
 * <p>
 * Title: Xhibit2 DisposalListPanel
 * </p>
 * <p>
 * Description: Displays a list of disposals for a defendant
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class DisposalListPanel extends AbstractOrdersPanel implements ActionListener {
    private static final Logger log = CSServices.getLogger(DisposalListPanel.class);

    /**
     * 
     * @param odm
     */
    public DisposalListPanel(OrderInitialDataVO model) throws CSRecoverableException {
        super(model);

        initialisePanel();
    }

    /**
     * 
     */
    public DisposalListPanel() throws CSRecoverableException {
        super();

        initialisePanel();

    }

    /**
     * 
     */
    protected void initialisePanel() throws CSRecoverableException {
        super.initialisePanel();
        addComponents();

        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), ResourceHelper
                .getResourceString("DisposalPanelTitle")));
    }

    /**
     * 
     */
    private void addComponents() {
        add(getDisposalLabel(), getConstraints());
        ++getConstraints().gridx;
        add(getDisposalList(), getConstraints());
    }

    /**
     * 
     * @return
     */
    private JLabel getDisposalLabel() {
        return new JLabel(ResourceHelper.getResourceString("DisposalLabel"));
    }

    /**
     * 
     * @return
     */
    private JComponent getDisposalList() {
        JList listBox = new JList(getDisposalsForCase());
        listBox.setVisibleRowCount(4);
        JScrollPane scrollPane = new JScrollPane(listBox);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        return scrollPane;
    }

    /**
     * 
     * @return
     */
    private String[] getDisposalsForCase() {
        String[] dispNames = { "Bail", "Bench Warrant", "CPO", "CPRO", "CRO", "Imprisonment" };
        return dispNames;
    }

    /**
     * 
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        log.debug("Selection: " + ((JList) e.getSource()).getSelectedValue().toString());
        /** @todo Implement this abstract method */
    }

    /**
     * 
     * @param odm
     */
    public void setPanelFromModel(OrderInitialDataVO model) {
        /** @todo Implement setPanelFromModel method */
    }

    /**
     * 
     */
    public void updateModel() {
        /** @todo Implement setPanelFromModel method */
    }
}