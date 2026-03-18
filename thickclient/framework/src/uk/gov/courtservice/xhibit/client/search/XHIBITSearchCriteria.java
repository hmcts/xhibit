package uk.gov.courtservice.xhibit.client.search;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtReporterCriteria;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT2 SearchCriteria
 * </p>
 * <p>
 * Description: This object gathers all criteria information from a search
 * implementation.
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
public abstract class XHIBITSearchCriteria extends XHIBITSearchStep {
    private AbstractSearchCriteria searchCriteria = null;

    private Vector theCriteriaKeys = new Vector(); // the attribute names

    protected Hashtable theCriteria = new Hashtable(); // the attribute name of

    // the Criteria object
    // is always the key

    protected Hashtable theCriteriaLabels = new Hashtable(); // the attribute

    // name of the
    // Criteria
    // object is
    // always the
    // key

    private Hashtable theCriteriaComponents = new Hashtable();// the attribute

    // name of the
    // Criteria
    // object is
    // always the
    // key

    // the key is the field name (which is used to look up the field
    // from theCriteria and the value is the value to be set in the field
    // (defaultValue)
    protected Hashtable theHiddenCriteria = new Hashtable();

    public XHIBITSearchCriteria() {
        super();
        setSearchCriteria();
    }

    // returns the CriteriumValueObject to be passed in the search method.
    public AbstractSearchCriteria getSearchCriteria() {
        return this.searchCriteria;
    }

    // implementors must return the class of the search method value object
    // argument
    public abstract Class getCriteriaValueClass();

    /*
     * implementors of the XHIBITSearchCriteria (typically a subclass of an
     * vos.services.systemadmin.criteria class) must return a collection of
     * member attributes that are to be used as criteria on the screen.
     * 
     * they do so by invoking addCriteriaField(fieldName) a number of times
     * 
     */
    public abstract void setCriteria();

    private void setSearchCriteria() {
        try {
            this.searchCriteria = (AbstractSearchCriteria) this.getCriteriaValueClass().newInstance();
            log.debug(".setSearchCriteria(): searchCriteria=" + this.searchCriteria);

            // this is being done here, as the detailindicator is not made
            // available in the interface of the real criteria class
            if (this.searchCriteria instanceof RefCourtReporterCriteria) {
                this.searchCriteria.setDetailIndicator("YES");
            }
        } catch (Exception e) {
            log.debug(".setSearchCriteria(): Exception whilst creation instance of getCriteriaValueClass.");
            log.debug(e);
        }
    }

    protected void addCriteria(XHIBITSearchCriteriaValue criteria) {
        if (this.searchCriteria == null) {
            log.error(".addCriteria(): Exception whilst trying to add criteria with name '" + criteria.getFieldName()
                    + "'.");
            log
                    .error(".addCriteria(): Must do setSearchCriteria(AbstractSearchCriteria isearchCriteria) in constructor.");
        } else {
            try {
                Class[] interfaces = searchCriteria.getClass().getDeclaredClasses(); // getdecinterf:-)
                // log.debug("___ interfaces: " + interfaces.length );
                int interfaceBrowser = 0;
                boolean attributesInterfaceFound = false;
                Class attributesDef = null;
                while ((interfaceBrowser < interfaces.length) && !attributesInterfaceFound) {
                    attributesDef = interfaces[interfaceBrowser];
                    if (attributesDef.getName().endsWith("AttributeNames")) {
                        attributesInterfaceFound = true;
                    }

                    interfaceBrowser++;
                }
                if (attributesInterfaceFound) {
                    // Field field =
                    // attributesDef.getDeclaredField(fieldName);

                    if ("detailIndicator".equals(criteria.getFieldName())) {
                        // this field is not made available via the interface -
                        // as it is implementation stuff!
                        // field = new Field();
                        // theCriteria.put(fieldName, "");
                    } else {
                        Field field = attributesDef.getField(criteria.getFieldName());
                        theCriteria.put(criteria.getFieldName(), field);
                    }

                    if (criteria.getComponent() != null || criteria.isVisible()) {
                        getTheCriteriaKeys().add(criteria.getFieldName());

                        if ((criteria.getResourceKey() != null) && (!criteria.getResourceKey().equals(""))) {
                            theCriteriaLabels.put(criteria.getFieldName(), XHIBITConstant.getResource(
                                    XhibitBundles.XhibitSearch, criteria.getResourceKey()));
                        } else {
                            theCriteriaLabels.put(criteria.getFieldName(), "");
                        }
                        getTheCriteriaComponents().put(criteria.getFieldName(), criteria.getComponent());
                    } else {
                        // no component or invisible therefore hidden variable
                        theHiddenCriteria.put(criteria.getFieldName(), criteria.getDefaultValue());
                    }
                } else {
                    throw new Exception("Could not find interface AttributeNames in criteria class.");
                }
            } catch (Exception e) {
                log.error(".addCriteria(): Exception whilst trying to add criteria with name '"
                        + criteria.getFieldName() + "'.");
                log.error(e);
                e.printStackTrace();
            }
        }
    }

