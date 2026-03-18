package uk.gov.courtservice.xhibit.client.util.table.style;

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
 * @version 1.0
 */

public class StyleTableCellValue {

    private String text;

    private int attribute;

    public StyleTableCellValue() {
    }

    public StyleTableCellValue(String text, int attribute) {
        setText(text);
        setAttribute(attribute);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public int getAttribute() {
        return attribute;
    }

}