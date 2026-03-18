package uk.gov.courtservice.xhibit.common.results.vos;

// jdk
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;

/**
 * <P>
 * Name: DisposalReferenceValue
 * </P>
 * <P>
 * Description: This object represents the reference data for a disposal
 * </P>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author: William Fardell, Xdevelopment (2004)
 * @version: 1.0
 */
public class DisposalLineReferenceValue extends ResultValue implements Cloneable, Comparable, Serializable {
    
	static final long serialVersionUID = 162741423592964841L;
	
	// The text set for deleted line data
    private static String DELETED_LINE_DATA = "DELETED";

    /**
     * Formater for disposal dates, note date is returned in lower case and must
     * be converted to upper case before use. Use formatDisposalDate to get the
     * required value.
     */
    private static final SimpleDateFormat disposalDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

    /**
     * Used to format amounts
     */
    private static final DecimalFormat disposalAmountformat = new DecimalFormat("£0.00");

    // Data
    private int refDisposalLineId;

    private int refDisposalTypeId;

    private String disposalCode;

    private int templateVersion;

    private int dilSeqNo;

    private String data;

    private boolean inputFlag;

    private boolean screenPrint;

    private boolean formPrint;

    private String dbdestin;

    private String prompt;

    private String format;

    private boolean mandatory;

    private String dbsource;

    private String validation;

    private boolean multipleChoice;

    private String mcgroup1;

    private String mcgroup2;

    private int charMax;

    private boolean lineInsert;

    // copy set to true when making a copy, can only update copies
    private boolean copy = false;

    /**
     * Construct a new DisposalReferenceValue with the specified data
     */
    public DisposalLineReferenceValue(Integer refDisposalLineId, Integer refDisposalTypeId, String disposalCode,
            Integer templateVersion, Integer dilSeqNo, String data, String inputFlag, String screenPrint,
            String formPrint, String dbdestin, String prompt, String format, String mandatory, String dbsource,
            String validation, String multipleChoice, String mcgroup1, String mcgroup2, Integer charMax,
            String lineInsert) {
        if (refDisposalLineId == null) {
            throw new IllegalArgumentException("refDisposalLineId: null");
        }
        if (refDisposalTypeId == null) {
            throw new IllegalArgumentException("refDisposalTypeId: null");
        }
        if (disposalCode == null) {
            throw new IllegalArgumentException("disposalCode: null");
        }
        if (templateVersion == null) {
            throw new IllegalArgumentException("templateVersion: null");
        }
        if (dilSeqNo == null) {
            throw new IllegalArgumentException("dilSeqNo: null");
        }
        if (!isFlag(inputFlag)) {
            throw new IllegalArgumentException("inputFlag: " + inputFlag);
        }
        if (!isFlag(screenPrint)) {
            throw new IllegalArgumentException("screenPrint: " + screenPrint);
        }
        if (!isFlag(formPrint)) {
            throw new IllegalArgumentException("formPrint: " + formPrint);
        }
        if (!isFlag(mandatory)) {
            throw new IllegalArgumentException("mandatory: " + mandatory);
        }
        if (!isFlag(multipleChoice)) {
            throw new IllegalArgumentException("multipleChoice: " + multipleChoice);
        }
        if (!isFlag(lineInsert)) {
            throw new IllegalArgumentException("lineInsert: " + lineInsert);
        }

        this.refDisposalLineId = refDisposalLineId.intValue();
        this.refDisposalTypeId = refDisposalTypeId.intValue();
        this.disposalCode = disposalCode;
        this.templateVersion = templateVersion.intValue();
        this.dilSeqNo = dilSeqNo.intValue();
        this.data = data;
        this.inputFlag = createFlag(inputFlag);
        this.screenPrint = createFlag(screenPrint);
        this.formPrint = createFlag(formPrint);
        this.dbdestin = dbdestin;
        this.prompt = prompt;
        this.format = format;
        this.mandatory = createFlag(mandatory);
        this.dbsource = dbsource;
        this.validation = validation;
        this.multipleChoice = createFlag(multipleChoice);
        this.mcgroup1 = mcgroup1;
        this.mcgroup2 = mcgroup2;
        this.charMax = charMax == null ? -1 : charMax.intValue();
        this.lineInsert = createFlag(lineInsert);
    }

