package uk.gov.courtservice.framework.jdbc.core;

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
 * @author XHIBIT User
 * @version $Id: Parameter.java,v 1.4 2006/06/05 12:30:16 bzjrnl Exp $
 */
public class Parameter {
    // Enumerated constant for IN type
    private static final int IN_PARAM = 1;

    // Enumerated constant for OUT type
    private static final int OUT_PARAM = 2;

    // Enumerated constant for IN_OUT type
    private static final int IN_OUT_PARAM = 3;

    // SQL type of the parameter
    private final int sqlType;

    // Whether the parameter is IN, OUT or IN OUT
    private final int parameterType;

    // Value of the parameter
    private final Object value;

    /**
     * Private constructor
     * 
     * @param SQL
     *            type
     * @param Parameter
     *            type
     */
    private Parameter(int sqlType, int parameterType, Object value) {
        this.sqlType = sqlType;
        this.parameterType = parameterType;
        this.value = value;
    }

    /**
     * Creates an IN parameter for the SQL type
     * 
     * @param SQL
     *            type
     * @return
     */
    public static Parameter getInParameter(int sqlType, Object value) {
        return new Parameter(sqlType, Parameter.IN_PARAM, value);
    }

    /**
     * Creates an OUT parameter for the SQL type
     * 
     * @param SQL
     *            type
     * @return
     */
    public static Parameter getOutParameter(int sqlType) {
        return new Parameter(sqlType, Parameter.OUT_PARAM, null);
    }

    /**
     * Creates an IN OUT parameter for the SQL type
     * 
     * @param SQL
     *            type
     * @return
     */
    public static Parameter getInOutParameter(int sqlType, Object value) {
        return new Parameter(sqlType, Parameter.IN_OUT_PARAM, value);
    }

    /**
     * Returns the SQL type
     * 
     * @return
     */
    public int getSqlType() {
        return sqlType;
    }

    /**
     * Returns the value
     * 
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * Checks whether a parameter is in type
     * 
     * @return
     */
    public boolean isIn() {
        return parameterType == IN_PARAM || parameterType == IN_OUT_PARAM;
    }

    /**
     * Checks whether a parameter is out type
     * 
     * @return
     */
    public boolean isOut() {
        return parameterType == OUT_PARAM || parameterType == IN_OUT_PARAM;
    }
}
