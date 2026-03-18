package uk.gov.courtservice.xhibit.client.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.framework.client.delegate.CSBusinessDelegate;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Appli cation
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */
public abstract class XHIBITSearchCriteria extends XHIBITSearchArgumentSpec {
    /** @todo: author : set internalDebug to false */
    static boolean INTERNAL_DEBUG = true;

    // protected Class[] parameterTypes = { String.class, String.class,
    // String.class, String.class, String.class, String.class, String.class,
    // String.class};
    protected Vector parameterNames;

    /*
     * Each Criterium implementor stores the instances of criteria/bd search
     * method arguments.
     */
    protected Vector criteria;

    int criteriaCounter = 0;

    static String EMPTY_VALUE = "";

    /* @todo: author, editor : consolidate with mid tear implementation */
    static String wildCharAnyCharNTime = "";

    static String wildCharOneCharOneTime = "";

    public XHIBITSearchCriteria(XHIBITSearch xs) {
        super(xs);
        try {
            this.criteria = new Vector();
            this.parameterNames = new Vector();
        } catch (Exception e) {
            /**
             * @todo: write the appropriate exception - but really, when will
             *        the exception be thrown? during development - only!
             */
            XHIBITConstant.handleError(e);
        }
    }

    // Search implementors must implement this method
    public abstract void setCriteria();

    // Search implementors must implement this method
    public abstract String getMethodName();

    // Search implementors must implement this method
    public abstract Class[] getParameterTypes();

    // May be overriden by value object specific (implementing) subclasses.
    // your string will be displayed when the user executed a search that
    // did not return any results.
    public String getNoMatchesText() {
        return XHIBITConstant.getResource(xs.rsc, "xs.gen.nomatchestext");
    }

    // To be called by by value object specific (implementing) subclasses.
    // usually to indicate this criterium must not be shown
    /* @ todo: document the trick that will set it's value */
    public void addCriteria(Class klass, String attributeName, String label) {
        this.addCriteria(klass, attributeName, label, false);
    }

    public Method getBusinessDelegateMethod() {
        Method m = null;
        try {
            Class[] c = this.getParameterTypes();
            if (INTERNAL_DEBUG) {
                for (int x = 0; x < getParameterTypes().length; x++) {
                    xs.debug("XSCriteria : parameterType[" + x + "]=" + c[x]);
                }
                xs.debug("XSCriteria : methodName==" + getMethodName());
            }
            m = this.getBusinessDelegate().getMethod(getMethodName(), c);
        } catch (Exception e) {
            /* @todo: remove stacktrace print */
            e.printStackTrace();
            XHIBITConstant.error(e);
        }
        return m;
    }

    public void addCriteria(Class klass, String attributeName) {
        addCriteria(klass, attributeName, false);
    }

    public void addCriteria(Class klass, String attributeName, boolean hidden) {
        String label = XHIBITConstant.getResource(xs.rsc, "xs.gen." + XHIBITSearch.SEARCH_CRITERIA_CARD_ID + ".label");
        if (!hidden) {
            try {
                label = XHIBITConstant.getResource(xs.rsc, xs.getComponentResourceKey() + "." + attributeName);
            } catch (Exception e) {
                xs.debug("addCriteria could not find a customezied label from the atribute " + attributeName
                        + " in component " + xs.getComponentResourceKey());
            }
        }
        addCriteria(klass, attributeName, label, hidden);
    }

    public void addCriteria(Class klass, String attributeName, String label, boolean hidden) {

        this.parameterNames.add(attributeName);

        try {
            JComponent component = null;
            Object mappingObject = null;
            if ((klass == String.class) || (klass == Integer.class)) {
                component = new JTextField();
            } else {
                xs.debug("XSCriteria: its of type " + klass.toString()
                        + " and we don't really handle that. here's another JTextField.");
                component = new JTextField();
            }
            Criterium aCriterium = new Criterium(attributeName, klass, label, component, mappingObject, hidden);
            this.criteria.add(aCriterium);
            xs.debug("XSCriteria: added criterium " + aCriterium.toString());
        }

        catch (Exception e) {
            /**
             * @todo: remove e.printStackTrace() and wrap in appropriate
             *        exception
             */
            if (INTERNAL_DEBUG) {
                xs.debug("XSCriteria : Exception thrown in  addCriteria(String " + attributeName + ", String " + label
                        + ")");
                XHIBITConstant.error(e);
                e.printStackTrace();
            } else {
                XHIBITConstant.handleError(e);
                e.printStackTrace();
            }
        }
    }

