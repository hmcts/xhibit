package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;

/**
 * <p>
 * Title: UncodedOffencePanel
 * </p>
 * <p>
 * Description: The panel for displaying uncoded offence related information.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version $Id: UncodedOffencePanel.java,v 1.16 2006/06/05 12:31:29 bzjrnl Exp $
 * 
 */
public class UncodedOffencePanel extends XPanel {
    /**
     * Logger logger
     */
    private static final Logger LOG = CSServices.getLogger(UncodedOffencePanel.class);

    /**
     * Maximum size of descriptions
     */
    private static final int TOTAL_DESCRIPTIONS_SIZE = 240;

    /**
     * The '*' character
     */
    private static final String ASTERISK = "*";

    /**
     * UncodedOffenceModel model
     */
    private final UncodedOffenceModel model;

    /**
     * Resources at config.bundles.XHIBITChargesResources
     */
    private ResourceBundle myResources;

    /**
     * JLabel lblHODesc
     */
    private JLabel lblHODesc = new JLabel();

    /**
     * JLabel lblRSDesc
     */
    private JLabel lblRSDesc = new JLabel();

    /**
     * JLabel lblHOClass
     */
    private JLabel lblHOClass = new JLabel();

    /**
     * JLabel lblHOSubClass
     */
    private JLabel lblHOSubClass = new JLabel();

    /**
     * JLabel lblDVLCClass
     */
    private JLabel lblDVLCClass = new JLabel();

    /**
     * JTextField txtFldHODesc
     */
    private JTextField txtFldHODesc = new JTextField();

    /**
     * JTextField txtFldRSDesc
     */
    private JTextField txtFldRSDesc = new JTextField();

    /**
     * JTextField txtFldHOClass
     */
    private JTextField txtFldHOClass = new JTextField();

    /**
     * JTextField txtFldHOSubClass
     */
    private JTextField txtFldHOSubClass = new JTextField();

    /**
     * Create an UncodedOffencePanel.
     * 
     * @param uod
     *            UncodedOffenceDialog
     */
    public UncodedOffencePanel(UncodedOffenceDialog uod) {
        this(uod.getController().getModel());
    }

