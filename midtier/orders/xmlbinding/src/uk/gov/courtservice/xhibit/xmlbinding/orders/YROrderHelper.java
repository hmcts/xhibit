package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Calendar;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.YouthRehabilitationOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.DetentionTypeType;

/**
 * <p/> Title: Helper class that populates the Castor bound java-XML object for
 * the YouthRehabilitationOrderStructure
 * </p>
 * <p/> Description:
 * </p>
 * <p/> Populates the YouthRehabilitationOrderStructure for 'new Youth Rehabilitation order'.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Brian Hingston
 */
//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class YROrderHelper {
    // set up logger
    private static final Logger log = CSServices.getLogger(YROrderHelper.class);

    /**
     * Utility method to populate an YouthRehabilitationOrderStructure from the rest
     * of the Xhibit entities.
     * 
     * @param yros
     *            The YouthRehabilitationOrderStructure to be populated (pass by
     *            reference).
     * @param docEntity
     *            The defendant on case EB for which to populate the order.
     * @param rehab
     *            Whether this Community order incorporates rehabilitation.
     * @param typeCode
     *            The type code of the order.
     * @throws OrderXMLException
     *             When there is a problem in population.
     */

    public static void populateYouthRehabilitationOrder(YouthRehabilitationOrderStructure yros, DefendantOnCase docEntity)
            throws OrderXMLException {
        populateCaseDetails(yros, docEntity);
    }

    /**
     * Populate the Youth Rehabilitation order structure with all relevant case details
     * 
     * @param yros
     *            the Youth Rehabilitation order structure
     * @param docEntity
     *            the defendant on case
     * @param caseEntity
     *            the case
     * @throws OrderXMLException
     */
    private static void populateCaseDetails(YouthRehabilitationOrderStructure yros, DefendantOnCase docEntity)
            throws OrderXMLException {
        //populateWarningType(yros, docEntity);
    }

    

}
