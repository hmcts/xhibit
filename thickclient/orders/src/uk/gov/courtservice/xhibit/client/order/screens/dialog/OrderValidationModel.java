package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationProblem;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application - OrderValidationModel. Holds
 * references to the OrderValidationProblem array representing errors and
 * warnings
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

public class OrderValidationModel {
    private static final Logger log = CSServices.getLogger(OrderValidationModel.class);

    private OrderValidationProblem[] errors;

    private OrderValidationProblem[] warnings;

    /**
     * Constructor
     * 
     * @param errors
     *            array of errors
     * @param warnings
     *            array of warnings
     */
    public OrderValidationModel(OrderValidationProblem[] errors, OrderValidationProblem[] warnings) {
        this.errors = errors;
        this.warnings = warnings;
    }

    /**
     * Return an array of errors
     * 
     * @return the errors
     */
    public OrderValidationProblem[] getErrors() {
        return errors;
    }

    /**
     * Sets the errors
     * 
     * @param errors
     *            the errors
     */
    public void setErrors(OrderValidationProblem[] errors) {
        this.errors = errors;
    }

    /**
     * Return an array of warnings
     * 
     * @return the warnings
     */
    public OrderValidationProblem[] getWarnings() {
        return warnings;
    }

    /**
     * Set the warnings
     * 
     * @param warnings
     */
    public void setWarnings(OrderValidationProblem[] warnings) {
        this.warnings = warnings;
    }

    // Utility methods
    /**
     * Print out the model for debug purposes
     */
    public void printModel() {
        XHIBITConstant.info("OrderValidationModel");
        XHIBITConstant.info("-------------------");
        XHIBITConstant.info("Error Count              : " + getErrors().length);
        XHIBITConstant.info("--------------------------------");
        XHIBITConstant.info("Error list               : ");
        printProblems(getErrors());
        XHIBITConstant.info("--------------------------------");
        XHIBITConstant.info("Warning Count            : " + getWarnings().length);
        XHIBITConstant.info("--------------------------------");
        XHIBITConstant.info("Warning list               : ");
        printProblems(getWarnings());
        XHIBITConstant.info("--------------------------------");
    }

    /**
     * Print out the problems
     * 
     * @param problems
     *            errors or warnings
     */
    private void printProblems(OrderValidationProblem[] problems) {
        for (int i = 0; i < problems.length; i++) {
            log.debug(" Code :   " + problems[i].getMessageCode() + "  Message: " + problems[i].getMessageFields());
        }
    }

    /**
     * Reset the model
     */
    public void clearmodel() {
        setErrors(null);
        setWarnings(null);
    }
}