package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import java.io.Reader;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedList;

/**
 * <p>
 * Title: WarnedListHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating WarnedList XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WarnedListHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class WarnedListHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(WarnedListHelper.class);

    /**
     * Unmarshal the warnedList from the reader
     * 
     * @param cdr
     *            the Class Descriptor Resolver, reusing this stops castor
     *            reloading descriptions.
     * @param validation
     *            pass in true to validate
     * @param out
     *            the writer to marshal to
     * @return the list
     */
    public static WarnedList unmarshal(ClassDescriptorResolver cdr, boolean validation, Reader in)
            throws MarshalException, ValidationException {
        if (log.isDebugEnabled()) {
            long startTime = System.currentTimeMillis();
            WarnedList warnedList = _unmarshal(cdr, validation, in);
            log.debug("Unmarshaling warnded list took " + (System.currentTimeMillis() - startTime) + "ms.");
            return warnedList;
        } else {
            return _unmarshal(cdr, validation, in);
        }
    }

    // Factored to facilitate logging
    private static final WarnedList _unmarshal(ClassDescriptorResolver cdr, boolean validation, Reader in)
            throws MarshalException, ValidationException {
        Unmarshaller unmarshaller = new Unmarshaller(WarnedList.class);
        unmarshaller.setResolver(cdr);
        unmarshaller.setValidation(validation);
        return (WarnedList) unmarshaller.unmarshal(in);
    }
}
