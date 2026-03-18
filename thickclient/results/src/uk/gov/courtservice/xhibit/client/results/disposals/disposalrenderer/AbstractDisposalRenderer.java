package uk.gov.courtservice.xhibit.client.results.disposals.disposalrenderer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.client.results.disposals.AggravatingReasonsModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DeportationModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalLineRenderer;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalLineRendererFactory;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalListener;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalProcessorManager;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalRenderer;
import uk.gov.courtservice.xhibit.client.results.disposals.HateCrimeModel;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;

/**
 * <p>
 * Title: AbstractDisposalRenderer
 * </p>
 * <p>
 * Description: Provide common renderer behaviour.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.16 $
 */
public abstract class AbstractDisposalRenderer implements DisposalRenderer {
    private static final Logger log = CSServices.getLogger(AbstractDisposalRenderer.class);

    /**
     * The disposal line renderers (do not access directly use get)
     */
    private Map disposalLineRenderers;

    /**
     * The disposal component (do not access directly use get)
     */
    private DisposalComponent disposalComponent;

    /**
     * The reference value can only be set once
     */
    private DisposalReferenceValue reference;

    /**
     * The value can only be set once
     */
    private DisposalValue disposal;

    /**
     * DisposalRenderer Implementation, process the reference then set it!
     */
    public void setReference(DisposalReferenceValue reference) {
        if (this.reference != null) {
            throw new IllegalStateException("reference: " + this.reference);
        }
        if (reference == null) {
            throw new IllegalArgumentException("reference: null");
        }
        this.reference = reference;
        process();
    }

    /**
     * DisposalRenderer Implementation, process the reference then set it!
     */
    protected void process() {
        DisposalProcessorManager.process(reference);
    }

