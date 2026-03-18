package uk.gov.courtservice.framework.services.validation;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: DataConverter
 * </p>
 * <p>
 * Description: Provides a mechanism to convert between Java string name types
 * and internal representation of data types
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

class DataConverter extends CSBusinessException {
    /**
     */
    public static final int STRING = 1;

    /**
     */
    public static final int INT = 2;

    /**
     */
    public static final int FLOAT = 3;

    /**
     */
    public static final int DOUBLE = 4;

    /**
     */
    public static final int LONG = 5;

    /**
     */
    public static final int SHORT = 6;

    /**
     */
    public static final int BYTE = 7;

    // add new data types here

    /**
     * 
     * @param dataTypeName
     *            name of Java data type.
     * @return internal value of Java data type.
     */
    public static int getInternalDataType(String dataTypeName) {
        if (dataTypeName.equalsIgnoreCase("String"))
            return STRING;
        if (dataTypeName.equalsIgnoreCase("int"))
            return INT;
        if (dataTypeName.equalsIgnoreCase("integer"))
            return INT;
        if (dataTypeName.equalsIgnoreCase("float"))
            return FLOAT;
        if (dataTypeName.equalsIgnoreCase("double"))
            return DOUBLE;
        if (dataTypeName.equalsIgnoreCase("long"))
            return LONG;
        if (dataTypeName.equalsIgnoreCase("short"))
            return SHORT;
        if (dataTypeName.equalsIgnoreCase("byte"))
            return BYTE;

        return 0;
    }

    /**
     * 
     * @param internalDataType
     *            use public available static vos only
     * @return name of corresponding Java data type.
     */
    public static String getJavaDataType(int internalDataType) {
        switch (internalDataType) {
        case STRING:
            return "String";
        case INT:
            return "int or Integer";
        case FLOAT:
            return "float or Float";
        case DOUBLE:
            return "double or Double";
        case LONG:
            return "long or Long";
        case SHORT:
            return "short or Short";
        case BYTE:
            return "byte or Byte";
        default:
            return null;
        }
    }
}