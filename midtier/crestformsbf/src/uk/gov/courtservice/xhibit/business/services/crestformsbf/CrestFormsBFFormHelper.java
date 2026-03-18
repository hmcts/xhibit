package uk.gov.courtservice.xhibit.business.services.crestformsbf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.CrestFormsBFDatabase;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFForm;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFType;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFXmlHelper;

/**
 * <p>
 * Title: The Crest Form B - F Form Helper
 * </p>
 * <p>
 * Description: An abstract helper for producing forms, concrete implementations
 * produce a specific type of form. These factories are the server side form
 * functionality
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

public abstract class CrestFormsBFFormHelper {

    /**
     * Resource name, and keys
     */
    private static final String PROPERTIES_NAME = "crestformsbf";

    private static final String FORM_KEY_PREFIX = "form.";

    private static final String FORM_LIST_KEY = FORM_KEY_PREFIX + "list";

    private static final String FORM_CLASS_KEY_POSTIFX = ".class";

    private static final String LIST_SEPERATOR_CHARACTERS = ",";

    /**
     * Cached list of available helpers
     */
    private static CrestFormsBFFormHelper[] helpers;

    /**
     * Get the xml documents for the forms in the array
     * 
     * @return a <code>String</code> array populated with the xml for the
     *         forms specified in the parameter.
     * @throws CrestFormBFXMLException
     *             if an error occures
     */
    public static String[] getXml(CrestFormsBFForm[] forms, String courtClerkName, Integer scheduledHearingId)
            throws CrestFormBFXMLException {
        String[] xml = new String[forms.length];
        for (int i = 0; i < xml.length; i++) {
            xml[i] = getXml(forms[i], courtClerkName, scheduledHearingId);
        }
        return xml;
    }

    /**
     * Get xml for the given form
     */
    private static String getXml(CrestFormsBFForm form, String courtClerkName, Integer scheduledHearingId)
            throws CrestFormBFXMLException {
        return getHelper(form.getType()).getFormXml(form.getCase(), form.getDefendant(), courtClerkName,
                scheduledHearingId);
    }

    /**
     * Returns an array containing the current forms for a case
     * 
     * @param caze
     *            the case to retrieve the forms for
     * @return an array containing the current forms for the specified case
     * @throws CSUnrecoverableException
     *             if an error occurs
     */
    public static CrestFormsBFForm[] getForms(CrestFormsBFCase caze) throws CSUnrecoverableException {
        List formList = getFormList(caze);
        CrestFormsBFForm[] forms = new CrestFormsBFForm[formList.size()];
        for (int i = 0; i < forms.length; i++) {
            forms[i] = (CrestFormsBFForm) formList.get(i);
        }
        return forms;
    }

    /*
     * Get the forms for the case and any linked cases
     */
    private static List getFormList(CrestFormsBFCase caze) throws CSUnrecoverableException {
        List formList = new ArrayList();

        HashSet caseSet = new HashSet();

        caseSet.add(caze);

        CrestFormsBFCase[] linkedCases = CrestFormsBFDatabase.getLinkedCases(caze);
        for (int i = 0; i < linkedCases.length; i++) {
            caseSet.add(linkedCases[i]);
        }

        CrestFormsBFCase[] consolidatedCases = CrestFormsBFDatabase.getConsolidatedCases(caze);
        for (int i = 0; i < consolidatedCases.length; i++) {
            caseSet.add(consolidatedCases[i]);
        }

        Iterator cases = caseSet.iterator();
        while (cases.hasNext()) {
            addForms(formList, (CrestFormsBFCase) cases.next());
        }
        return formList;
    }

    /*
     * Get the forms for all the defendants on the case
     */
    private static void addForms(List formList, CrestFormsBFCase caze) {
        CrestFormsBFDefendant[] defendants = CrestFormsBFDatabase.getDefendantsOnCase(caze);
        for (int i = 0; i < defendants.length; i++) {
            addForms(formList, caze, defendants[i]);
        }
    }

    /*
     * Check each possible form type to see if it is valid for a given case and
     * defendant
     */
    private static void addForms(List formList, CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        CrestFormsBFFormHelper[] helpers = getHelpers();
        for (int i = 0; i < helpers.length; i++) {
            if (helpers[i].isValidForm(caze, defendant)) {
                formList.add(helpers[i].createForm(caze, defendant));
            }
        }
    }

    /**
     * Return the helper for the specified type
     * 
     * @throws CSUnrecoverableException
     *             if a helper can not be found for the type
     */
    private static CrestFormsBFFormHelper getHelper(CrestFormsBFType type) throws CSUnrecoverableException {
        CrestFormsBFFormHelper[] helpers = getHelpers();
        for (int i = 0; i < helpers.length; i++) {
            if (helpers[i].type.equals(type)) {
                return helpers[i];
            }
        }
        throw new CSUnrecoverableException("Could not find helper for type " + type);
    }

    /**
     * Returns an array containing the available helpers. These are loaded from
     * a property file and represents the set of available forms
     * 
     * @return an array containing the available helpers
     */
    private static CrestFormsBFFormHelper[] getHelpers() {
        if (helpers == null) {
            helpers = createHelpers();
        }
        return helpers;
    }

    private static CrestFormsBFFormHelper[] createHelpers() {
        List helperList = createHelperList();
        CrestFormsBFFormHelper[] helpers = new CrestFormsBFFormHelper[helperList.size()];
        for (int i = 0; i < helpers.length; i++) {
            helpers[i] = (CrestFormsBFFormHelper) helperList.get(i);
        }
        return helpers;
    }

    private static List createHelperList() {
        List typeList = new ArrayList();
        Iterator typeNames = getProperties(FORM_LIST_KEY);
        while (typeNames.hasNext()) {
            typeList.add(getFormHelperProperty(FORM_KEY_PREFIX + (String) typeNames.next() + FORM_CLASS_KEY_POSTIFX));
        }
        return typeList;
    }

    /**
     * Get the defendant on case id for the given defendant and case
     */
    private static Integer getDefendantOnCaseId(CrestFormsBFCase caze, CrestFormsBFDefendant defendant)
            throws CSUnrecoverableException {
        Integer defendantOnCaseId = CrestFormsBFDatabase.getDefendantOnCaseId(caze, defendant);
        if (defendantOnCaseId == null) {
            throw new CSUnrecoverableException("Could not get defendant on case id for defendant " + defendant
                    + " on case " + caze);
        }
        return defendantOnCaseId;
    }

    /**
     * The type this helper is helping
     */
    private final CrestFormsBFType type;

    /**
     * Cached version of xml helper
     */
    private CrestFormBFXmlHelper xmlHelper;

    /**
     * Construct a helper for the given type name
     */
    protected CrestFormsBFFormHelper(String name) {
        this(new CrestFormsBFType(name));
    }

    /**
     * Construct a helper for the given type name with a different display name
     */
    protected CrestFormsBFFormHelper(String name, String displayName) {
        this(new CrestFormsBFType(name, displayName));
    }

    /**
     * Construct a helper for the given type
     */
    protected CrestFormsBFFormHelper(CrestFormsBFType type) {
        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }
        this.type = type;
    }

    /**
     * Get the type for the helper
     * 
     * @return the type
     */
    public CrestFormsBFType getType() {
        return type;
    }

    /**
     * Creates a new instance of the form for the given case and defendant
     * 
     * @param caze
     * @param defendant
     * @return a new instance of the form for the given case and defendant
     */
    public CrestFormsBFForm createForm(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        return new CrestFormsBFForm(caze, defendant, getType());
    }

    /**
     * Returns true if the form is valid for the defendant on the given case
     * 
     * @param caze
     * @param defendant
     * @return true if the form is valid for the defendant on the given case
     */
    public abstract boolean isValidForm(CrestFormsBFCase caze, CrestFormsBFDefendant defendant);

    /**
     * Returns true if the form is valid for the defendant on the given case
     * 
     * @param caze
     * @param defendant
     * @return true if the form is valid for the defendant on the given case
     * @throws CrestFormBFXMLException
     *             if a problem occures generating the xml
     */
    public String getFormXml(CrestFormsBFCase caze, CrestFormsBFDefendant defendant, String courtClerkName,
            Integer scheduledHearingId) throws CrestFormBFXMLException {
        Integer defendantOnCaseId = getDefendantOnCaseId(caze, defendant);
        return getXmlHelper().getCrestFormXml(defendantOnCaseId, courtClerkName, scheduledHearingId);
    }

    /**
     * Get an instance of the XmlHelper for this helper
     * 
     * @return the cached version of the xmlHelper
     */
    public CrestFormBFXmlHelper getXmlHelper() {
        if (xmlHelper == null) {
            xmlHelper = createXmlHelper();
        }
        return xmlHelper;
    }

    /**
     * Get an instance of the XmlHelper for this helper
     * 
     * @return a new instance of the xmlHelper
     */
    public abstract CrestFormBFXmlHelper createXmlHelper();

    /**
     * Get the name of the type we are helping
     * 
     * @return the name of the type we are helping
     */
    public String getName() {
        return getType().getName();
    }

    /**
     * @return a <code>String</code>containing a representation of this
     *         object
     */
    public String toString() {
        return type.toString();
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param object
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public final boolean equals(Object object) {
        return object instanceof CrestFormsBFFormHelper && equals((CrestFormsBFFormHelper) object);
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param type
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public boolean equals(CrestFormsBFFormHelper helper) {
        return helper != null && type.equals(helper.type);
    }

    /**
     * Returns a hashcode for this object, implemented for the benifit of
     * collections.
     * 
     * @return a hash code value for this object
     */
    public int hashCode() {
        return type.hashCode();
    }

    //
    // Typed Property management
    //

    /**
     * Get a form helper from the crestformsbf component properties
     * 
     * @key the name of the key
     * @return an instance of the form helper named by the key
     * @throws CSUnrecoverableException
     *             if the property is not a valid type
     */
    private static CrestFormsBFFormHelper getFormHelperProperty(String key) throws CSUnrecoverableException {
        Class clazz = getClassProperty(key);
        try {
            return (CrestFormsBFFormHelper) clazz.newInstance();
        } catch (InstantiationException ie) {
            throw getTypePropertyException(clazz, key, ie);
        } catch (IllegalAccessException iae) {
            throw getTypePropertyException(clazz, key, iae);
        } catch (ClassCastException cce) {
            throw getTypePropertyException(clazz, key, cce);
        }
    }

    private static CSUnrecoverableException getTypePropertyException(Class clazz, String key, Throwable cause) {
        return new CSUnrecoverableException("Class " + clazz.getName() + " keyed by " + key + " in properties "
                + PROPERTIES_NAME + " is not a valid CrestFormsBFFormHelper.", cause);
    }

    /**
     * Get a class property from the crestformsbf component properties
     * 
     * @key the name of the key
     * @return the keyed property
     * @throws CSUnrecoverableException
     *             if the key does not exist or is not a valid class
     */
    private static Class getClassProperty(String key) throws CSUnrecoverableException {
        String property = getProperty(key);
        try {
            return CrestFormsBFFormHelper.class.getClassLoader().loadClass(property);
        } catch (ClassNotFoundException cnfe) {
            throw new CSUnrecoverableException("Could not find class " + property + " keyed by " + key
                    + " in properties " + PROPERTIES_NAME + ".", cnfe);
        }
    }

    /**
     * Get a property from the crestformsbf component properties
     * 
     * @key the name of the key
     * @return the keyed property
     * @throws CSUnrecoverableException
     *             if the key does not exist in the collection
     */
    private static String getProperty(String key) throws CSUnrecoverableException {
        if (key != null) {
            String property = getProperties().getProperty(key);
            if (property != null) {
                return property;
            }
        }
        throw new CSUnrecoverableException("Could not find key " + key + " in properties " + PROPERTIES_NAME + ".");
    }

    /**
     * Get an iterator over a list defined in the keyed properties
     * 
     * @key the name of the key
     * @return the keyed property
     * @throws CSUnrecoverableException
     *             if the key does not exist or is not a valid class
     */
    private static Iterator getProperties(String key) {
        final String property = getProperty(key);
        return new Iterator() {
            StringTokenizer tokenizer = new StringTokenizer(property, LIST_SEPERATOR_CHARACTERS);

            public boolean hasNext() {
                return tokenizer.hasMoreTokens();
            }

            public Object next() {
                return tokenizer.nextToken().trim();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
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
