package uk.gov.courtservice.xhibit.client.util;

import java.util.Collection;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.InputVerifier;
import javax.swing.JPanel;
import javax.swing.text.Document;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import mseries.ui.MNationalityEntryField;
import mseries.ui.MNationalityField;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;


/*
 * XNationalityPanel for selection of nationality.  
 * Modelled on XDatePanel for selection of date.
 */
public class XNationalityPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private static final String nationalityToolTip = "NationalityComponent";
    
    public static final String nationalityError = "validation.nationality";

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private boolean required = true;

    private ResourceBundle nationalityPanelResources;

    private JPanel nationalityContainer;

    private MNationalityEntryField entryField;

    private Collection<String> nationalities;
    

    public XNationalityPanel(
            JPanel containingPanel, 
            String defaultNationality,
            Collection<String> nationalities) {
        this.nationalityContainer = containingPanel;
        this.nationalities = nationalities;
        stepInitialise();
        init(defaultNationality, nationalities);
        stepActivate();
    }


    private void init(
            String defaultNationality,
            Collection<String> nationalities) {
        final Capability[] capabilities = new Capability[] { 
                Capability.upperCase(), 
                Capability.alphaNumeric(),
                Capability.limitedText(MNationalityField.NATIONALITY_CHARS) };

        // Create the text document with the required capabilities.
        final Document doc = DocumentFactory.newDocument(capabilities);
        entryField = new MNationalityEntryField(doc, nationalities);
        entryField.setEditable(true);
        entryField.setToolTipText(XHIBITConstant.getResource(nationalityPanelResources, nationalityToolTip));
        setNationality(defaultNationality);

        entryField.addMChangeListener(new MChangeListener() {
            public void valueChanged(MChangeEvent e) {
                entryField_changed(e);
            }
        });

        setLayout(gridBagLayout1);
        this.add(entryField, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    }

    public void stepInitialise() {
        nationalityPanelResources = XHIBITConstant.getResourceBundle(XhibitBundles.UtilResources);
    }

    public void stepActivate() {
        // empty
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        if (nationalityContainer instanceof XPanel) {
            ((XPanel) nationalityContainer).stepUpdateViewState();
        }

        //Check if ChildOfXPanel
        if (nationalityContainer instanceof ChildOfXPanel) {
            ((ChildOfXPanel) nationalityContainer).stepUpdateViewState();
        }
    }

    protected boolean isNationalityValid() {
        if (!entryField.isEditable() || !entryField.isEnabled())
            return true;

        if (entryField.getText().length() == 0 && !required) {
            return true;
        } else {
            if (entryField.getText() == null || entryField.getText().equals("")) {
                return false;
            }
            for (String str : nationalities) {
                if (str.length() >= 3 && str.substring(0, 3).equals(entryField.getText())) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * XPanel implementaion of life cycle method, called to validate the panel.
     * Checks if the nationality field is valid and if not, gives the field the focus
     * and display the appropriate error message. The parent panel can include a
     * call to this method in its own stepValidate method.
     * 
     * @throws CSValidationException
     *             if nationality field is invalid.
     */
    public void stepValidate() throws CSValidationException {
        if (!isNationalityValid()) {
            entryField.requestFocus();
            throw new CSValidationException(nationalityError, "string not valid nationality");
        }
    }

    public void stepDeactivate() {
        // empty
    }

    public void stepDeinitialise(boolean save) {
        // empty
    }

    /**
     * Allows access to the component for more advanced programming
     * 
     * @return
     */
    public MNationalityEntryField getNationalityComponent() {
        return entryField;
    }

    /**
     * Returns the nationality in the field as a String. Only a valid nationality will be
     * returned
     * 
     * @return
     * @throws CSValidationException
     */
    public String getNationality() throws CSValidationException {
        stepValidate();
        return getText();
    }

    /**
     * Return the text in the field. This does not guarantee the validity of the
     * nationality
     * 
     * @return
     */
    public String getText() {
        return entryField.getText();
    }

    /**
     * Set a new nationality for the field
     * 
     * @param nationality
     */
    public void setNationality(String nationality) {
        if (nationality == null) {
            entryField.setValue("");
        } else {
            entryField.setValue(nationality);
        }
    }

    /**
     * Set if the user is required to complete the field Defaults to true
     * 
     * @param newValue
     */
    public void setRequired(boolean newValue) {
        required = newValue;
    }

    /**
     * Set whether the display of the field is enabled. Defaults to true
     * 
     * @param newValue
     */
    public void setNationalityEnabled(boolean newValue) {
        entryField.setEnabled(newValue);

        if (newValue == false) {
            this.getNationalityComponent().setToolTipText(null);
        } else {
            // Setup tool tip text
            nationalityPanelResources = XHIBITConstant.getResourceBundle(XhibitBundles.UtilResources);
            this.getNationalityComponent().setToolTipText(
                    XHIBITConstant.getResource(nationalityPanelResources, nationalityToolTip));
        }
    }

    /**
     * Set whether the field can be editted Defaults to true
     * 
     * @param newValue
     */
    public void setNationalityEditable(boolean newValue) {
        entryField.setEditable(newValue);
    }

    /**
     * Check if the field is required and whether it has been populated.
     * 
     * @return true if there is a valid nationality. Also returns true if the field is
     *         not a required field.
     */
    public boolean isMandatoryFieldsCompleted() {
        if (required) {
            boolean fieldCompleted = false;
            if (isNationalityValid()) {
                fieldCompleted = true;
            }
            return fieldCompleted;
        } else {
            return true;
        }
    }

    /**
     * Install the default InputVerifier, currently an XNationalityVerifier, on this
     * XNationalityPanel
     */
    public void installDefaultInputVerifier() {
        entryField.setInputVerifier(new XNationalityVerifier(this));
    }

    /**
     * Sets the InputVerifier on this XNationalityPanel
     * 
     * @param inputVerifier
     *            InputVerifier to install on this XNationalityPanel
     */
    public void setInputVerifier(InputVerifier inputVerifier) {
        entryField.setInputVerifier(inputVerifier);
    }
    
    private void entryField_changed(MChangeEvent e) {
        if (e.getType() == MChangeEvent.PULLDOWN_OPENED || e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
            // If the chooser is being opened or closed the nationality will not
            // have changed.
            return;
        }
        try {
            stepUpdateViewState();
        } catch (CSRecoverableException ex) {
            XHIBITConstant.handleError(ex);
        }
    }

    public void addMChangeListener(MChangeListener listener) {
        entryField.addMChangeListener(listener);
    }
}