    /**
     * DisposalRenderer Implementation
     */
    public DisposalReferenceValue getReference() {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }
        return reference;
    }

    /**
     * DisposalRenderer Implementation
     */
    public void setDisposal(DisposalValue disposal) {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }
        if (this.disposal != null) {
            throw new IllegalStateException("disposal: " + this.disposal);
        }
        if (disposal == null) {
            throw new IllegalArgumentException("disposal: null");
        }
        if (log.isDebugEnabled()) {
            log.debug("Setting Disposal Debug: " + disposal);
            log.debug("Setting Disposal Summary: " + reference.getCaseProgressSummaryText(disposal));
            log.debug("Setting Disposal Detail: " + reference.getCaseProgressDetailText(disposal));
        }
        this.disposal = disposal;

        for (int i = 0, c = reference.getLineCount(); i < c; i++) {
            Integer refDisposalLineId = new Integer(reference.getLine(i).getRefDisposalLineId());
            int s = disposal.getLineCount(refDisposalLineId);
            if (0 < s) {
                DisposalLineRenderer disposalLineRenderer = (DisposalLineRenderer) getDisposalLineRenderers().get(
                        refDisposalLineId);
                disposalLineRenderer.setLine(disposal.getLine(refDisposalLineId, 0));
                for (int j = 1; j < s; j++) {
					//don't show obsolete rows
                	if(!disposal.getLine(refDisposalLineId, j).getObsInd().equals("Y")){
                		disposalLineRenderer.setLine(disposal.getLine(refDisposalLineId, j));
                	}
                }
            }
        }
    }

    /**
     * DisposalRenderer Implementation
     */
    public DisposalValue getDisposal() {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }
        if (disposal == null) {
            throw new IllegalStateException("disposal: null");
        }
        
        disposal.clearLines();

        for (int i = 0, c = reference.getLineCount(); i < c; i++) {
            Integer refDisposalLineId = new Integer(reference.getLine(i).getRefDisposalLineId());
            DisposalLineRenderer disposalLineRenderer = (DisposalLineRenderer) getDisposalLineRenderers().get(
                    refDisposalLineId);
            for (int j = 0, s = disposalLineRenderer.getLineCount(); j < s; j++) {
                XhbDisposalLineBasicValue line = disposalLineRenderer.getLine(j);

                if (line != null) {
                    line.setDisposal2Id(disposal.getDisposal2Id());
                    disposal.addLine(line);
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Getting Disposal Debug: " + disposal);
            log.debug("Getting Disposal Summary: " + reference.getCaseProgressSummaryText(disposal));
            log.debug("Getting Disposal Detail: " + reference.getCaseProgressDetailText(disposal));
        }
        return disposal;
    }

    private Map getDisposalLineRenderers() {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }
        DisposalComponent dc = getDisposalComponent(); // Uses side effect
        if (dc != null) {
            return disposalLineRenderers;
        }
        return null;
    }

    /**
     * DisposalRenderer Implementation (Side Effect: initialises
     * disposalLineRenderers)
     */
    public DisposalComponent getDisposalComponent() {
        if (reference == null) {
            throw new IllegalStateException("reference: null");
        }

        if (disposalComponent == null) {
            DisposalComponent dc = createDisposalComponent(reference.isHateCrimeTabVisible(), reference.isAggravatingTabVisible());
            if (dc != null) {
                
                DeportationModel deportReasons = new DeportationModel();
                    deportReasons.setCustodial(reference.getCustodial());
                    deportReasons.setSuspended(reference.getSuspended());
                    deportReasons.setSeriousDrugOffence(reference.getSeriousDrugOffence());
                    deportReasons.setRecommendedDeportation(reference.getRecommendedDeportation());
                
                dc.setTitle(reference.getTitle());
                dc.setLineAvail(reference.getLineAvail());
                dc.setVersion(reference.getTemplateVersion());
                dc.setCode(reference.getDisposalCode());
                dc.setDeportationReason(deportReasons);
                dc.setDeportationVisibility(reference.getDeportationVisibilty());
                dc.setHateCrimeTabVisibility(reference.isHateCrimeTabVisible());
                dc.setAggravatingTabVisibility(reference.isAggravatingTabVisible());
                
                HateCrimeModel hateCrimeReasons = new HateCrimeModel();
                hateCrimeReasons.setGeneralDisability(reference.isGeneralDisability());
                hateCrimeReasons.setGeneralSexual(reference.isGeneralSexual());
                hateCrimeReasons.setGeneralTransgender(reference.isGeneralTransgender());
                hateCrimeReasons.setHateCrimeFlag(reference.isHateCrimeFlag());
                hateCrimeReasons.setRaceAndReligionAggravated(reference.isRaceAndReligionAggravated());
                hateCrimeReasons.setRacialAggravated(reference.isRacialAggravated());
                hateCrimeReasons.setReligionAggravated(reference.isReligionAggravated());
                hateCrimeReasons.setVictimDisability(reference.isVictimDisability());
                hateCrimeReasons.setVictimSexual(reference.isVictimSexual());
                hateCrimeReasons.setVictimTransgender(reference.isVictimTransgender());
                dc.setHateCrimeReasons(hateCrimeReasons);
                
                AggravatingReasonsModel aggravatingReasons = new AggravatingReasonsModel();
                aggravatingReasons.setAssaultOnWorkers(reference.isAggravatingAssaultOnWorkers());
            	aggravatingReasons.setTerroristConnection(reference.isAggravatingTerroristConnection());
                aggravatingReasons.setEmergencyWorkers(reference.isAggravatingEmergencyWorkers());
                aggravatingReasons.setHostility(reference.isAggravatingHostility());
                aggravatingReasons.setSexualOrientation(reference.isAggravatingSexualOrientation());
                aggravatingReasons.setSexualOrientationOfVictim(reference.isAggravatingSexualOrientationOfVictim());
                aggravatingReasons.setTransgender(reference.isAggravatingTransgender());
                aggravatingReasons.setTransgenderOfVictim(reference.isAggravatingTransgenderOfVictim());
                dc.setAggravatingReasons(aggravatingReasons);
                
                disposalComponent = dc;

                Map dlrs = new HashMap();

                DataComponent previousDataComponent = null;
                for (int i = 0, c = reference.getLineCount(); i < c; i++) {
                    DisposalLineReferenceValue dlrv = reference.getLine(i);
                    DisposalLineRenderer dlr = DisposalLineRendererFactory.create(dlrv);

                    DataComponent currentDataComponent = dlr.getDataComponent();
                    if (currentDataComponent != null) {
                        currentDataComponent.setPreviousDataComponent(previousDataComponent);
                    }

                    dc.add(dlr.getPromptComponent(), currentDataComponent, dlr.getInsertComponent());
                    dlrs.put(new Integer(dlrv.getRefDisposalLineId()), dlr);

                    previousDataComponent = currentDataComponent;
                }
                disposalLineRenderers = dlrs;
            }
        }
        return disposalComponent;
    }

    /**
     * Return true if the disposal has errors
     */
    public boolean isComplete() {
        return getDisposalComponent().isComplete();
    }

    /**
     * Add the listener
     */
    public void addDisposalListener(DisposalListener listener) {
        getDisposalComponent().addDisposalListener(listener);
    }

    /**
     * Remove the listener
     */
    public void removeDisposalListener(DisposalListener listener) {
        getDisposalComponent().removeDisposalListener(listener);
    }

    /**
     * Create an instance of the disposal component
     */
    protected abstract DisposalComponent createDisposalComponent(boolean hateCrimeTabVisible, boolean aggravatingTabVisible);

}
