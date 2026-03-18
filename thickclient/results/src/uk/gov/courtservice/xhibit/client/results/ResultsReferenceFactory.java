package uk.gov.courtservice.xhibit.client.results;

// 3rd Party
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalMenuReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsValueException;

/**
 * <p>
 * Title: ResultsReferenceFactory
 * </p>
 * <p>
 * Description: Singleton for caching and accessing reference data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public class ResultsReferenceFactory {
    /**
     * The singleton instance
     */
    private static ResultsReferenceFactory instance = null;

    /**
     * The singleton accessor
     */
    public static synchronized ResultsReferenceFactory getInstance() {
        if (instance == null) {
            instance = new ResultsReferenceFactory();
        }
        return instance;
    }

    /**
     * The singleton release
     */
    public static synchronized void freeInstance() {
        instance = null;
    }

    /**
     * The cached reference data, do not access directly use
     * getRecordSheetDisposalReferenceValue to ensure the operation is thread
     * safe
     */
    private ResultsReferenceValue recordSheetDisposalReferenceValue = null;

    /**
     * Stop external construction of this object
     */
    private ResultsReferenceFactory() {
        // Change permisions of default constructor
    }

    /**
     * Get the root of the drecord sheet disposal menu data
     * 
     * @return the root of the result disposal menu
     * @throws ResultsValueException
     *             if the reference data does not have the disposal menu
     */
    public DisposalMenuReferenceValue getRecordSheetDisposalMenuRoot() throws ResultsControllerException {
        return getRecordSheetDisposalReference().getRecordSheetDisposalMenuRoot();
    }

    /**
     * Get a disposal from record sheet map
     * 
     * @param refDisposalTypeId
     *            the pk of the disposal to lookup
     * @return the DisposalReferenceValue or null if not found
     * @throws IllegalArgumentException
     *             if the key is null
     */
    public DisposalReferenceValue getRecordSheetDisposal(int refDisposalTypeId) throws ResultsControllerException {
        return getRecordSheetDisposalReference().getRecordSheetDisposal(refDisposalTypeId);
    }

    public DisposalReferenceValue getRecordSheetDisposal(Integer refDisposalTypeId) throws ResultsControllerException {
        return getRecordSheetDisposalReference().getRecordSheetDisposal(refDisposalTypeId);
    }

    /**
     * Get the latest disposal from the record sheet map
     * 
     * @param disposalCode
     *            code of disposal to lookup
     * @param templateVersion
     *            of disposal to lookup
     * 
     * @return the DisposalReferenceValue or null if not found
     * @throws IllegalArgumentException
     *             if the key is null
     */
    public DisposalReferenceValue getLatestRecordSheetDisposal(String disposalCode) throws ResultsControllerException {
        return getRecordSheetDisposalReference().getLatestRecordSheetDisposal(disposalCode);
    }

    /**
     * Return a string containing debug infromation (note this can be very
     * verbose)
     * 
     * @return the debug string
     */
    public String toDebug() {
        try {
            return getRecordSheetDisposalReference().toDebug();
        } catch (ResultsControllerException rce) {
            return rce.toString();
        }
    }

    private synchronized ResultsReferenceValue getRecordSheetDisposalReference() throws ResultsControllerException {
        if (recordSheetDisposalReferenceValue == null) {
            recordSheetDisposalReferenceValue = XhibitDelegateHelper.getResults2Delegate().getReference(
                    XhibitSingleton.getInstance().getCourtId());
        }
        return recordSheetDisposalReferenceValue;
    }

}