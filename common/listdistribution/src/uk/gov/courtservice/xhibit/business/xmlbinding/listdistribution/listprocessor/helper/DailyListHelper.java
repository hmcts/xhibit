package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import java.io.Reader;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyList;

/**
 * <p>
 * Title: DailyListHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating DailyList XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DailyListHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class DailyListHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(DailyListHelper.class);

    /**
     * Unmarshal the DailyList from the reader
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
    public static DailyList unmarshal(ClassDescriptorResolver cdr, boolean validation, Reader in)
            throws MarshalException, ValidationException {
        if (log.isDebugEnabled()) {
            long startTime = System.currentTimeMillis();
            DailyList DailyList = _unmarshal(cdr, validation, in);
            log.debug("Unmarshaling daily list took " + (System.currentTimeMillis() - startTime) + "ms.");
            return DailyList;
        } else {
            return _unmarshal(cdr, validation, in);
        }
    }

    // Factored to facilitate logging
    private static final DailyList _unmarshal(ClassDescriptorResolver cdr, boolean validation, Reader in)
            throws MarshalException, ValidationException {
        Unmarshaller unmarshaller = new Unmarshaller(DailyList.class);
        unmarshaller.setResolver(cdr);
        unmarshaller.setValidation(validation);
        return (DailyList) unmarshaller.unmarshal(in);
    }
}
