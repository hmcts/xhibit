package uk.gov.courtservice.xhibit.client.search;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
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
 * @author Frederik Vandendriessche
 * @version $Revision: 1.3 $
 */
public abstract class XHIBITSearchDetails extends XHIBITSearchStep {
    private CSValueObject detailsObject = null;

    private Class detailsClass = null;

    protected Vector theDetailsKeys = new Vector(); // the attribute names

    protected Hashtable theDetails = new Hashtable(); // the attribute name of

    // the Criteria object
    // is always the key

    protected Hashtable theDetailsLabels = new Hashtable(); // the attribute

    // name of the
    // Criteria
    // object is
    // always the
    // key

    protected Hashtable theDetailsComponents = new Hashtable();// the attribute

    // name of the
    // Criteria
    // object is
    // always the
    // key

    public XHIBITSearchDetails() {
        super();
        setVODetails(getDetailsValueObject());
        setDetails();
    }

    public CSValueObject getDetailsObject() {
        return this.detailsObject;
    }

    public Class getDetailsClass() {
        return this.detailsClass;
    }

    private void setVODetails(CSValueObject o) {
        this.detailsObject = o;
        this.detailsClass = o.getClass();
    }

    // implementors must return a value object of the type they are to be
    // displaying on the details screen
    public abstract CSValueObject getDetailsValueObject();

    // not required by XHIBITSearch for XHIBITSearchDetails
    // XHIBITSearchStep, right now.
    // the current working assumption is that a Results Value Object will be
    // passed to the details
    public Class[] getParameterTypes() {
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.client.search.XHIBITSearchStep
         *       abstract method
         */
        throw new java.lang.UnsupportedOperationException("Method getParameterTypes() not yet implemented.");
    }

    // not required by XHIBITSearch for XHIBITSearchDetails
    // XHIBITSearchStep, right now.
    // the current working assumption is that a Results Value Object will be
    // passed to the details
    public String getMethodName() {
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.client.search.XHIBITSearchStep
         *       abstract method
         */
        throw new java.lang.UnsupportedOperationException("Method getMethodName() not yet implemented.");
    }

    /*
     * implementors of the XHIBITSearchCriteria (typically a subclass of an
     * vos.services.systemadmin.criteria class) must return a collection of
     * member attributes that are to be used as criteria on the screen.
     * 
     * they do so by invoking addCriteriaField(fieldName) a number of times
     * 
     */
    public abstract void setDetails();

    protected void addDetail(String fieldName, String resourceKey, JComponent component) {
        if (this.detailsObject == null) {
            log.error(".addDetail(): Exception whilst trying to add detail with name '" + fieldName + "'.");
            log.error(".addDetail(): Must do setDetailsValueObject(CSValueObject o) in constructor.");
        } else {
            try {
                // Field field =
                // detailsObject.getClass().getDeclaredField(fieldName);
                // Field field = detailsObject.getClass().getField(fieldName);
                Object field = new Object();
                theDetailsKeys.add(fieldName);
                theDetails.put(fieldName, field);
                theDetailsLabels.put(fieldName, XHIBITConstant.getResource(XhibitBundles.XhibitSearch, resourceKey));
                theDetailsComponents.put(fieldName, component);
            } catch (Exception e) {
                log.error(".addDetail(): Exception whilst trying to add detail attribure with name '" + fieldName
                        + "'.");
                log.error(e);
            }
        }
    }

    protected void addDetail(String fieldName, String resourceKey) {
        addDetail(fieldName, resourceKey, new JLabel());
    }

	public Vector getTheDetailsKeys() {
		return theDetailsKeys;
	}

	public Hashtable getTheDetails() {
		return theDetails;
	}

	public Hashtable getTheDetailsComponents() {
		return theDetailsComponents;
	}

	public Hashtable getTheDetailsLabels() {
		return theDetailsLabels;
	}

	
    // public String getStepTitle()
    // {
    // return new String("XHIBIT Search Details");
    // }
    // public String getStepDescription()
    // {
    // return new String("hit ok to use this results only, or hit back to go
    // back to the list with all results.");
    // }

}