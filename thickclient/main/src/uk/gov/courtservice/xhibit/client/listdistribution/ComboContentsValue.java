package uk.gov.courtservice.xhibit.client.listdistribution;

/**
 * <p>
 * Title: DocumentFormatValue
 * </p>
 * <p>
 * Description: Value object to represent the document format code which is
 * stored in the database and the value which is displayed on screen. Used in
 * building combo boxes.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: ComboContentsValue.java,v 1.4 2006/06/27 14:50:17 xztnfq Exp $
 */

public class ComboContentsValue implements Comparable{
    private String code;

    private String value;

    public ComboContentsValue() {
    }

    // accessor methods
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * @return The string to display in the drop down
     */
    public String toString() {
        return value;
    }

    /**
     * Compare the toString() values to enable sorting of objects
     */
    public int compareTo(Object o) {
        return this.toString().compareTo(((ComboContentsValue)o).toString());
    }
}