package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.ResultsDatabase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.services.results.Results2WorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Variation;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Variations;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.types.YesNoType;

/**
 * <p/> Title: Utility class for populating Unrelated Information in a Crest
 * Form Schema.
 * </p>
 * <p/> Description: Charge information population
 * </p>
 * <p/> This class populates castor bound xml objects from entity beans.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Surtar Bachra
 * @version 1.0
 */
public class CrestFormBFUnrelatedDisposalHelper2 {
    protected static Logger log = CSServices.getLogger(CrestFormBFUnrelatedDisposalHelper2.class);

    private static final String MAGISTRATES_COURT = "M";

    /**
     * Util method to set up unrelated disposal information for the given docID
     *
     * @param urd
     *            Castor bound UnrelatedDisposals structure to populate
     * @param doc
     *            XhbDefendantOnCase
     * @return populated castor object
     */
    public static uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.UnrelatedDisposals populateUnrelatedDisposals(
            uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.UnrelatedDisposals urd, XhbDefendantOnCase doc) {

        log.debug("Unrelated Disposals Helper");
        Integer docID = doc.getDefendantOnCaseId();
        Integer caseId = doc.getCaseId();
        log.debug(">>>>>>> DOC ID :: " + docID);
        log.debug(">>>>>>> CASE ID :: " + caseId);

        try {
            ResultsCompositeValue rcv = Results2WorkFlow.getResults(caseId);
            int noOfUnrelatedDisposals = rcv.getUnrelatedDisposalCount(docID);
            log.debug("No of Unrelated Disposals :: " + noOfUnrelatedDisposals);

            if (noOfUnrelatedDisposals > 0 && urd == null) {
                // add a new structure if disposals exist and there is no urd
                // structure
                urd = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.UnrelatedDisposals();
            }

            for (int i = 0; i < noOfUnrelatedDisposals; i++) {
                uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Disposal disposalToAdd = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Disposal();
                DisposalValue dv = rcv.getUnrelatedDisposal(docID, i);

                int dispTypeId = dv.getRefDisposalTypeId();
                DisposalReferenceValue drv = ResultsDatabase.getReferenceDisposal(new Integer(dispTypeId));
                String disposalCode = drv.getDisposalCode();
                String disposalDesc = drv.getTitle();
                String formBFText = drv.getFormBFText(dv);
                disposalToAdd.setDisposalCode(disposalCode);
                disposalToAdd.setDisposalDesc(disposalDesc);
                disposalToAdd.setFormBFText(formBFText);
                disposalToAdd.setIsMagistrateCourt(YesNoType.NO); // set
                // up
                // default
                // value
                // test if magistrates court disposal
                if (dv.getCourtType().equals(MAGISTRATES_COURT)) {
                    disposalToAdd.setIsMagistrateCourt(YesNoType.YES);
                    // if it is a Magistrates Disposal find the verdict
                    // based on the Disposal2Id
                    // This may throw an exception
                    log.debug("<<<>>> Disposal2Id = " + dv.getDisposal2Id());
                    XhbVerdict verdict = null;
                    try {
                        verdict = XhbVerdictBeanHelper2.findByDisposal2Id(dv.getDisposal2Id());
                        log.debug("<<<>>> VERDICT CODE: " + verdict.getXhbRefAppResult().getAppResultCode());
                        log.debug("<<<>>> VERDICT DESC: " + verdict.getXhbRefAppResult().getAppResultDescr1()
                                + verdict.getXhbRefAppResult().getAppResultDescr2());
                        disposalToAdd.setVerdictCode(verdict.getXhbRefAppResult().getAppResultCode());
                        StringBuffer verdictDesc = new StringBuffer(verdict.getXhbRefAppResult().getAppResultDescr1());
                        if (null != verdict.getXhbRefAppResult().getAppResultDescr2()) {
                            verdictDesc.append(" ");
                            verdictDesc.append(verdict.getXhbRefAppResult().getAppResultDescr2());
                        }
                        disposalToAdd.setVerdictDesc(verdictDesc.toString());
                    } catch (XhbVerdictBeanNotFoundException ex) {
                        // This is expected as not all disposals will have a
                        // corresponding verdict, or the OBS_IND may be set to Y
                        // Just log and carry on
                        log.info("No verdict found for Disposal2Id = " + dv.getDisposal2Id());
                    }
                }
                populateVariationDisposals(dv.getDisposal2Id(), disposalToAdd);

                // add the disposal to the unrelated disposal structure
                // only if there is no PSD_DISPOSAL2_ID
                if (null == dv.getPsdDisposal2Id()) {
                    urd.addDisposal(disposalToAdd);
                }
            }

        } catch (ResultsControllerException e) {
            e.printStackTrace();
        }
        return urd; // return the populated unrelated disposals castor
        // object
    }

    private static void populateVariationDisposals(Integer disposal2Id,
            uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Disposal disposalToAdd) {
        log.debug("populateVariationDisposals - start");
        XhbDisposal2BasicValue[] variations = XhbDisposal2BeanHelper2.findRSByPsdDisposal2IdValue(disposal2Id);
        log.debug("populateVariationDisposals: variations size = " + variations.length);
        Variations varsToAdd = new Variations();
        for (int x = 0; x < variations.length; x++) {
            Variation var = new Variation();
            DisposalValue val = new DisposalValue(variations[x]);
            populateLinesForVariation(val);

            int dispTypeId = variations[x].getRefDisposalTypeId().intValue();
            DisposalReferenceValue drv = ResultsDatabase.getReferenceDisposal(new Integer(dispTypeId));
            String disposalCode = drv.getDisposalCode();
            log.debug("DisposalReferenceValue dispTypeId = " + drv.getRefDisposalTypeId());
            log.debug("DisposalValue dispTypeId = " + dispTypeId);
            String disposalDesc = drv.getTitle();
            String formBFText = drv.getFormBFText(val);
            log.debug("DisposalReferenceValue disposalCode = " + disposalCode);
            log.debug("DisposalReferenceValue disposalDesc = " + disposalDesc);
            log.debug("DisposalReferenceValue formBFText = " + formBFText);
            var.setDisposalCode(disposalCode);
            var.setDisposalDesc(disposalDesc);
            var.setFormBFText(formBFText);
            var.setIsMagistrateCourt(YesNoType.YES); // set up default value

            varsToAdd.addVariation(var);

        }
        log.debug("<<<<>>>> varsToAdd.getVariationCount(): " + varsToAdd.getVariationCount());
        if (varsToAdd.getVariationCount() > 0) {
            disposalToAdd.setVariations(varsToAdd);
        }
    }

    private static void populateLinesForVariation(DisposalValue val) {
        XhbDisposalLineBasicValue[] lines = XhbDisposalLineBeanHelper2.findByDisposal2IdValue(val.getDisposal2Id());
        for (int lineNo = 0; lineNo < lines.length; lineNo++) {
            val.addLine(lines[lineNo]);
        }

    }
}
