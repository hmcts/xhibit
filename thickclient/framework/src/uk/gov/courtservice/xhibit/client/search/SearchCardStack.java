package uk.gov.courtservice.xhibit.client.search;

import java.awt.CardLayout;

import javax.swing.BorderFactory;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Panel to hold and control indivual panels used for searching
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
 * @version $Id: SearchCardStack.java,v 1.3 2006/06/05 12:30:29 bzjrnl Exp $
 */
public class SearchCardStack extends XPanel {
    private CardLayout cardLayout = new CardLayout();

    private XPanel currentPanel = null;

    public SearchCardStack(XPanel[] panels) {
        super();
        setLayout(cardLayout);
        this.setBorder(BorderFactory.createEtchedBorder());
        for (int i = 0; i < panels.length; i++) {
            panels[i].setVisible(true);
            this.add(panels[i], panels[i].getClass().getName());
        }
    }

    public void show(XPanel panel) {
        currentPanel = panel;
        cardLayout.show(this, panel.getClass().getName());
    }

    // Delegate life cycle method calls to currently visible window.
    public void stepInitialise() throws CSRecoverableException {
        if (currentPanel != null)
            currentPanel.stepInitialise();
    }

    public void stepDeactivate() throws CSRecoverableException {
        if (currentPanel != null)
            currentPanel.stepDeactivate();
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        if (currentPanel != null)
            currentPanel.stepValidate();
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        if (currentPanel != null)
            currentPanel.stepUpdateViewState();
    }

    public void stepDeinitialise(boolean parm1) throws CSRecoverableException {
        if (currentPanel != null)
            currentPanel.stepDeinitialise(parm1);
    }

    public void stepActivate() throws CSRecoverableException {
        if (currentPanel != null)
            currentPanel.stepActivate();
    }

	/**
	 * @return the currentPanel
	 */
	public XPanel getCurrentPanel() {
		return currentPanel;
	}
}