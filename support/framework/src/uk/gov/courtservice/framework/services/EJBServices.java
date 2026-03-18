package uk.gov.courtservice.framework.services;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: EJBServices
 * </p>
 * <p>
 * Description: Provides a group of ejb related services. Wroks with the
 * LocatorService to find and create EJBs. Provodes utility methods for copy
 * data to and from ejb and value objects
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version 1.0
 */
public interface EJBServices {

    /**
     * Creates an entity from a class. The JNDI name of the class so if the
     * class DefendantRemoteHome.class is passed in the JNDI name
     * DefendantRemoteHome will be used. The JNDI lookup will be performed only
     * from the configured subcontext for this framework application.
     * 
     * @param klass
     *            the Class file to be used for the lookup
     * @param attributes
     *            the Map of key/value attributes to be used in creating the
     * @return an Entity EJB representing the newly created object.
     * @throws a
     *             CSResourceUnavailableException if the ejb cannot be located
     */
    // public EJBLocalObject createLocalEntity(CSValueObject value)
    // throws CSUnrecoverableException, UnmatchedMethodException;
    /**
     * Creates a Session bean using the remote interface based on the JNDI name
     * of the class. So if the class DefendantControllerRemoteHome.class is
     * passed in the JNDI name DefendantControllerRemoteHome will be used as the
     * JNDI name. The JNDI lookup will be performed only from the configured
     * subcontext for this framework application.
     * 
     * @param klass
     *            the Class file to be used for the lookup
     * @return a Session EJB representing the newly created object.
     * @throws a
     *             CSResourceUnavailableException if the ejb cannot be located
     */
    public EJBObject createRemoteSession(Class klass) throws CSUnrecoverableException;

    /**
     * Creates a Session bean using the local interface based on the JNDI name
     * of the class. So if the class DefendantControllerHome.class is passed in
     * the JNDI name DefendantControllerHome will be used as the JNDI name. The
     * JNDI lookup will be performed only from the configured subcontext for
     * this framework application.
     * 
     * @param klass
     *            the Class file to be used for the lookup
     * @return a Session EJB representing the newly created object.
     * @throws a
     *             CSResourceUnavailableException if the ejb cannot be located
     */
    public EJBLocalObject createLocalSession(Class klass) throws CSUnrecoverableException;

    /**
     * Converts the serialized string into a EJBHandle then to EJBObject.
     * Normally used to recreate an ejb after a pause in the workflow.
     * 
     * @param serializedHomeHandle
     *            the String to convert to a EJBObject
     * @returns an EJBObject for the string.
     * @throws a
     *             CSResourceUnavailableException if the conversion fails
     */
    public EJBObject getEJBObject(String serializedHomeHandle) throws CSUnrecoverableException;

    /**
     * Converts the EJBObject into a serialized string. Normally used to store
     * an ejb during a pause in the workflow.
     * 
     * @param objectToSerialize
     *            the EJBObject to convert to serialized form.
     * @returns the EJBObject serilized to String
     * @throws a
     *             CSResourceUnavailableException if the conversion fails
     */
    public String getSerializedEJBObject(EJBObject objectToSerialize) throws CSUnrecoverableException;

    /**
     * Copies the contents of the value to the ejb or the ejb to the value.
     * Relies on the set methods of the target matching the get methods of the
     * source. So for example the method getCourtCaseId on the value object
     * would need to be matched exactly to the setCourtCaseId method on the
     * EJBObject. The data type must also match. If there is a method in on the
     * value which is not matched on the ejb an exception is thrown
     * 
     * @param value
     *            the ValueObject to copy the data from.
     * @param the
     *            ejb to copy the data to
     * @throws a
     *             CSResourceUnavailableException if the copy fails - normally
     *             due to a non matching method or data type
     */
    // public void copyAttributes(Object source, Object target)
    // throws UnmatchedMethodException;
    /**
     * Calculates the jndi name for a class base don the convention that each
     * class is bound to the jndi tree with the same name as its class.
     * 
     * @param klass
     *            the Class to find the name for
     * @returns the String for the JNDI name
     */
    public String getJndiName(Class ejbHome);

    /**
     * Finds as EJB Entity using the EJBLocalHome. First finds the EJBLocalHome
     * via the JNDi service and then creates the EJBObject using the Integer
     * primary key. For example if the the call
     * <code>findLocalEntityByPrimaryKey(DefendantHome.class, new Integer(1));</code>
     * was made then a Defendant would be returned. As the return type is
     * EJBLocalObject this would need to be cast to a Defendant.
     * 
     * @param ejbLocalHomeClass
     *            the Class to find the JNDI name from
     * @param primarykey
     *            the Integer for the entity primary key.
     * @returns the EJBLocalObject for the for corresponding Entity. The
     *          returned object must be cast to the correct type.
     * @throws CSResourceUnavailableException
     *             if the object cannot be located
     */
    public EJBLocalObject findLocalEntityByPrimaryKey(Class ejbLocalHomeClass, Integer primaryKey)
            throws CSUnrecoverableException, ObjectNotFoundException;

    /**
     * Deletes an EJB Entity using the EJBLocalHome. First finds the
     * EJBLocalHome via the JNDi service and the primary key and then calls
     * <code>remove()</code> For example if the the call
     * <code>deleteLocalEntity(DefendantHome.class, new Integer(1));</code>
     * was made then a Defendant with primry key 1 would be deleted.
     * 
     * @param ejbLocalHomeClass
     *            the Class to find the JNDI name from
     * @param primarykey
     *            the Integer for the entity primary key.
     * @returns the EJBLocalObject for the for corresponding Entity. The
     *          returned object must be cast to the correct type.
     * @throws CSResourceUnavailableException
     *             if the object cannot be located
     */
    public void deleteLocalEntity(Class ejbLocalHomeClass, Integer primarykey) throws CSUnrecoverableException;

}