package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.address.UKPostalAddressStructure;

/**
 * <p>
 * Title: Utility class for populating addresses in an order schema.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class populates castor bound xml objects from entity beans.
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
public class AddressHelper {
    /**
     * Method populating an castor bound xml schema from an entity bean. Must
     * always use the isValidAddress method to validate the addressEntity before
     * using this method.
     * 
     * @param address
     *            The castor bound object to populate.
     * @param addressEntity
     *            The address entity bean to use for population.
     * @throws OrderXMLException
     *             when the addressEntity passed in is not valid for populating
     *             the XML address.
     */

    // set up logger
    private static final Logger log = CSServices.getLogger(AddressHelper.class);

    // Surrounding the postcode regualr expressions with ^$ means that
    // the string must match one of the options. If we omit these
    // metacharacters
    // a best match may be achieved so allowing through some invalid
    // postcodes
    // NB: These metacharacters are NOT included in the BS7666-v1.xsd so
    // care
    // should be taken if the pattern changes
    // See also
    // uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers.PostCodeVerifier
    private static final String REG_EXP_POSTCODE = "(GIR 0AA)|^((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|"
            + "(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$";

    public static void populateAddress(UKPostalAddressStructure address, Address addressEntity)
            throws RESyntaxException {
        int positionToAdd = 0;
        boolean address3Added = false;
        boolean address4Added = false;

        // S.Bachra 09.05.03 If the address line1 or address line2 is blank set
        // to " "
        if (addressEntity.getAddress1() == null || addressEntity.getAddress1().equals("")) {
            log.debug("Empty AddressLine1");
            address.addLine(positionToAdd++, "  ");
        } else {
            log.debug("AddressLine1 Added");
            address.addLine(positionToAdd++, addressEntity.getAddress1());
        }

        if (addressEntity.getAddress2() == null || addressEntity.getAddress2().equals("")) {
            // Add blank address line but don't increment positionToAdd.
            // If further lines are found these will overwrite the blank.
            log.debug("Empty AddressLine2");
            address.addLine(positionToAdd, "  ");
        } else {
            log.debug("AddressLine2 Added");
            address.addLine(positionToAdd++, addressEntity.getAddress2());
        }

        // check no of remaining lines
        if (addressEntity.getAddress3() != null && !addressEntity.getAddress3().equals("")) {
            log.debug("AddressLine3 Exists & Added");
            address.addLine(positionToAdd++, addressEntity.getAddress3());
            address3Added = true;
        }

        if (addressEntity.getAddress4() != null && !addressEntity.getAddress4().equals("")) {
            log.debug("AddressLine4 Exists & Added");
            address.addLine(positionToAdd++, addressEntity.getAddress4());
            address4Added = true;
        }

        // check status of town and county
        boolean townExists = false;
        boolean countyExists = false;

        if (addressEntity.getTown() != null && !addressEntity.getTown().equals("")) {
            log.debug("Town Exists");
            townExists = true;
        }

        if (addressEntity.getCounty() != null && !addressEntity.getCounty().equals("")) {
            log.debug("County Exists");
            countyExists = true;
        }

        if (address3Added && address4Added) {

            log.debug("Full Address: 3+4 Added");
            // need to concat the town and county if they exist (check
            // values exist first)
            if (townExists && countyExists) {
                log.debug("Town & County Exist CONCAT");
                StringBuffer addressConcat = new StringBuffer();
                addressConcat.append(addressEntity.getTown());
                addressConcat.append(", ");
                addressConcat.append(addressEntity.getCounty());
                String townCounty = null;
                if (addressConcat.toString().length() > 34) {
                    // max size set to 35 characters
                    townCounty = addressConcat.toString().substring(0, 34);
                } else {
                    townCounty = addressConcat.toString();
                }
                log.debug("Concat String : " + townCounty);
                address.addLine(positionToAdd++, townCounty);
            } else {
                if (townExists) {
                    log.debug("Only Town Exists");
                    address.addLine(positionToAdd++, addressEntity.getTown());
                }
                if (countyExists) {
                    log.debug("Only County Exists");
                    address.addLine(positionToAdd++, addressEntity.getCounty());
                }
            }
        } else {
            log.debug("Enough Space to add Town + County");
            // have enough space to add town and county if they exist
            if (townExists)
                address.addLine(positionToAdd++, addressEntity.getTown());

            if (countyExists)
                address.addLine(positionToAdd++, addressEntity.getCounty());
        }

        if (addressEntity.getPostcode() != null && !addressEntity.getPostcode().equals("")) {
            log.debug("POSTCODE |" + addressEntity.getPostcode() + "|");
            // check validity of postcode against regular expression
            RE regexp = new RE(REG_EXP_POSTCODE);

            boolean matched = false;
            matched = regexp.match(addressEntity.getPostcode());

            log.debug("<<<<<<>>>>>>>> matched |" + matched + "|");
            if (matched) {
                address.setPostCode(addressEntity.getPostcode());
            }
            log.debug("<<<<<<>>>>>>>> address.getPostCode() |" + address.getPostCode() + "|");
        }
    }

    /**
     * Utility method to say whether an address entity is valid for population
     * of the XML.
     * 
     * @param addressEntity
     *            The entity to validate
     * @return Whether XML can be populated from this XML.
     */
    public static boolean isValidAddress(Address addressEntity) {
        return addressEntity.getAddress1() != null && !addressEntity.getAddress1().equals("")
                && addressEntity.getAddress2() != null && !addressEntity.getAddress2().equals("");
    }
}