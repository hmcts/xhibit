package uk.gov.courtservice.xhibit.client.results.appealresults;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;

/**
 * <p>
 * Title: Class to hold reference data
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Rakesh Lakhani
 * @version $Id: AppealResultsModel.java,v 1.5 2007/07/11 09:08:09 rzvddy Exp $
 */

public class AppealResultsModel {
    private Collection referenceData = null;

    private Collection referenceAppealOffenceData = null;

    private Collection referenceAppealGenMagsData = null;

    private ResultsHelper resultsHelper;

    private ApplicationCaseModel acm;

    public AppealResultsModel(ApplicationCaseModel acm) throws ResultsControllerException {
        this.acm = acm;
        reloadResults();
    }

    public void reloadResults() throws ResultsControllerException {
        resultsHelper = new ResultsHelper(acm);
    }

    /**
     * set the ref system code data for use in other methods
     *
     * @param refData
     */
    public void setRefData(Collection refData) {
        referenceData = refData;
    }

    /**
     * get the appeal offence ref system code data for use in other methods
     *
     * @param refData
     */
    public Collection getAppealOffenceRefData() {
        return referenceAppealOffenceData;
    }

    /**
     * set the appeal offence ref system code data for use in other methods
     *
     * @param refData
     */
    public void setAppealOffenceRefData(Collection refData) {
        referenceAppealOffenceData = refData;
    }

    /**
     * get the appeal result ref data for general magistrates courts disposals.
     *
     * @return Collection
     */
    public Collection getAppealGeneralMagsRefData() {
        return referenceAppealGenMagsData;
    }

    /**
     * set the appeal result ref data for general magistrates courts disposals.
     *
     * @param refData
     */
    public void setAppealGeneralMagsRefData(Collection refData) {
        referenceAppealGenMagsData = refData;
    }

    /**
     * get the ref system code data for use in other methods
     *
     * @param refData
     */
    public Collection getRefData() {
        return referenceData;
    }

    public ResultsHelper getResultsHelper() {
        return resultsHelper;
    }

    public ResultsCompositeValue getResultsCompositeValue() {
        return resultsHelper.getResultsCompositeValue();
    }

    public XhbCaseBasicValue getCaseBasicValue() {
        return getResultsCompositeValue().getChargeCompositeValue().getCaseBasicValue();
    }
}