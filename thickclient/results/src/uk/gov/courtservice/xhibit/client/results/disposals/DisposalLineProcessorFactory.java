package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor.DataDisposalLineProcessor;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor.InputFlagDisposalLineProcessor;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor.LineInsertDisposalLineProcessor;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor.MandatoryDisposalLineProcessor;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor.PromptDisposalLineProcessor;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor.ScreenPrintDisposalLineProcessor;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor.ValidationDisposalLineProcessor;

/**
 * <p>
 * Title: DisposalLineProcessorFactory
 * </p>
 * <p>
 * Description: DisposalLineProcessorFactory used to create disposal line
 * processors.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Validation Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.12 $
 */
public class DisposalLineProcessorFactory {

    /**
     * Stop creation of this static factory
     */
    private DisposalLineProcessorFactory() {
        // Change permisions of default constructor
    }

    /**
     * Create a DisposalLineProcessor to set the prompt to the specified value
     * 
     * @param prompt
     *            the new value
     * @return the new DisposalCriteria object
     */
    public static DisposalLineProcessor createPrompt(String prompt) {
        return new PromptDisposalLineProcessor(prompt);
    }

    /**
     * Create a DisposalLineProcessor to set the data to the specified value
     * 
     * @param data
     *            the new value
     * @return the new DisposalCriteria object
     */
    public static DisposalLineProcessor createData(String data) {
        return new DataDisposalLineProcessor(data);
    }

    /**
     * Create a DisposalLineProcessor to set the data to the specified value
     * 
     * @param mandatory
     *            the new value
     * @return the new DisposalCriteria object
     */
    private static final DisposalLineProcessor TRUE_MANDATORY = new MandatoryDisposalLineProcessor(true);

    private static final DisposalLineProcessor FALSE_MANDATORY = new MandatoryDisposalLineProcessor(false);

    public static DisposalLineProcessor createMandatory(boolean mandatory) {
        return mandatory ? TRUE_MANDATORY : FALSE_MANDATORY;
    }

    /**
     * Create a DisposalLineProcessor to set the screen print flag to the
     * specified value
     * 
     * @param DisposalCode
     *            the code to match
     * @return the new DisposalCriteria object
     */
    private static final DisposalLineProcessor TRUE_SCREEN_PRINT = new ScreenPrintDisposalLineProcessor(true);

    private static final DisposalLineProcessor FALSE_SCREEN_PRINT = new ScreenPrintDisposalLineProcessor(false);

    public static DisposalLineProcessor createScreenPrint(boolean screenPrint) {
        return screenPrint ? TRUE_SCREEN_PRINT : FALSE_SCREEN_PRINT;
    }

    /**
     * Create a DisposalLineProcessor to set the line insert flag to the
     * specified value
     * 
     * @param lineInsert
     *            the flag to match
     * @return the new DisposalCriteria object
     */
    private static final DisposalLineProcessor TRUE_LINE_INSERT = new LineInsertDisposalLineProcessor(true);

    private static final DisposalLineProcessor FALSE_LINE_INSERT = new LineInsertDisposalLineProcessor(false);

    public static DisposalLineProcessor createLineInsert(boolean lineInsert) {
        return lineInsert ? TRUE_LINE_INSERT : FALSE_LINE_INSERT;
    }

    /**
     * Create a DisposalLineProcessor to set the input flag to the specified
     * value
     * 
     * @param inputFlag
     *            the flag to match
     * @return the new DisposalCriteria object
     */
    private static final DisposalLineProcessor TRUE_INPUT_FLAG = new InputFlagDisposalLineProcessor(true);

    private static final DisposalLineProcessor FALSE_INPUT_FLAG = new InputFlagDisposalLineProcessor(false);

    public static DisposalLineProcessor createInputFlag(boolean inputFlag) {
        return inputFlag ? TRUE_INPUT_FLAG : FALSE_INPUT_FLAG;
    }

    /**
     * Create a DisposalLineProcessor to set the validation to the specified
     * value
     * 
     * @param validation
     *            the new value
     * @return the new DisposalCriteria object
     */
    public static DisposalLineProcessor createValidation(String validation) {
        return new ValidationDisposalLineProcessor(validation);
    }

}
