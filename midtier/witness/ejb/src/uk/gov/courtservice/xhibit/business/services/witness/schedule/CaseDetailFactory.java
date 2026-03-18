package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;

/**
 * <p>
 * Title: A factory to obtain CaseDetail instances.
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * 
 * <b>This is not thread safe, under the covers it uses unsynchronized lazy
 * instantiation. You have been warned :-) </b>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.10 $
 */
public class CaseDetailFactory {
    private static final CaseDetailFactory ourInstance = new CaseDetailFactory();

    private transient WitnessControllerBeanBusinessDelegate delegate;

    private CaseDetailFactory() {
    }

    /**
     * @return an instance of the CaseDetailFactory
     */
    public static CaseDetailFactory getInstance() {
        return ourInstance;
    }

    protected WitnessControllerBeanBusinessDelegate getDelegate() {
        if (delegate == null) {
            delegate = WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        }
        return delegate;

    }

    /**
     * Returns a specific case detail by primary key.
     * 
     * @param caseId
     * @return
     * @throws CaseNotFoundException
     *             if the primary key is not valid.
     */
    public CaseDetail getCaseDetail(final Integer caseId) throws CaseNotFoundException {
        return getDelegate().getCaseDetails(caseId);
    }

    /**
     * Searches for cases by the caseRef that is the case number and case type
     * combined. eg. T2002002
     * 
     * @param caseRef
     * @return
     * @throws CaseNotFoundException
     *             if the caseRef does not match an existing case.
     */
    public CaseDetail getCaseDetailsByCaseRef(final String caseRef) throws CaseNotFoundException {
        return getDelegate().getCaseDetailsByCaseRef(caseRef);
    }

    /**
     * Searches for cases by the caseRef that is the case number and case type
     * combined. eg. T2002002
     * 
     * @param caseRef
     * @return
     * @throws CaseNotFoundException
     *             if the caseRef does not match an existing case.
     */
    public CaseDetail getCaseDetailsByCaseRefAndCourtId(final String caseRef, final Integer courtId)
            throws CaseNotFoundException {
        return getDelegate().getCaseDetailsByCaseRefAndCourtId(caseRef, courtId);
    }
}
