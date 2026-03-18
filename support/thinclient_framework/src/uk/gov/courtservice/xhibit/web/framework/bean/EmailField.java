package uk.gov.courtservice.xhibit.web.framework.bean;

/**
 * <p>Title: EmailField</p>
 * <p>Description: This class provides a java object to be used by a bean
 * mapping an HTML page where the data can be modified.
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * 
 * @author  Edward Cawley, Xdevelopment LLP (2003)
 * $Revision: 1.4 $
 * $Log: EmailField.java,v $
 * Revision 1.4  2006/06/05 12:30:24  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 *
 * Revision 1.3  2006/05/31 14:23:52  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting
 *
 * Revision 1.2  2003/10/01 15:27:13  bzw8gp
 * Jon Powell
 *
 * remove unused variables
 *
 * Revision 1.1  2003/09/18 10:44:56  tzj8k5
 * Refactor move from thinclient/probation
 *
 * Revision 1.2  2003/08/11 08:23:20  bzw8gp
 * Jon Powell
 *
 * organise imports (remove unused)
 *
 * Revision 1.1  2003/03/24 16:45:23  fz0n8j
 * Added/Modified for Email functionality.
 *
 *
 */
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailField extends StringField {

    /**
     * Construct an Email field with a given value, assumes no length limit
     * 
     * @param newValue
     *            the value
     * @throws java.lang.IllegalArgumentException
     *             if the value is null
     */
    public EmailField(String newValue) throws IllegalArgumentException {
        super(newValue);
    }

    /**
     * Construct an Email field with a given value and length limit, and
     * nullable flag
     * 
     * @param newValue
     *            the value
     * @param newLength
     *            the max length of the field
     * @param newNullable
     *            wheather the field can be blank or null
     * @throws java.lang.IllegalArgumentException
     *             if the value is null and it isn't supposed to be
     */
    public EmailField(String newValue, int newLength, boolean newNullable) throws IllegalArgumentException {
        super(newValue, newLength, newNullable);
    }

    /**
     * Standard java bean setter
     * 
     * @param newValue
     *            the new value
     * @throws java.lang.IllegalArgumentException
     *             if the value is null and it isn't supposed to be
     */
    public void setValue(String newValue) throws IllegalArgumentException {
        if (newValue == null) {
            if (nullable) {
                newValue = "";
            } else {
                throw new IllegalArgumentException("newValue");
            }
        }
        if (newValue.length() == 0 && !nullable) {
            setErrorValue(newValue);
            setErrorMessageKey("stringfield.emptyString");
        }
        if (newValue.length() > length && length != 0) {
            setErrorValue(newValue);
            setErrorMessageKey("stringfield.tooLong");
        }
        if (newValue.length() > 0) {
            try {
                new InternetAddress(newValue);
            } catch (AddressException ae) {
                setErrorValue(newValue);
                setErrorMessageKey("emailfield.invalid");
            }
        }
        value = newValue;
    }

}
