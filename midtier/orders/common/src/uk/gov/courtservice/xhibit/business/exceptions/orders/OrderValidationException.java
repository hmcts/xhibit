package uk.gov.courtservice.xhibit.business.exceptions.orders;

/**
 * <p>
 * Title: Exception designed to encapsulate problems in order validation
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class returns all the errors/warnings associated with a validation of
 * the warning. If there were only warnings, then this class will also contain
 * the successfully saved XhbOrderValue primary key.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class OrderValidationException extends OrderException {
	
	static final long serialVersionUID = -8498486406401200129L;
	
    private OrderValidationProblem[] errors;

    private OrderValidationProblem[] warnings;

    private Integer savedWithWarningsID;

    public OrderValidationException(OrderValidationProblem[] errors, OrderValidationProblem[] warnings) {
        super();
        this.errors = errors;
        this.warnings = warnings;
    }

    public OrderValidationException(OrderValidationProblem[] errors, OrderValidationProblem[] warnings, String s,
            String s1, Throwable throwable) {
        super(s, s1, throwable);
        this.errors = errors;
        this.warnings = warnings;
    }

    public OrderValidationException(OrderValidationProblem[] errors, OrderValidationProblem[] warnings, String s,
            Object[] objects, String s1, Throwable throwable) {
        this.errors = errors;
        this.warnings = warnings;
    }

    public OrderValidationException(OrderValidationProblem[] errors, OrderValidationProblem[] warnings, String s,
            String s1) {
        this.errors = errors;
        this.warnings = warnings;
    }

    public OrderValidationProblem[] getErrors() {
        return errors;
    }

    public OrderValidationProblem[] getWarnings() {
        return warnings;
    }

    public boolean isFatal() {
        return errors != null && errors.length > 0;
    }

    public Integer getSavedWithWarningsID() {
        return savedWithWarningsID;
    }

    public void setSavedWithWarningsID(Integer savedWithWarningsID) {
        this.savedWithWarningsID = savedWithWarningsID;
    }

}