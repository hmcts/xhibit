package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Calendar;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CommunityOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.DetentionTypeType;

/**
 * <p/> Title: Helper class that populates the Castor bound java-XML object for
 * the CommunityOrderStructure
 * </p>
 * <p/> Description:
 * </p>
 * <p/> Populates the CommunityOrderStructure for 'new Community order'.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Neil Entwistle
 */
//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class XhibitCOMSENTOrderHelper {
    // set up logger
    private static final Logger log = CSServices.getLogger(XhibitCOMSENTOrderHelper.class);

    /**
     * Utility method to populate an XHIBITCommunityOrderStructure from the rest
     * of the Xhibit entities.
     * 
     * @param cos
     *            The XHIBITCommunityOrderStructure to be populated (pass by
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

    public static void populateXhibitCommunityOrder(CommunityOrderStructure cos, DefendantOnCase docEntity)
            throws OrderXMLException {
        populateCaseDetails(cos, docEntity);
    }

    /**
     * Populate the community order structure with all relevant case details
     * 
     * @param cos
     *            the community order structure
     * @param docEntity
     *            the defendant on case
     * @param caseEntity
     *            the case
     * @throws OrderXMLException
     */
    private static void populateCaseDetails(CommunityOrderStructure cos, DefendantOnCase docEntity)
            throws OrderXMLException {
        populateWarningType(cos, docEntity);
    }

    private static void populateWarningType(CommunityOrderStructure cos, DefendantOnCase docEntity) {

        java.util.Date defDOB = docEntity.getDefendant().getDateOfBirth();

        // defendant DOB can be null
        if (defDOB != null) {
            Calendar dob = Calendar.getInstance();
            dob.setTime(defDOB);

            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            dob.set(Calendar.YEAR, today.get(Calendar.YEAR));
            if (dob.after(today)) {
                age--;
            }

            cos.setWarningDetentionType((age >= 21) ? DetentionTypeType.IMPRISONMENT : DetentionTypeType.DETENTION);
        } else {
            log.debug("Cannot calculate age, defendant DOB  is null");
        }
    }

}
