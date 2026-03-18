package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.regexp.RESyntaxException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourt;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourtHome;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CourtHouseAddress;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.XHIBITCourtHouseStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.XHIBITCourtType;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * CourtHouse
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Populates the CourtHouse.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class CourtHouseHelper {
    private static final Logger log = CSServices.getLogger(CourtHouseHelper.class);

    private static final String CROWN_CRESTCODE_MIN = "orders.crown.crestcode.min";

    private static final String CROWN_CRESTCODE_MAX = "orders.crown.crestcode.max";

    private static final String MAGISTRATES_CRESTCODE_MIN = "orders.magistrates.crestcode.min";

    private static final String MAGISTRATES_CRESTCODE_MAX = "orders.magistrates.crestcode.max";

    private static final String YOUTH_CRESTCODE_MIN = "orders.youth.crestcode.min";

    private static final String YOUTH_CRESTCODE_MAX = "orders.youth.crestcode.max";

    private static final String ORDERS_BUNDLE = "XHIBITOrdersClient_en_GB";

    private static final RefCourtHome refCourtHome;
    static {
        refCourtHome = (RefCourtHome) CSServices.getServiceLocator().getLocalHome(RefCourtHome.class);
    }

    /**
     * Utility method to populate a CourtHouse Castor object from the EB.
     * 
     * @param courtHouse
     *            The CourtHouse to be populated (pass by reference).
     * @param courtEntity
     *            The court entity bean to use for population.
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    public static void populateCourtHouse(XHIBITCourtHouseStructure courtHouse, Court courtEntity) throws OrderXMLException {
        // Set up court house type.
        courtHouse.setCourtHouseType(XHIBITCourtType.CROWN_COURT);// Will always be a crown court.

        // Set Court House Code.
        courtHouse.getCourtHouseCode().setContent(courtEntity.getCrestCourtId());
        courtHouse.getCourtHouseCode().setCourtHouseShortName(courtEntity.getShortName());

        // Set the court house name.
        courtHouse.setCourtHouseName(courtEntity.getCourtName());

        // Set address details.
        Address addressEntity = courtEntity.getAddress();

        courtHouse.setCourtHouseAddress(populateAddress(courtHouse.getCourtHouseAddress(), addressEntity));
    }

    /**
     * Utility method to populate a CourtHouse Castor object from the EB.
     * 
     * @param courtHouse
     *            The CourtHouse to be populated (pass by reference).
     * @param refCourtEntity
     *            The refCourt entity bean to use for population.
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    public static void populateCourtHouse(XHIBITCourtHouseStructure courtHouse, RefCourt refCourtEntity)
            throws OrderXMLException {
        int crown_min = 0;
        int crown_max = 0;
        int mag_min = 0;
        int mag_max = 0;
        int youth_min = 0;
        int youth_max = 0;
        ResourceBundle bundle = CSServices.getConfigServices().getBundle(ORDERS_BUNDLE);

        crown_min = setCourtCodeRange(bundle, CROWN_CRESTCODE_MIN);
        crown_max = setCourtCodeRange(bundle, CROWN_CRESTCODE_MAX);
        mag_min = setCourtCodeRange(bundle, MAGISTRATES_CRESTCODE_MIN);
        mag_max = setCourtCodeRange(bundle, MAGISTRATES_CRESTCODE_MAX);
        youth_min = setCourtCodeRange(bundle, YOUTH_CRESTCODE_MIN);
        youth_max = setCourtCodeRange(bundle, YOUTH_CRESTCODE_MAX);

        // Set up court house type.
        // From Valid courts split via the Crest Code (Tracker 53235)
        // Split the court types by range of CREST_CODE (assumption)
        // CROWN COURTS 1-999
        // MAGISTRATES COURTS 1000-4999
        // YOUTH COURTS 5000-9999
        if (refCourtEntity.getIsPsd() != null && refCourtEntity.getIsPsd().equalsIgnoreCase("Y") // Is
                // petty
                // sessional
                // division.
                && refCourtEntity.getCrestCode() != null) {
            int crestNo = Integer.parseInt(refCourtEntity.getCrestCode());

            // youth courts
            if (crestNo > youth_min && crestNo < youth_max) {
                courtHouse.setCourtHouseType(XHIBITCourtType.YOUTH_COURT);// youth
                // court.
            } else
                courtHouse.setCourtHouseType(XHIBITCourtType.MAGISTRATES_COURT); // magistrates
            // court.
        } else {
            courtHouse.setCourtHouseType(XHIBITCourtType.CROWN_COURT); // crown
            // court.
        }

        // Set Court House Code.
        courtHouse.getCourtHouseCode().setContent(refCourtEntity.getCrestCode());
        courtHouse.getCourtHouseCode().setCourtHouseShortName(refCourtEntity.getCourtShortName());

        // Set the court house name.
        courtHouse.setCourtHouseName(refCourtEntity.getCourtFullName() + " (" + refCourtEntity.getCrestCode() + ")");

        // Set address details.
        Address addressEntity = null;// refCourtEntity.getAddress();

        courtHouse.setCourtHouseAddress(populateAddress(courtHouse.getCourtHouseAddress(), addressEntity));
    }

    /**
     * Retrieves the required key from the resource bundle and converts it to an
     * int
     * 
     * @param bundle
     *            the bundle
     * @param key
     *            the key to retrieve
     * @return the value as an int
     * @throws NumberFormatException
     * @throws MissingResourceException
     */
    private static int setCourtCodeRange(ResourceBundle bundle, String key) throws NumberFormatException,
            MissingResourceException {
        // pick up values from ResourceBundle and convert to int
        return Integer.parseInt(bundle.getString(key));
    }

    /**
     * Utility method to create/populate a CourtHouseAddress using the
     * AddressHelper.
     * 
     * @param address
     *            The court House Address to populate (might be null).
     * @param addressEntity
     *            The entity bean to populate from.
     * @return the created/populated CourtHouseAddress.
     * @throws OrderXMLException
     *             When there is an issue in population.
     */
    private static CourtHouseAddress populateAddress(CourtHouseAddress address, Address addressEntity)
            throws OrderXMLException {
        // If the address is valid, bother if not, don't
        if (addressEntity != null && AddressHelper.isValidAddress(addressEntity)) {
            if (address == null)
                address = new CourtHouseAddress(); // note COURT HOUSE
            // ADDRESS...
            try {
                AddressHelper.populateAddress(address, addressEntity);

            } catch (RESyntaxException e) {
                e.printStackTrace(); // To change body of catch statement use
                // Options | File Templates.
                log.error(e);
                throw new OrderXMLException("order.validation.postcode.invalid",
                        "Error within regular expression for postcode", e);
            }
        }
        return address;
    }
}