    /**
     * Create an UncodedOffencePanel.
     * 
     * @param uod
     *            UncodedOffenceModel
     */
    public UncodedOffencePanel(UncodedOffenceModel uom) {
        myResources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);
        this.model = uom;
        try {
            stepInitialise();
        } catch (CSRecoverableException ex) {
            LOG.error(ex.getMessage());
        }
    }

    /**
     * stepInitialise
     * 
     * @throws CSRecoverableException -
     */
    public void stepInitialise() throws CSRecoverableException {
        this.setLayout(new GridBagLayout());
        this.setDebugGraphicsOptions(0);

        final Capability[] classCapabilities = new Capability[] { Capability.numeric(), Capability.limitedText(3) };

        final Capability[] subclassCapabilities = new Capability[] { Capability.numeric(), Capability.limitedText(2) };

        KeyAdapter keyAdapter = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                try {
                    stepValidate();
                } catch (CSRecoverableException ex) {
                    XHIBITConstant.handleError(ex);
                }
            }
        };

        txtFldHOClass = JTextFieldFactory.getTextField("", keyAdapter, 3, null, null, null, true, "", DocumentFactory
                .newDocument(classCapabilities));

        txtFldHOClass.setMinimumSize(new Dimension(40, 20));

        txtFldHOSubClass = JTextFieldFactory.getTextField("", keyAdapter, 2, null, null, null, true, "",
                DocumentFactory.newDocument(subclassCapabilities));

        txtFldHOSubClass.setMinimumSize(new Dimension(40, 20));

        lblHODesc.setText(myResources.getString("HODesc.label"));
        txtFldHODesc.setDocument(DocumentFactory.newDocument(new Capability[] { Capability
                .utf8LimitedTextCapability(TOTAL_DESCRIPTIONS_SIZE - 1) }));

        lblRSDesc.setText(myResources.getString("RSDesc.label"));
        txtFldRSDesc.setDocument(DocumentFactory.newDocument(new Capability[] { Capability
                .utf8LimitedTextCapability(TOTAL_DESCRIPTIONS_SIZE - 1) }));

        lblDVLCClass.setText(myResources.getString("DVLCClass.label"));
        lblDVLCClass.setFont(lblDVLCClass.getFont().deriveFont(Font.ITALIC));

        lblHOClass.setText(myResources.getString("HOClass.label"));
        lblHOSubClass.setText(myResources.getString("HOSubclass.label"));

        this.add(lblHODesc, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(lblRSDesc, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(lblDVLCClass, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(lblHOClass, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(lblHOSubClass, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

        this.add(txtFldHODesc, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
        this.add(txtFldRSDesc, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
        this.add(txtFldHOClass, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(txtFldHOSubClass, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    }

    /**
     * updateModel
     */
    private void updateModel() {
        model.setHoDesc(txtFldHODesc.getText());
        model.setRsDesc(txtFldRSDesc.getText());
        model.setHoClass(txtFldHOClass.getText());
        model.setHoSubclass(txtFldHOSubClass.getText());
    }

    /**
     * stepActivate
     * 
     * @throws CSRecoverableException -
     */
    public void stepActivate() {
        moveModelToScreen();
    }

    /**
     * stepUpdateViewState
     * 
     * @throws CSRecoverableException -
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        stepValidate();
        updateModel();
    }

    /**
     * stepValidate
     * 
     * @throws CSValidationException -
     * @throws CSRecoverableException -
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        if ((txtFldHODesc.getText().indexOf(ASTERISK) != -1) || (txtFldRSDesc.getText().indexOf(ASTERISK) != -1)) {
            throw new CSValidationException("gui.uncodedOffence.starInText", "one of the descriptions has *");
        }

        StringBuffer buf = new StringBuffer();
        buf.append(txtFldHODesc.getText());
        buf.append(ASTERISK);
        buf.append(txtFldRSDesc.getText());
        final int utf8Length = getUTF8Length(buf.toString());
        if (utf8Length > TOTAL_DESCRIPTIONS_SIZE) {
            throw new CSValidationException("gui.uncodedOffence.freeTextDescriptionTooLong", new String[] { String
                    .valueOf(utf8Length - TOTAL_DESCRIPTIONS_SIZE) },
                    "UTF8 encoded length of HODesc + '*' + RSDesc exceeds " + TOTAL_DESCRIPTIONS_SIZE);
        }
    }

    private int getUTF8Length(String str) {
        try {
            return str.length() == 0 ? 0 : str.getBytes("UTF-8").length;
        } catch (java.io.UnsupportedEncodingException uee) {
            return str.length() * 3;
        }
    }

    /**
     * stepDeactivate
     * 
     * @throws CSRecoverableException -
     */
    public void stepDeactivate() throws CSRecoverableException {
    }

    /**
     * stepDeinitialise
     * 
     * @param update
     *            parameter for stepDeinitialise
     * @throws CSRecoverableException -
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    /**
     * gettxtFldHODesc
     * 
     * @return the returned JTextField
     */
    public JTextField gettxtFldHODesc() {
        return txtFldHODesc;
    }

    /**
     * gettxtFldRSDesc
     * 
     * @return the returned JTextField
     */
    public JTextField gettxtFldRSDesc() {
        return txtFldRSDesc;
    }

    /**
     * gettxtFldHOClass
     * 
     * @return the returned JTextField
     */
    public JTextField gettxtFldHOClass() {
        return txtFldHOClass;
    }

    /**
     * gettxtFldHOSubClass
     * 
     * @return the returned JTextField
     */
    public JTextField gettxtFldHOSubClass() {
        return txtFldHOSubClass;
    }

    /**
     * getlblHOClass
     * 
     * @return the returned JLabel
     */
    public JLabel getlblHOClass() {
        return lblHOClass;
    }

    /**
     * getlblHOSubClass
     * 
     * @return the returned JLabel
     */
    public JLabel getlblHOSubClass() {
        return lblHOSubClass;
    }

    /**
     * getlblDVLCClass
     * 
     * @return the returned JLabel
     */
    public JLabel getlblDVLCClass() {
        return lblDVLCClass;
    }

    public void updateModel(UncodedOffenceModel uom) {
        model.setHoDesc(uom.getHoDesc());
        model.setRsDesc(uom.getRsDesc());
        model.setHoClass(uom.getHoClass());
        model.setHoSubclass(uom.getHoSubclass());
        model.setRefOffenceDesc(uom.getRefOffenceDesc());
    }

    public void moveModelToScreen() {
        gettxtFldHODesc().setText(model.getHoDesc());
        gettxtFldRSDesc().setText(model.getRsDesc());
        gettxtFldHOClass().setText(model.getHoClass());
        gettxtFldHOSubClass().setText(model.getHoSubclass());
    }

    public void setEnabled(final boolean enable) {
        LOG.debug("setEnabled(" + enable + ")");
        lblHODesc.setEnabled(enable);
        lblRSDesc.setEnabled(enable);
        lblDVLCClass.setEnabled(enable);
        lblHOClass.setEnabled(enable);
        lblHOSubClass.setEnabled(enable);

        gettxtFldHODesc().setEnabled(enable);
        gettxtFldRSDesc().setEnabled(enable);
        gettxtFldHOClass().setEnabled(enable);
        gettxtFldHOSubClass().setEnabled(enable);
        // super.setEnabled(enable);
    }

    public void setEditable(final boolean editable) {
        LOG.debug("setEditable(" + editable + ")");
        gettxtFldHODesc().setEditable(editable);
        gettxtFldRSDesc().setEditable(editable);
        gettxtFldHOClass().setEditable(editable);
        gettxtFldHOSubClass().setEditable(editable);
        // super.setEnabled(enable);
    }
}
