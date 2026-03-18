package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.DataDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.DbDestinDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.DefaultDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.DisposalCodeDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.DisposalCodeTemplateVersionDilSeqNoDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.DisposalCodeTemplateVersionDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.FooterDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.InputDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.MultipleDataDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.MultiplePromptDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.OptionalDateOfResultDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.PromptDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.SeperatorDisposalLineCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria.ValidationDisposalLineCriteria;

/**
 * <p>
 * Title: DisposalLineCriteriaFactory
 * </p>
 * <p>
 * Description: DisposalLineCriteriaFactory used to create disposal line
 * criteria.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 */
public class DisposalLineCriteriaFactory {
    /**
     * Stop static factory class from being created
     */
    private DisposalLineCriteriaFactory() {
        // Change permisions of default constructor
    }

    /**
     * Create a DisposalLineCriteria to match all lines
     * 
     * @param DisposalCode
     *            the code to match
     * @return the new DisposalLineCriteria object
     */
    public static DisposalLineCriteria create() {
        return new DefaultDisposalLineCriteria();
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param DisposalCode
     *            the code to match
     * @return the new DisposalLineCriteria object
     */
    public static DisposalLineCriteria create(String code) {
        return new DisposalCodeDisposalLineCriteria(code);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param disposalCode
     *            search criteria to match
     * @param templateVersion
     *            search criteria to match
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    public static DisposalLineCriteria create(String disposalCode, int templateVersion) {
        return new DisposalCodeTemplateVersionDisposalLineCriteria(disposalCode, templateVersion);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param disposalCode
     *            search criteria to match
     * @param templateVersion
     *            search criteria to match
     * @param dilSeqNo
     *            search criteria to match
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    public static DisposalLineCriteria create(String disposalCode, int templateVersion, int dilSeqNo) {
        return new DisposalCodeTemplateVersionDilSeqNoDisposalLineCriteria(disposalCode, templateVersion, dilSeqNo);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param prompt
     *            search criteria to match
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    public static DisposalLineCriteria createPrompt(String prompt) {
        return new PromptDisposalLineCriteria(prompt);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param prompt
     *            search criteria to match
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    public static DisposalLineCriteria createPrompt(String[] prompt) {
        return new MultiplePromptDisposalLineCriteria(prompt);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param data
     *            search criteria to match
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    public static DisposalLineCriteria createData(String data) {
        return new DataDisposalLineCriteria(data);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param data
     *            search criteria to match
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    public static DisposalLineCriteria createData(String[] data) {
        return new MultipleDataDisposalLineCriteria(data);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param validation
     *            search criteria to match
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    public static DisposalLineCriteria createValidation(String validation) {
        return new ValidationDisposalLineCriteria(validation);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param dbdestin
     *            search criteria to match
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    public static DisposalLineCriteria createDbDestin(String dbdestin) {
        return new DbDestinDisposalLineCriteria(dbdestin);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @param input
     *            search criteria to match
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    public static DisposalLineCriteria createInput(boolean input) {
        return new InputDisposalLineCriteria(input);
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    private static final DisposalLineCriteria footer = new FooterDisposalLineCriteria();

    public static DisposalLineCriteria createFooter() {
        return footer;
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    private static final DisposalLineCriteria seperator = new SeperatorDisposalLineCriteria();

    public static DisposalLineCriteria createSeperator() {
        return seperator;
    }

    /**
     * Create a DisposalLineCriteria to match the specified criteria
     * 
     * @return the new DisposalLineCriteria object to match the specified
     *         criteria
     */
    private static final DisposalLineCriteria optionalDateOfResult = new OptionalDateOfResultDisposalLineCriteria();

    public static DisposalLineCriteria createOptionalDateOfResult() {
        return optionalDateOfResult;
    }
}
