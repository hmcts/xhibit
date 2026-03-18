package uk.gov.courtservice.xhibit.client.util;

/**
 * <p>Title: Xhibit2</p>
 * <p>Description: Court Services Application</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author David Crossland
 * @version 1.0
 * Test Case Ref    Date          Author        Description
 *
 * 52366            21-03-2003    CP Davies    Deleted SetPreferredSize & SetMinimumSize
 *                                              lines in getFreeTextArea to force scroll
 *                                              bars to work
 */
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import uk.gov.courtservice.framework.services.validation.CSValidationException;

public class FreeTextPanel extends JPanel {

    private JPanel freeTextContainer;

    private String ftPanelResources = XhibitBundles.UtilResources;

    private Dimension panelDim;

    private JScrollPane freetextScrollPanel = new JScrollPane();

    private JTextArea freeTextArea;

    private String freeText;

    private boolean required = false;

    public FreeTextPanel(JPanel containingPanel, Dimension pDim) {
        this.freeTextContainer = containingPanel;
        this.panelDim = pDim;
        stepInitialise();
        jbInit();
    }

    public FreeTextPanel(JPanel containingPanel) {
        this(containingPanel, new Dimension(260, 120));
    }

    private void jbInit() {
        addFreeTextField();
        moveModelToScreen();

        freetextScrollPanel.setMinimumSize(panelDim);
        freetextScrollPanel.setPreferredSize(panelDim);
        freetextScrollPanel.getViewport().add(getFreeTextArea(), null);

        /** @todo add listener and popup */
        MouseListener popupListener = new FreetextPopupListener();
        getFreeTextArea().addMouseListener(popupListener);

    }

    private void stepInitialise() {
    }

    private void addFreeTextField() {

        this.setLayout(new GridBagLayout());
        this.setPreferredSize(panelDim);
        this.setMinimumSize(panelDim);
        this.setMaximumSize(panelDim);

        this.add(freetextScrollPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
    }

    public JTextArea getFreeTextArea() {
        if (freeTextArea == null) {
            freeTextArea = new JTextArea();
            freeTextArea.setBorder(null);
            freeTextArea.setToolTipText(XHIBITConstant.getResource(ftPanelResources, "ttFreeText"));
            freeTextArea.setLineWrap(true);
            freeTextArea.setWrapStyleWord(true);
            freeTextArea.setFont(XHIBITConstant.getCurrentFont());

            freetextScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            freetextScrollPanel.setAutoscrolls(true);
            freetextScrollPanel.setToolTipText(XHIBITConstant.getResource(ftPanelResources, "ttFreeText"));

            freeTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    freeTextArea_keyReleased(e);
                }
            });
        }
        return freeTextArea;
    }

    // class EventDocListener implements DocumentListener {
    //
    // public void insertUpdate(DocumentEvent e) {
    // // enableOkButton();
    // // not necessary any more
    // }
    //
    // public void removeUpdate(DocumentEvent e) {
    // //no need to fire these events
    // }
    //
    // public void changedUpdate(DocumentEvent e) {
    // //Plain text components don't fire these events
    // }
    // }

    private void moveModelToScreen() {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("In moveModelToScreen");
    }

    public String getFreeText() {
        freeText = freeTextArea.getText();
        return freeText;
    }

    // stully
    public void setFreeTextArea(String param) {
        freeTextArea.setText(param);
    }

    void freeTextArea_keyReleased(KeyEvent e) {
        try {
            if (freeTextContainer instanceof XPanel) {
                ((XPanel) freeTextContainer).stepUpdateViewState();
            }
        } catch (Exception ex) {
            XHIBITConstant.handleError(ex);
        }
    }

    public void setRequired(boolean newValue) {
        required = newValue;
    }

    public boolean getRequired() {
        return required;
    }

    public void stepValidate() throws CSValidationException {
        if (required) {
            if (!isMandatoryFieldsCompleted()) {
                throw new CSValidationException("validation.required", new String[] { "Text Area" },
                        "Freetext field not completed");
            }
        }
    }

    public boolean isMandatoryFieldsCompleted() {
        boolean returnCode = true;

        if (freeTextArea.getText().trim().length() == 0) {
            returnCode = false;
        }

        return returnCode;
    }

    public int getScrollPanelWidth() {
        return freetextScrollPanel.getWidth();
    }

    private JPopupMenu thisPopup = null;

    private JPopupMenu getPopup() {
        JMenuItem menuItem;

        if (thisPopup == null) {
            thisPopup = new DefaultPopup();
        }

        return thisPopup;
    }

    // Right Click Listener
    class FreetextPopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                getPopup().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

}