package uk.gov.courtservice.xhibit.business.services.results;

import uk.gov.courtservice.xhibit.business.services.results.populater.CaseAppReasonPopulater;
import uk.gov.courtservice.xhibit.business.services.results.populater.PleasPopulater;
import uk.gov.courtservice.xhibit.business.services.results.populater.RecordSheetDisposalsPopulater;
import uk.gov.courtservice.xhibit.business.services.results.populater.RecordSheetDisposalsReferencePopulater;
import uk.gov.courtservice.xhibit.business.services.results.populater.VerdictsPopulater;

/**
 * <p>
 * Title: Factory containing result populators
 * </p>
 * <p>
 * Description: Contains method called to populate ResultsCompositeValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: ResultsPopulaterFactory.java,v 1.6 2005/01/25 07:56:18 tz0d5m
 *          Exp $
 */
public class ResultsPopulaterFactory {
    private static final ResultsPopulaterFactory instance = new ResultsPopulaterFactory();

    public static ResultsPopulaterFactory getInstance() {
        return instance;
    }

    private final ResultsPopulater[] resultsPopulaters = new ResultsPopulater[] { new CaseAppReasonPopulater(),
            new PleasPopulater(), new VerdictsPopulater(), new RecordSheetDisposalsPopulater() };

    private final ReferencePopulater[] referencePopulaters = new ReferencePopulater[] { new RecordSheetDisposalsReferencePopulater() };

    private ResultsPopulaterFactory() {
    }

    public ResultsPopulater[] getResultsPopulaters() {
        return resultsPopulaters;
    }

    public ReferencePopulater[] getReferencePopulaters() {
        return referencePopulaters;
    }
}