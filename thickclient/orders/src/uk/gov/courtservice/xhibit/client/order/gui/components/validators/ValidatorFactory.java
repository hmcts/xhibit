package uk.gov.courtservice.xhibit.client.order.gui.components.validators;

/**
 * <p>
 * Title: Factory class to return a GuiValidator.
 * </p>
 * <p>
 * Description: Factory class to return a GuiValidator, providing validation for
 * all gui components.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public class ValidatorFactory {
    /**
     * Returns a GuiValidator specified by the type parameter.
     * 
     * @param type
     *            Type of validator.
     * @return GuiValidator.
     */
    public static GuiValidator getValidator(String type) {
        if (type == null) {
            // no validation requested...
            return null;
        } else if (type.equals("int")) {
            return new IntValidator();
        } else if (type.equals("float")) {
            return new FloatValidator();
        } else if (type.equals("required")) {
            return new StringValidator();
        } else {
            return new StringValidator();
        }

    }

    /**
     * Return the OrdersExceptionDialog to display critical gui faluires.
     * 
     * @return OrdersExceptionDialog.
     */
    public static OrdersExceptionDialog getOrdersDialog() {
        return new OrdersExceptionDialog();
    }

}