    public void setCriteriaUI(String label, Object o) {
    }

    protected void executeSearch() {

        /**
         * @todo: CAN ALL OF THIS GO INTO AN ACTION? This will provide the best
         *        support to retry and handle exception.
         */

        if (this.businessDelegate == null)
            this.businessDelegate = this.getBusinessDelegate();
        if (this.businessDelegate == null) {
            /**
             * @todo: rework exception when invalid BusinessDelegateController
             */
            Exception e = new Exception("XHIBITSearchCriteria wrongly implemented. getBusinessDelegate() failed.");
            XHIBITConstant.handleError(e);
        } else {
            this.businessDelegateName = this.businessDelegate.getName();
            xs.debug("XSCriteria : The BusinessDelegate name is " + this.businessDelegateName);

            // fixing the name up
            int businessDelegateStartPos = this.businessDelegateName.indexOf("BusinessDelegate");
            this.businessDelegateShortName = this.businessDelegateName.substring(0, businessDelegateStartPos);
            // now loosing everything before the last . in the name (the
            // package location)
            this.businessDelegateShortName = this.businessDelegateShortName.substring(this.businessDelegateShortName
                    .lastIndexOf(".") + 1, this.businessDelegateShortName.length());
            xs.debug("XSCriteria : The businessDelegateShortName is " + this.businessDelegateShortName);

            try {
                if (this.businessDelegateMethod == null) {
                    this.businessDelegateMethod = this.getBusinessDelegateMethod();
                    if (this.businessDelegateMethod == null) {
                        throw new Exception("getBusinessDelegateMethod() did not set a Method but NULL!");
                    } else {
                        xs.debug("XSCriteria: businessDelegateMethod.getName()=="
                                + this.businessDelegateMethod.getName());
                    }
                }

                // Localised change to stop direct access to generic business
                // delegate factory. This change
                // was disigned to have the smallest possible impact. The way
                // this class creates its delegate
                // should be revisited.
                Class businessDelegateFactory = Class.forName(businessDelegate.getClass().getName()
                        + ".DelegateFactory");
                Method businessDelegateFactoryGetInstance = businessDelegateFactory.getMethod("getInstance",
                        (Class[]) null);
                CSBusinessDelegate dcBD = (CSBusinessDelegate) businessDelegateFactoryGetInstance.invoke(null,
                        (Object[]) null);

                // assemble the UI Input and parse them into the declared
                // criteria.
                Vector arguments = new Vector();

                Vector criteriumObjects = this.criteria;
                Iterator criteriumIterator = criteriumObjects.iterator();
                while (criteriumIterator.hasNext()) {
                    Criterium c = (Criterium) criteriumIterator.next();

                    Constructor constructor; // to construct and instance of
                    // criterium.type ??

                    // to capture vos for different types
                    String criteriaString = "";
                    Integer criteriaInteger = null;

                    if (INTERNAL_DEBUG) {
                        xs.debug("XSCriteria : Criteria : _start_criterium [" + c.name + "]");
                        xs.debug("XSCriteria : type= [" + c.type.toString() + "]");
                    }
                    if (c.name.equals("courtId") && c.hidden) {
                        Integer courtId = XhibitSingleton.getInstance().getCourtId();
                        xs.debug("XSCriteria : courtId from session " + courtId);
                        criteriaInteger = courtId;
                        arguments.add(criteriaInteger);
                    } else {
                        JTextComponent jtc = (JTextComponent) c.view;
                        // Class[] attributeTypeConstructorParameterTypes = {
                        // c.type };
                        Class[] attributeTypeConstructorParameterTypes = { String.class };
                        constructor = c.type.getConstructor(attributeTypeConstructorParameterTypes);

                        criteriaString = "" + jtc.getText();
                        if (criteriaString.equals(EMPTY_VALUE) && c.type.isAssignableFrom(String.class)) {
                            criteriaString = this.wildCharAnyCharNTime;
                        }

                        Object[] initargs = { criteriaString };
                        try {
                            arguments.add(constructor.newInstance(initargs));
                        } catch (Exception e) {
                            xs.debug("XSCriteria : could not instantiat the argument instanace. Going null.");
                            arguments.add(null);
                        }
                        if (INTERNAL_DEBUG) {
                            xs.debug("XSCriteria : constructor=[" + constructor.toString());
                            xs.debug("XSCriteria : instance= [" + "constructor.newInstance(initargs).toString()" + "]");
                            xs.debug("XSCriteria : instance class=["
                                    + "constructor.newInstance(initargs).getClass().toString()" + "]");
                        }
                    }
                    if (INTERNAL_DEBUG) {
                        if (criteriaInteger == null) {
                            xs.debug("XSCriteria String : value=[" + criteriaString + "]");
                        } else {
                            xs.debug("XSCriteria Integer : value=[" + criteriaInteger + "]");
                        }
                        xs.debug("XSCriteria : _end");
                    }

                }
                Object returnValue = null;
                try {
                    // now we invoke the method on the business delegate
                    // that does the search, using the arguments
                    // as collected above from the UI widgets on the the
                    // Search Criteria Card.
                    returnValue = this.businessDelegateMethod.invoke(dcBD, arguments.toArray());
                    if (INTERNAL_DEBUG)
                        xs.debug("XSCriteria : execute search : Return value class is "
                                + returnValue.getClass().toString());
                    if (INTERNAL_DEBUG)
                        xs.debug("XSCriteria : execute search : Return value is " + returnValue.toString());

                    Collection tempResults = new Vector();
                    if (returnValue == null) {
                        /*
                         * @@ todo: can this happen? check bd rules of
                         * interaction!
                         */
                        XHIBITConstant.error(new Exception("XSCriteria: execute search : A BD returned a NULL!!!"));
                    }
                    if (!(returnValue instanceof Collection)) {
                        tempResults.add(returnValue);
                        returnValue = tempResults;
                    }

                    // by now we're sure we've got a collection 'returned'
                    // (or not!) by the BD.
                    Collection results = (Collection) returnValue;

                    this.xs.setSearchResults(results);

                    if (results.size() == 0) {
                        xs.debug("XSCriteria : results.size() == 0 redraw first card or popup 'no matching data'");
                        xs.searchValueObjectCardStack.sop.stepNameLbl.setText("No Matching Results found.");
                    } else if (results.size() == 1) {
                        // go to the detail Card.
                        xs.debug("XSCriteria : results.size() == 1 go to the detail Card.'");
                        xs.searchValueObjectCardStack.sorp.resultsJTable.getSelectionModel().setSelectionInterval(0, 0);
                        xs.showCard(XHIBITSearch.SearchResultsItemDetailsCardID);
                    } else {
                        xs.debug("XSCriteria : results.size() == 2..n go to the list Card.'");
                        xs.showCard(XHIBITSearch.SEARCH_RESULT_LIST_CARD_ID);
                    }
                } catch (Exception e) {
                    XHIBITConstant.debug("Exception in XHIBITSearchCriteria (1): ");
                    XHIBITConstant.error(e);
                    XHIBITConstant.handleError(e);
                }
            } catch (Exception e) {
                XHIBITConstant.debug("Exception in XHIBITSearchCriteria: ");
                XHIBITConstant.error(e);
                XHIBITConstant.handleError(e);
            }
        }
    }

    /*
     * 
     * A Criterium maps to a) an argument passed to the Business Delegate search
     * method b) the visualisation of a criterium - generic (some/all may not be
     * shown!) c) value mapping between vos shown to the user and db vos
     * 
     */
    public class Criterium {
        public String name; // the attribute name the search method argument

        public String label; // UI Text identifying the criteria - this

        // argument

        public Class type; // the type of the attribute [ String, Integer,

        // ... ]

        public JComponent view; // all UI minus the label

        public Object valueMapping; // allows to map UI to specific vos that

        // match e.g. database 'flags'.

        public boolean hidden; // allows hidden criteria (court ID is always

        // one?)

        public Criterium(String name, Class type, String label, JComponent view, Object valueMapping, boolean hidden) {
            this.name = name;
            this.type = type;
            this.label = label;
            this.view = view;
            this.valueMapping = valueMapping;
            this.hidden = hidden;
        }
    }
}
