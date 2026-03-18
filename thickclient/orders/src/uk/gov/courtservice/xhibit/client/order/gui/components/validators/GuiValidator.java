package uk.gov.courtservice.xhibit.client.order.gui.components.validators;

import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;

/**
 * <p>
 * Title: Common interface to all validator classes.
 * </p>
 * <p>
 * Description: Common interface to all validator classes.
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
public interface GuiValidator {

    /**
     * Validates field and returns true if valid.
     * 
     * @param value
     *            String contents of field to be validated.
     * @return true if valid.
     */
    public boolean isValid(String value);

    /**
     * Sets the minimum value allowed for the numeric field to be validated.
     * 
     * @param minVal
     *            String representing the minimum value constraint.
     */
    public void setMinVal(String minVal);

    /**
     * Set required to true or false.
     * 
     * @param req
     *            String value which is parsed to the correct boolean value.
     */
    public void setRequired(boolean req);

    public void setIsParentAnOrderOption(boolean b);

    public boolean isParentAnOrderOption();

    public void setOpt(OrderOption opt);

    public OrderOption getOpt();

    public void setValidationOn(boolean validationOn);
}
