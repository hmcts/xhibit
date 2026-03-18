package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import java.io.Reader;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmList;

/**
 * <p>
 * Title: FirmListHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating FirmList and FirmList Summary XML
 * bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: FirmListHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class FirmListHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(FirmListHelper.class);

    /**
     * Unmarshal the firmList from the reader
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
    public static FirmList unmarshal(ClassDescriptorResolver cdr, boolean validation, Reader in)
            throws MarshalException, ValidationException {
        if (log.isDebugEnabled()) {
            long startTime = System.currentTimeMillis();
            FirmList firmList = _unmarshal(cdr, validation, in);
            log.debug("Unmarshaling firm list took " + (System.currentTimeMillis() - startTime) + "ms.");
            return firmList;
        } else {
            return _unmarshal(cdr, validation, in);
        }
    }

    // Factored to facilitate logging
    private static final FirmList _unmarshal(ClassDescriptorResolver cdr, boolean validation, Reader in)
            throws MarshalException, ValidationException {
        Unmarshaller unmarshaller = new Unmarshaller(FirmList.class);
        unmarshaller.setResolver(cdr);
        unmarshaller.setValidation(validation);
        return (FirmList) unmarshaller.unmarshal(in);
    }
}
