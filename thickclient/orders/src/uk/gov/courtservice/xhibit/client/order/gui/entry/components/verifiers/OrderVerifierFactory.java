package uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers;

import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;

/**
 * <p>
 * Title: OrderVerifierFactory
 * </p>
 * <p>
 * Description: Returns the correct verifier according to the type of the XML
 * element.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderVerifierFactory {
    /**
     * Returns the InputVerifier specified by the type parameter. If type does
     * nor exist, an OrderComponentException is thrown.
     * 
     * @param type
     *            type of component
     * @throws OrderComponentException
     */
    public static OrderInputVerifier createVerifier(String type) throws OrderComponentException {
        if (type.equals(OrderInputVerifier.INT_TYPE)) {
            return new IntVerifier();
        } else if (type.equals(OrderInputVerifier.STRING_TYPE)) {
            return new StringVerifier();
        } else if (type.equals(OrderInputVerifier.ALPHANUMERIC_TYPE)) {
            return new AlphanumericVerifier();
        } else if (type.equals(OrderInputVerifier.NAME_TYPE)) {
            return new NameVerifier();
        } else if (type.equals(OrderInputVerifier.ADDRESS_TYPE)) {
            return new AddressStringVerifier();
        } else if (type.equals(OrderInputVerifier.POSTCODE_TYPE)) {
            return new PostCodeVerifier();
        } else if (type.equals(OrderInputVerifier.COMBO_TYPE)) {
            return new ComboBoxVerifier();
        } else {
            throw new OrderComponentException("No such component " + type + ".");
        }
    }
}
