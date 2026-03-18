package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.Calendar;

/**
 * <p>
 * Title: Stores data specific to adding an Indictment.
 * </p>
 * <p>
 * Description: Used to store data entered/selected on the screens in the
 * AddIndictmentWizard that is specific to an Indictment. Data generic to
 * Charges is stored in the super class: ChargeWizardModel.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class IndictmentWizardModel extends ChargeWizardModel {
    private Calendar _indReceivedDate = null;

    private Calendar _prosPapersServedDate = null;

    private boolean _indictmentDetailsPopulated = false;

    /**
     * Creates a default IndictmentWizardModel.
     */
    public IndictmentWizardModel() {
    }

    /**
     * Gets the indictment received date.
     * 
     * @return the indictment received date.
     */
    public Calendar getIndReceivedDate() {
        return _indReceivedDate;
    }

    /**
     * Sets the indictment received date to the specified date.
     * 
     * @param indReceivedDate
     *            date the indictment was received.
     */
    public void setIndReceivedDate(Calendar indReceivedDate) {
        _indReceivedDate = indReceivedDate;
    }

    /**
     * Gets the prosecution papers served date.
     * 
     * @return the prosecution papers served date.
     */
    public Calendar getProsPapersServedDate() {
        return _prosPapersServedDate;
    }

    /**
     * Sets the prosecution papers served date to the specified date.
     * 
     * @param prosPapersServedDate
     *            date the prosecution papers were served.
     */
    public void setProsPapersServedDate(Calendar prosPapersServedDate) {
        _prosPapersServedDate = prosPapersServedDate;
    }

    /**
     * Gets the value of indictmentDetailsPopulated.
     * 
     * @return true if all of the required IndictmentDetailsPanel screen fields
     *         were populated.
     */
    public boolean isIndictmentDetailsPopulated() {
        return _indictmentDetailsPopulated;
    }

    /**
     * Sets the indictmentDetailsPopulated to the specified value.
     * 
     * @param indictmentDetailsPopulated
     *            whether or not the required fields on the
     *            IndictmentDetailsPanel have been populated.
     */
    public void setIndictmentDetailsPopulated(boolean indictmentDetailsPopulated) {
        _indictmentDetailsPopulated = indictmentDetailsPopulated;
    }

}