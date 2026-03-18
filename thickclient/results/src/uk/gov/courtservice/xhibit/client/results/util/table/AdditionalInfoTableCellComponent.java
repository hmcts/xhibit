package uk.gov.courtservice.xhibit.client.results.util.table;

/**
 * A lightweight component that allows the passing of multiple required
 * parameters to the editor and renderer of the additional information columns.
 * 
 * @author tz0d5m
 * @version $Revision: 1.6 $
 * @history Kelvin Davies - 04062009 - CCN1263 - Updated to include new Verdict Values - GLOJ, GJ, GAOJ
 */
public abstract class AdditionalInfoTableCellComponent {
    private final String alternateText;

    private final String otherText;

    private final String code;

    /**
     * @param code
     *            A <code>String</code> value of the plea/verdict/appeal
     *            result code.
     * @param alternateText
     *            A <code>String</code> containing the alternate offence text.
     * @param otherText
     *            A <code>String</code> containing the other offence text.
     * @param process
     *            <code>boolean</code> used to indicate if any values should
     *            actually displayed.
     */
    protected AdditionalInfoTableCellComponent(String code, String alternateText, String otherText, boolean process) {
        this.alternateText = alternateText;
        this.otherText = otherText;
        // if we do not process the component, then do not save code value...
        this.code = (process ? code : null);
    }

    /**
     * Acquire the text that should be displayed on screen, determined by the
     * type of additional information required. i.e. if this is for an "other
     * offence", the "other text" passed in previously will be returned.
     * 
     * @return A guaranteed not- <i>null </i> <code>String</code>.
     */
    public String getText() {
        String text = null;

        if (isOtherOffence()) {
            text = this.otherText;
        }

        if (isAlternateOffence()) {
            text = this.alternateText;
        }

        // ensure that we never return null...
        return ((text != null) ? text : "");
    }

    /**
     * Method used to determine if the code previously passed in represents an
     * "other offence".
     * 
     * @return <i>true </i> if this is an "other offence", <i>false </i>
     *         otherwise.
     */
    public boolean isOtherOffence() {
        return "O".equals(getCode());
    }

    /**
     * Method used to determine if the code previously passed in represents an
     * "alternate offence".
     * 
     * @return <i>true </i> if this is an "alternate offence", <i>false </i>
     *         otherwise.
     */
    public abstract boolean isAlternateOffence();

    /**
     * Method used to acquire the constant defined in
     * <code>PanelTableCell</code> that represents the panel type (other,
     * offence or none) that this component should be used to create.
     * 
     * @return The <code>int</code> value of one of the constants defined in
     *         <code>PanelTableCell</code>.
     */
    public int getShowParameter() {
        if (isOtherOffence()) {
            return AdditionalInfoTableCell.SHOW_OTHER;
        } else if (isAlternateOffence()) {
            return AdditionalInfoTableCell.SHOW_OFFENCE;
        }

        return AdditionalInfoTableCell.SHOW_NOTHING;
    }

    /**
     * Method used to acquire the code previously passed in.
     * 
     * @return The code.
     */
    protected String getCode() {
        return this.code;
    }

    /**
     * Determine if the cell that this component represents is editable or not.
     * It will be determmined editable if it is either an "other offence" or an
     * "alternate offence".
     * 
     * @return
     */
    public boolean isCellEditable() {
        return this.isOtherOffence() || this.isAlternateOffence();
    }

    public static final AdditionalInfoTableCellComponent getPleaComponent(String code, String alternateText,
            String otherText, boolean process) {
        return new AdditionalInfoTableCellComponent(code, alternateText, otherText, process) {
            public boolean isAlternateOffence() {
                return "GLO".equals(getCode()) || "GAO".equals(getCode());
            }
        };
    }

    public static final AdditionalInfoTableCellComponent getVerdictComponent(String code, String alternateText,
            String otherText, boolean process) {
        return new AdditionalInfoTableCellComponent(code, alternateText, otherText, process) {
            public boolean isAlternateOffence() {
                return "GAJ".equals(getCode()) || "GA".equals(getCode()) || "GLJ".equals(getCode())
                        || "GL".equals(getCode()) || "GAOJ".equals(getCode()) || "GLOJ".equals(getCode());
            }
        };
    }

    public static final AdditionalInfoTableCellComponent getAppealResultComponent(String code, String alternateText,
            String otherText, boolean process) {
        return new AdditionalInfoTableCellComponent(code, alternateText, otherText, process) {
            public boolean isAlternateOffence() {
                return "ACALO".equals(getCode());
            }

            /**
             * Appeal results screen component will never have an "other
             * offence"
             * 
             * @see uk.gov.courtservice.xhibit.client.results.util.table
             *      .AdditionalInfoTableCellComponent#isOtherOffence()
             */
            public boolean isOtherOffence() {
                return false;
            }
        };
    }
}