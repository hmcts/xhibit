package uk.gov.courtservice.xhibit.client.results;

import uk.gov.courtservice.xhibit.client.util.ArrayComboBoxModel;

/**
 * <p>
 * Title: AssentingDissentingComboBoxModel
 * </p>
 * <p>
 * Description: Model used to select the number of asscenting dissenting jurors.
 * It contains all the valid combinations a valid AssentingDissenting
 * combination.
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
public class AssentingDissentingComboBoxModel extends ArrayComboBoxModel {
    /**
     * Construct a new model with the default (null) value selected
     */
    public AssentingDissentingComboBoxModel() {
        this(null);
    }

    /**
     * Construct a new model setting the selected value to the parameter
     */
    public AssentingDissentingComboBoxModel(AssentingDissenting assDiss) {
        super(AssentingDissentingFactory.getInstance().getValues());
        if (assDiss != null) {
            setSelectedItem(assDiss);
        }
    }
}