    // getters
    public int getRefDisposalLineId() {
        return refDisposalLineId;
    }

    public int getRefDisposalTypeId() {
        return refDisposalTypeId;
    }

    public String getDisposalCode() {
        return disposalCode;
    }

    public int getTemplateVersion() {
        return templateVersion;
    }

    public int getDilSeqNo() {
        return dilSeqNo;
    }

    public String getData() {
        return data;
    }

    public boolean isInput() {
        return inputFlag;
    }

    public boolean isScreenPrint() {
        return screenPrint;
    }

    public boolean isFormPrint() {
        return formPrint;
    }

    public String getDbDestin() {
        return dbdestin;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getFormat() {
        return format;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public String getDbSource() {
        return dbsource;
    }

    public String getValidation() {
        return validation;
    }

    public boolean isMultipleChoice() {
        return multipleChoice;
    }

    public String getMcGroup1() {
        return mcgroup1;
    }

    public String getMcGroup2() {
        return mcgroup2;
    }

    public int getCharMax() {
        return charMax;
    }

    public boolean isLineInsert() {
        return lineInsert;
    }

    // setters
    public void setRefDisposalLineId(int refDisposalLineId) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.refDisposalLineId = refDisposalLineId;
    }

    public void setRefDisposalTypeId(int refDisposalTypeId) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.refDisposalTypeId = refDisposalTypeId;
    }

