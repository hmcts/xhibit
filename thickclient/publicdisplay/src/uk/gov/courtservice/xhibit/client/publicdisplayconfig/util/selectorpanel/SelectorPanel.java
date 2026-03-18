package uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: SelectorPanel.java,v 1.3 2006/06/05 12:32:08 bzjrnl Exp $
 */

public class SelectorPanel extends XPanel {
    private final XPanel _parent;

    public SelectorPanel(XPanel parent, AbstractSelectorPanel fromPanel, AbstractSelectorPanel toPanel)
            throws CSRecoverableException {
        super(new GridBagLayout());
        if (fromPanel == null || toPanel == null)
            throw new IllegalArgumentException("From and To Views can not be null");
        _parent = parent;
        init(fromPanel, toPanel);
    }

    private void init(AbstractSelectorPanel fromPanel, AbstractSelectorPanel toPanel) throws CSRecoverableException {
        SelectorButtonPanel buttonPanel = new SelectorButtonPanel(this, fromPanel, toPanel);

        this.add(fromPanel, new GridBagConstraints(0, 0, 1, 1, 0.5, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(buttonPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(toPanel, new GridBagConstraints(2, 0, 1, 1, 0.5, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        if (_parent != null) {
            _parent.stepUpdateViewState();
        }
    }

    public void stepInitialise() {
    }

    public void stepActivate() {
    }

    public void stepValidate() {
    }

    public void stepDeactivate() {
    }

    public void stepDeinitialise(boolean save) {
    }
}