    protected void addCriteria(String fieldName, String resourceKey, JComponent component) {
        // if the component is null no point in this object!
        if (component != null) {
            XHIBITSearchCriteriaValue criteriaValue = new XHIBITSearchCriteriaValue(fieldName, resourceKey, component,
                    true, "");
            addCriteria(criteriaValue);
        }
    }

    public Hashtable getHiddenCriteria() {
        return theHiddenCriteria;
    }

    /**
     * @param fieldName
     * @param resourceKey
     * @deprecated the preferred method is to create a XHIBITSearchCriteriaValue
     */
    protected void addCriteria(String fieldName, String resourceKey) {
        // JTextField jtf = new JTextField();
        // jtf.setColumns(15);
        //
        // addCriteria(fieldName, resourceKey, jtf);
        XHIBITSearchCriteriaValue criteriaValue = new XHIBITSearchCriteriaValue(fieldName, resourceKey);
        addCriteria(criteriaValue);
    }

    /**
     * @param fieldName
     * @param visible
     * @deprecated the preferred method is to create a XHIBITSearchCriteriaValue
     *             and pass it in.
     */
    protected void addCriteria(String fieldName, boolean visible) {
        if (fieldName.equals("COURT_ID")) {
            String defaultValue = XhibitSingleton.getInstance().getCourtId().toString();
            XHIBITSearchCriteriaValue criteriaValue = new XHIBITSearchCriteriaValue(fieldName, "", null, visible,
                    defaultValue);
            addCriteria(criteriaValue);
        }
        // addCriteria(fieldName, "", null);
    }

    // public String getStepTitle()
    // {
    // return new
    // String(XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
    // "xs.gen.CriteriaCard.title"));
    // }
    // public String getStepDescription()
    // {
    // return new
    // String(XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
    // "xs.gen.CriteriaCard.description"));
    // }

    // Search implementors may override this method (should only do so it
    // the
    // setSearchCriteria() argument is not the only argument of the search
    // method
    public Class[] getParameterTypes() {
        Vector parameterTypes = new Vector();
        parameterTypes.add(getCriteriaValueClass());
        Class[] paraTypes = {};

        paraTypes = (Class[]) parameterTypes.toArray(paraTypes);
        log.debug(".getParameterTypes(): returns: " + paraTypes);
        return paraTypes;
    }

	public Vector getTheCriteriaKeys() {
		return theCriteriaKeys;
	}

	public void setTheCriteriaKeys(Vector theCriteriaKeys) {
		this.theCriteriaKeys = theCriteriaKeys;
	}

	public Hashtable getTheCriteriaComponents() {
		return theCriteriaComponents;
	}

	public void setTheCriteriaComponents(Hashtable theCriteriaComponents) {
		this.theCriteriaComponents = theCriteriaComponents;
	}

	public Hashtable getTheCriteria() {
		return theCriteria;
	}

	public Hashtable getTheCriteriaLabels() {
		return theCriteriaLabels;
	}
	
	
}