    public void setDisposalCode(String disposalCode) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        if (disposalCode == null) {
            throw new IllegalArgumentException("disposalCode: null");
        }
        this.disposalCode = disposalCode;
    }

    public void setTemplateVersion(int templateVersion) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.templateVersion = templateVersion;
    }

    public void setDilSeqNo(int dilSeqNo) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.dilSeqNo = dilSeqNo;
    }

    public void setData(String data) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.data = data;
    }

    public void setMandatory(boolean mandatory) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.mandatory = mandatory;
    }

    public void setInput(boolean inputFlag) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.inputFlag = inputFlag;
    }

    public void setScreenPrint(boolean screenPrint) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.screenPrint = screenPrint;
    }

    public void setFormPrint(boolean formPrint) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.formPrint = formPrint;
    }

    public void setDbDestin(String dbdestin) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.dbdestin = dbdestin;
    }

    public void setPrompt(String prompt) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.prompt = prompt;
    }

    public void setFormat(String format) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.format = format;
    }

    public void isMandatory(boolean mandatory) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.mandatory = mandatory;
    }

    public void setDbSource(String dbsource) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.dbsource = dbsource;
    }

    public void setValidation(String validation) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.validation = validation;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.multipleChoice = multipleChoice;
    }

    public void setMcGroup1(String mcgroup1) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.mcgroup1 = mcgroup1;
    }

    public void setMcGroup2(String mcgroup2) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.mcgroup2 = mcgroup2;
    }

    public void setCharMax(int charMax) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.charMax = charMax;
    }

    public void setLineInsert(boolean lineInsert) {
        if (!copy) {
            throw new IllegalStateException("copy: false");
        }
        this.lineInsert = lineInsert;
    }

    /**
     * Clone this object
     */
    public Object clone() {
        try {
            DisposalLineReferenceValue value = (DisposalLineReferenceValue) super.clone();
            value.copy = true;
            return value;
        } catch (CloneNotSupportedException cnse) {
            throw new IllegalStateException(cnse.getMessage());
        }
    }

    /**
     * Copy this object
     */
    public DisposalLineReferenceValue copy() {
        return (DisposalLineReferenceValue) clone();
    }

    /**
     * Append debug information to the buffer
     */
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(DisposalLineReferenceValue.class.getName());
        buffer.append(" {refDisposalLineId=");
        buffer.append(refDisposalLineId);
        buffer.append(", refDisposalTypeId=");
        buffer.append(refDisposalTypeId);
        buffer.append(", disposalCode=");
        buffer.append(disposalCode);
        buffer.append(", templateVersion=");
        buffer.append(templateVersion);
        buffer.append(", dilSeqNo=");
        buffer.append(dilSeqNo);
        buffer.append(", data=");
        buffer.append(data);
        buffer.append(", inputFlag=");
        buffer.append(inputFlag);
        buffer.append(", screenPrint=");
        buffer.append(screenPrint);
        buffer.append(", formPrint=");
        buffer.append(formPrint);
        buffer.append(", dbdestin=");
        buffer.append(dbdestin);
        buffer.append(", prompt=");
        buffer.append(prompt);
        buffer.append(", format=");
        buffer.append(format);
        buffer.append(", mandatory=");
        buffer.append(mandatory);
        buffer.append(", dbsource=");
        buffer.append(dbsource);
        buffer.append(", validation=");
        buffer.append(validation);
        buffer.append(", multipleChoice=");
        buffer.append(multipleChoice);
        buffer.append(", mcgroup1=");
        buffer.append(mcgroup1);
        buffer.append(", mcgroup2=");
        buffer.append(mcgroup2);
        buffer.append(", charMax=");
        buffer.append(charMax);
        buffer.append(", lineInsert=");
        buffer.append(lineInsert);
        buffer.append("}");
    }

    /**
     * Creates a new line value with the specified details, checks data to
     * ensure it is valid
     * 
     * @param lineNumber
     *            the new number of the line
     * @throws ResultsValueException
     */
    public XhbDisposalLineBasicValue createValue(int value) {
        return createValue(0, value);
    }

    public XhbDisposalLineBasicValue createValue(int lineNumber, int value) {
        return createValue(0, String.valueOf(value));
    }

    public XhbDisposalLineBasicValue createValue(boolean flag) {
        return createValue(0, flag);
    }

    public XhbDisposalLineBasicValue createValue(int lineNumber, boolean flag) {
        return createValue(0, flag ? "Y" : "N");
    }

    public XhbDisposalLineBasicValue createValue(Date date) {
        return createValue(0, date);
    }

    public XhbDisposalLineBasicValue createValue(int lineNumber, Date date) {
        return createValue(0, date == null ? null : formatDisposalDate(date));
    }

    public XhbDisposalLineBasicValue createValue(String data) {
        return createValue(0, data);
    }

    public XhbDisposalLineBasicValue createValue(int lineNumber, String data) {
        XhbDisposalLineBasicValue value = new XhbDisposalLineBasicValue();
        value.setRefDisposalLineId(new Integer(getRefDisposalLineId()));
        value.setLineNumber(new Integer(lineNumber));
        value.setLineData(data);
        value.setObsInd("N");

        if (isValidValue(value)) {
            return value;
        } else {
            throw new ResultsValueException("Value " + value + " is invalid.");
        }
    }

    public XhbDisposalLineBasicValue copyValue(XhbDisposalLineBasicValue value) {
        XhbDisposalLineBasicValue newValue = new XhbDisposalLineBasicValue();
        newValue.setRefDisposalLineId(new Integer(getRefDisposalLineId()));
        newValue.setLineData(value.getLineData());
        newValue.setDelLineData(value.getDelLineData());
        newValue.setDelG1(value.getDelG1());
        newValue.setDelG2(value.getDelG2());
        newValue.setLineNumber(value.getLineNumber());
        newValue.setObsInd("N");
        if (isValidValue(newValue)) {
            return newValue;
        } else {
            throw new ResultsValueException("Value " + newValue + " is invalid.");
        }
    }

    /**
     * Validate Line
     * 
     * @param the
     *            line to validate
     * @return true if the line is valid
     */
    public boolean isValidValue(XhbDisposalLineBasicValue value) {
        return isValidRefDispoalLineId(value) && isValidLineNumber(value) && isValidLineData(value)
                && isValidDelLineData(value) && isValidDelG1(value) && isValidDelG2(value);
    }

    private boolean isValidRefDispoalLineId(XhbDisposalLineBasicValue value) {
        Integer refDisposalLineId = value.getRefDisposalLineId();
        return refDisposalLineId != null && refDisposalLineId.intValue() == getRefDisposalLineId();
    }

    private boolean isValidLineNumber(XhbDisposalLineBasicValue value) {
        Integer lineNumber = value.getLineNumber();
        return lineNumber != null && (isLineInsert() ? lineNumber.intValue() >= 0 : lineNumber.intValue() == 0);
    }

    private boolean isValidLineData(XhbDisposalLineBasicValue value) {
        /**
         * @todo Implement rule
         */
        return true;
    }

    private boolean isValidDelLineData(XhbDisposalLineBasicValue value) {
        /**
         * @todo Implement rule
         */
        return true;
    }

    private boolean isValidDelG1(XhbDisposalLineBasicValue value) {
        /**
         * @todo Implement rule
         */
        return true;
    }

    private boolean isValidDelG2(XhbDisposalLineBasicValue value) {
        /**
         * @todo Implement rule
         */
        return true;
    }

    /**
     * Return true if the specified line is deleted
     */
    public boolean isDeleted(XhbDisposalLineBasicValue value) {
        if (!isReferenceLineInstance(value)) {
            throw new IllegalArgumentException("value: " + value);
        }
        return DELETED_LINE_DATA.equals(value.getLineData()) && !_isDeletedG1(value) && !_isDeletedG2(value);
    }

    /**
     * Return true if the specified line is deleted in group 1
     */
    public boolean isDeletedG1(XhbDisposalLineBasicValue value) {
        if (!isReferenceLineInstance(value)) {
            throw new IllegalArgumentException("value: " + value);
        }
        return _isDeletedG1(value);
    }

    private boolean _isDeletedG1(XhbDisposalLineBasicValue value) {
        return mcgroup1 != null && "Y".equals(value.getDelG1());
    }

    /**
     * Return true if the specified line is deleted in group 1
     */
    public boolean isDeletedG2(XhbDisposalLineBasicValue value) {
        if (!isReferenceLineInstance(value)) {
            throw new IllegalArgumentException("value: " + value);
        }
        return _isDeletedG2(value);
    }

    private boolean _isDeletedG2(XhbDisposalLineBasicValue value) {
        return mcgroup2 != null && "Y".equals(value.getDelG2());
    }

    /**
     * Delete the line
     */
    public void delete(XhbDisposalLineBasicValue value) {
        if (!isReferenceLineInstance(value)) {
            throw new IllegalArgumentException("value: " + value);
        }
        String data = value.getLineData();
        if (!DELETED_LINE_DATA.equals(data)) {
            value.setDelLineData(data);
            value.setLineData(DELETED_LINE_DATA);
            // CREST erroneously sets the delg2 for anonymous deletes, we do
            // the same for compatibility
            value.setDelG2("Y");
        }
    }

    /**
     * Delete the line as a member of group 1
     */
    public void deleteG1(XhbDisposalLineBasicValue value) {
        if (!isReferenceLineInstance(value)) {
            throw new IllegalArgumentException("value: " + value);
        }
        if (mcgroup1 == null) {
            throw new IllegalStateException("mcgroup1: " + mcgroup1);
        }
        String data = value.getLineData();
        if (!DELETED_LINE_DATA.equals(data)) {
            value.setDelLineData(data);
            value.setLineData(DELETED_LINE_DATA);
        }
        value.setDelG1("Y");
    }

    /**
     * Delete the line as a member of group 2
     */
    public void deleteG2(XhbDisposalLineBasicValue value) {
        if (!isReferenceLineInstance(value)) {
            throw new IllegalArgumentException("value: " + value);
        }
        if (mcgroup2 == null) {
            throw new IllegalStateException("mcgroup2: " + mcgroup2);
        }
        String data = value.getLineData();
        if (!DELETED_LINE_DATA.equals(data)) {
            value.setDelLineData(data);
            value.setLineData(DELETED_LINE_DATA);
        }
        value.setDelG2("Y");
    }

    /**
     * Get the data from the line
     */
    public String getData(XhbDisposalLineBasicValue value) {
        if (!isReferenceLineInstance(value)) {
            throw new IllegalArgumentException("value: " + value);
        }

        return isDeleted(value) || isDeletedG1(value) || isDeletedG2(value) ? value.getDelLineData() : value
                .getLineData();
    }

    /**
     * Format the reference data
     */
    public String formatData() {
        return isAmount() ? formatAmount() : getData();
    }

    /**
     * Format the data from the line (used to format decimals)
     */
    public String formatData(XhbDisposalLineBasicValue value) {
        return isAmount() ? formatAmount(value) : getData(value);
    }

    /**
     * Format the data from the line as an amount
     */
    public String formatAmount(XhbDisposalLineBasicValue value) {
        return formatAmount(getData(value));
    }

    public String formatAmount() {
        return formatAmount(getData());
    }

    public String formatAmount(String data) {
        if (data != null) {
            try {
                return disposalAmountformat.format(Double.parseDouble(data));
            } catch (NumberFormatException e) {
                // return original value
            }
        }
        return data;
    }

    /**
     * Return true if the value is an amount in pounds, check validation is v4
     * and contains the word amount in the prompt
     */
    public boolean isAmount() {
        return validation != null && validation.equals("V4") && prompt != null
                && prompt.toLowerCase().indexOf("amount") != -1;
    }

    /**
     * Return true if the line references this reference line
     */
    public boolean isReferenceLineInstance(XhbDisposalLineBasicValue value) {
        if (value != null) {
            Integer refDisposalLineId = value.getRefDisposalLineId();
            return refDisposalLineId != null && getRefDisposalLineId() == refDisposalLineId.intValue();
        } else {
            return false;
        }
    }

    /**
     * Comparator Implementation, allows the object to be sorted into order
     * based on the dil seq no.
     * 
     * @param object
     *            the object to compare to
     * @return the result see Comparator for more information
     * @throws ClassCastException
     *             if parameter object is not comparable
     * @throws NullPointerException
     *             if parameter object is null
     */
    public int compareTo(Object object) throws ClassCastException, NullPointerException {
        int thisVal = getDilSeqNo();
        int anotherVal = ((DisposalLineReferenceValue) object).getDilSeqNo();
        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
    }

    //    
    // Date Utilities
    //

    public static boolean isValidDate(String date) {
        /**
         * todo tighten up validation date capitilisation etc.
         */
        if (date != null) {
            try {
                disposalDateFormat.parse(date);
                return true;
            } catch (ParseException pe) {
            }
        }
        return false;
    }

    public static String reformatDisposalDate(String date) {
        return formatDisposalDate(parseDisposalDate(date));
    }

    public static Date parseDisposalDate(String date) {
        if (date != null) {
            try {
                return disposalDateFormat.parse(date);
            } catch (ParseException pe) {
            }
        }
        throw new IllegalArgumentException("date: " + date);
    }

    public static String formatDisposalDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date: null");
        }
        return disposalDateFormat.format(date).toUpperCase();
    }

    //
    // Flag Utilities
    //

    private static boolean createFlag(String flag) {
        return "Y".equals(flag);
    }

    private static boolean isFlag(String flag) {
        return flag == null || "Y".equals(flag) || "N".equals(flag);
    }

}
