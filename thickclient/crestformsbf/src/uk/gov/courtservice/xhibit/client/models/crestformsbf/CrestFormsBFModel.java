package uk.gov.courtservice.xhibit.client.models.crestformsbf;

import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XSLServices;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFForm;
import uk.gov.courtservice.xhibit.client.crestformsbf.util.Util;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: The Crest Form B - F Model
 * </p>
 * <p>
 * Description: The crest form b - f model.
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
public class CrestFormsBFModel {

    /*
     * Logger
     */
    private static final Logger log = CSServices.getLogger(CrestFormsBFModel.class);

    /**
     * Resource name, and keys
     */
    private static final String PROPERTIES_NAME = "crestformsbf";

    private static final String XSL_SCHEMA_KEY = "xsl.schema";

    private static final String XML_PREFIX_KEY = "xml.prefix";

    private static final String XML_POSTFIX_KEY = "xml.postfix";

    /**
     * Cached resources
     */
    private static final String XSL_SCHEMA = getProperty(XSL_SCHEMA_KEY);

    private static final String XML_PREFIX = getProperty(XML_PREFIX_KEY);

    private static final String XML_POSTFIX = getProperty(XML_POSTFIX_KEY);

    /**
     * The forms that make up the model
     */
    private final CrestFormsBFForm[] forms;

    /**
     * The name of the court clerk
     */
    private final String courtClerkName;

    /**
     * The indicies of the selected forms
     */
    private int[] selectedIndicies;

    /**
     * Scheduled Hearing for the selected case
     */
    private final Integer scheduledHrgId;

    /**
     * Construct a CrestFormsBFModel for the given caseId
     * 
     * @param caseId
     */
    public CrestFormsBFModel(Integer caseId, Integer scheduledHearingId) {
        forms = XhibitDelegateHelper.getCrestFormsBFDelegate().getForms(caseId);
        // get the court clerk name from the session
        courtClerkName = XhibitSingleton.getInstance().getUserSession().getUserName();
        scheduledHrgId = scheduledHearingId;

        selectedIndicies = new int[0];
    }

    /**
     * Number of forms in the model
     * 
     * @return an <code>int</code> containing the number of forms in the model
     */
    public int getFormCount() {
        return forms.length;
    }

    /**
     * Get a form from the model
     * 
     * @return the form from the model
     * @throws IllegalArgumentException
     *             if the index is invalid
     */
    public CrestFormsBFForm getForm(int index) {
        if (index < 0 || index >= forms.length) {
            throw new IllegalArgumentException("index:" + index);
        }
        return forms[index];
    }

    /**
     * Get the formated xml for the current selection
     * 
     * @return a <code>String</code> object containing the formated xml EC
     *         change comments
     */
    public String getFormatedXml() throws CrestFormBFXMLException {
        return XSLServices.getInstance().transform(getXml(), getProperty(XSL_SCHEMA_KEY), Locale.getDefault(), null);
    }

    /**
     * Get the xml for the current selection
     * 
     * @return a <code>String</code> object containing the xml EC change
     *         comments
     */
    public String getXml() throws CrestFormBFXMLException {
        String[] xml = XhibitDelegateHelper.getCrestFormsBFDelegate().getXml(getSelectedForms(), courtClerkName,
                scheduledHrgId);

        /*
         * if(log.isDebugEnabled()) { log.debug("xml.length: " + xml.length);
         * for(int i = 0; i < xml.length; i++) { log.debug("xml[" + i + "]: " +
         * xml[i]); } }
         */

        StringBuffer buffer = new StringBuffer(XML_PREFIX);
        for (int i = 0; i < xml.length; i++) {
            buffer.append(xml[i]);
        }
        buffer.append(XML_POSTFIX);

        String result = buffer.toString();
        log.debug("result: " + result);
        return result;
    }

    /**
     * Have any forms been selected
     * 
     * @return <code>true</code> if any forms have been selected, otherwise
     *         false
     */
    public boolean hasSelected() {
        return selectedIndicies.length > 0;
    }

    /**
     * Update the indicies selected by this model
     * 
     * @param selectedIndicies
     *            an <code>int</code> array containing the indicies of the
     *            selected forms
     */
    public void setSelectedIndicies(int[] selectedIndicies) {
        if (!areFormIndiciesValid(selectedIndicies)) {
            throw new IllegalArgumentException("selectedIndicies: " + Util.valueOf(selectedIndicies));
        }
        this.selectedIndicies = selectedIndicies;
    }

    /**
     * The selected Indicies
     * 
     * @return an <code>int</code> array containing the indicies of the
     *         selected forms, returns an empty array if none are selected
     */
    public int[] getSelectedIndicies() {
        return selectedIndicies;
    }

    /**
     * The selected forms
     * 
     * @return an array containing the forms
     */
    public CrestFormsBFForm[] getSelectedForms() {
        CrestFormsBFForm[] selectedForms = new CrestFormsBFForm[selectedIndicies.length];
        for (int i = 0; i < selectedIndicies.length; i++) {
            selectedForms[i] = forms[selectedIndicies[i]];
        }
        return selectedForms;
    }

    /**
     * Is the index a valid form index
     * 
     * @param index
     *            the form index to check
     * @return <code>true</code> if form index is valid, otherwise false
     */
    private boolean isFormIndexValid(int index) {
        return index >= 0 && index < forms.length;
    }

    /**
     * Are the indicies valid form indicies
     * 
     * @param indicies
     *            the form indices to check
     * @return <code>true</code> if the indicies are all valid form indicies,
     *         otherwise false
     */
    private boolean areFormIndiciesValid(int[] indicies) {
        if (indicies == null) {
            return false;
        } else {
            for (int i = 0; i < indicies.length; i++) {
                if (!isFormIndexValid(indicies[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Get a property from the crestformsbf component properties
     * 
     * @key the name of the key
     * @return the keyed property
     * @throws IllegalArgumentException
     *             if the key does not exist in the collection
     */
    private static String getProperty(String key) throws IllegalArgumentException {
        if (key != null) {
            String property = getProperties().getProperty(key);
            if (property != null) {
                return property;
            }
        }
        throw new IllegalArgumentException("key: " + key);
    }

    /**
     * Get properties collection for crestformsbf (consider caching)
     * 
     * @return the crestformsbf properties collection
     */
    private static Properties getProperties() {
        return CSServices.getConfigServices().getProperties(PROPERTIES_NAME);
    }

}
