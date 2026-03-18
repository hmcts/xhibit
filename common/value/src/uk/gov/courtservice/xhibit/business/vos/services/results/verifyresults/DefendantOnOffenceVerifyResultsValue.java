package uk.gov.courtservice.xhibit.business.vos.services.results.verifyresults;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
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
 * @author unascribed
 * @version $Id: DefendantOnOffenceVerifyResultsValue.java,v 1.3 2004/07/01
 *          07:30:38 tz0d5m Exp $
 */
public class DefendantOnOffenceVerifyResultsValue extends CSAbstractValue {
	private static final long serialVersionUID = -2035171763443139926L;
	private boolean verdicts = false;

    private boolean pleas = false;

    private boolean disposals = false;

    public boolean hasVerdicts() {
        return verdicts;
    }

    public boolean hasPleas() {
        return pleas;
    }

    public boolean hasDisposals() {
        return disposals;
    }

    /**
     * Helper method used to determine if this value object has any results. It
     * is classes as having results if the verdicts, pleas or disposals
     * <code>boolean</code> values have been set to <i>true</i>
     * 
     * @return <i>true</i> if there are any results.
     * @see #hasVerdicts()
     * @see #hasPleas()
     * @see #hasDisposals()
     * @since Version 1.3
     */
    public boolean hasResults() {
        return (hasDisposals() || hasVerdicts() || hasPleas());
    }

    /**
     * Set the property to indicate if there are verdicts for this defendant on
     * offence.
     * 
     * @param verdicts
     */
    public void setVerdicts(boolean verdicts) {
        this.verdicts = verdicts;
    }

    /**
     * Set the property to indicate if there are pleas for this defendant on
     * offence.
     * 
     * @param pleas
     */
    public void setPleas(boolean pleas) {
        this.pleas = pleas;
    }

    /**
     * Set the property to indicate if there are disposals for this defendant on
     * offence.
     * 
     * @param disposals
     */
    public void setDisposals(boolean disposals) {
        this.disposals = disposals;
    }
}