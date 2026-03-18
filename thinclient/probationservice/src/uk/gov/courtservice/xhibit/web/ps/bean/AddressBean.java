package uk.gov.courtservice.xhibit.web.ps.bean;

/**
 * <p>Title: AddressBean</p>
 * <p>Description: This holds the address details according to how they are stored in the database</p>
 * 
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * 
 * @author  Edward Cawley, Xdevelopment LLP (2003)
 * $Revision: 1.11 $
 * $Log: AddressBean.java,v $
 * Revision 1.11  2006/06/05 12:32:27  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 *
 * Revision 1.10  2006/05/31 14:26:55  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting
 *
 * Revision 1.9  2006/04/26 09:01:55  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade
 *
 * Revision 1.8  2003/09/18 10:41:41  tzj8k5
 * Validation of Postcode added
 *
 * Revision 1.7  2003/03/17 11:32:20  fz0n8j
 * Added revision cvs comments. ecawley
 *
 * Revision 1.6  2003/03/14 20:56:19  fz0n8j
 * Uses modified stringfield. ecawley
 *
 * Revision 1.5  2003/03/11 15:46:38  fz0n8j
 * Added CVS Log comments - ecawley
 *
 */

import uk.gov.courtservice.xhibit.web.framework.bean.PostCodeField;
import uk.gov.courtservice.xhibit.web.framework.bean.StringField;

public class AddressBean {
    /**
     * Address line 1
     */
    private StringField line1;

    /**
     * Address line 2
     */
    private StringField line2;

    /**
     * Address line 3
     */
    private StringField line3;

    /**
     * Address line 4
     */
    private StringField line4;

    /**
     * The town part of the address
     */
    private StringField town;

    /**
     * The county part of the address
     */
    private StringField county;

    /**
     * The postcode part of the address
     */
    private PostCodeField postcode;

    /**
     * The country part of the address
     */
    private StringField country;

    /**
     * Construct a bean using line1, line2, line3 , line4, town, county,
     * postcode and country
     * 
     * @param newLine1
     *            line 1 of the address
     * @param newLine2
     *            line 2 of the address
     * @param newLine3
     *            line 3 of the address
     * @param newLine4
     *            line 4 of the address
     * @param newTown
     *            town part of the address
     * @param newCounty
     *            county part of the address
     * @param newPostcode
     *            postcode part of the address
     * @param newCountry
     *            country of the address
     * @throws IllegalArgumentException
     *             if any of the String arguments are null
     */
    public AddressBean(String newLine1, String newLine2, String newLine3, String newLine4, String newTown,
            String newCounty, String newPostcode, String newCountry) throws IllegalArgumentException {
        setLine1(new StringField(newLine1, 30, false));
        setLine2(new StringField(newLine2, 30, true));
        setLine3(new StringField(newLine3, 30, true));
        setLine4(new StringField(newLine4, 30, true));
        setTown(new StringField(newTown, 30, true));
        setCounty(new StringField(newCounty, 30, true));
        setPostcode(new PostCodeField(newPostcode));
        setCountry(new StringField(newCountry, 255, true));
    }

    /**
     * Used to find out if the data in the bean is valid
     * 
     * @return a boolean showing the status of the bean
     */
    public boolean isValid() {
        if (line1.getErrorValue() != null || line2.getErrorValue() != null || line3.getErrorValue() != null
                || line4.getErrorValue() != null || town.getErrorValue() != null || county.getErrorValue() != null
                || postcode.getErrorValue() != null || country.getErrorValue() != null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Standard java bean accessor
     * 
     * @return the first line of the address
     */
    public StringField getLine1() {
        return line1;
    }

    /**
     * Standard java bean setter
     * 
     * @param newLine1
     *            the first line of the address
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setLine1(StringField newLine1) throws IllegalArgumentException {
        if (newLine1 == null) {
            throw new IllegalArgumentException("newLine1");
        }
        line1 = newLine1;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the second line of the address
     */
    public StringField getLine2() {
        return line2;
    }

    /**
     * Standard java bean setter
     * 
     * @param newLine2
     *            the second line of the address
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setLine2(StringField newLine2) throws IllegalArgumentException {
        if (newLine2 == null) {
            throw new IllegalArgumentException("newLine2");
        }
        line2 = newLine2;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the third line of the address
     */
    public StringField getLine3() {
        return line3;
    }

    /**
     * Standard java bean setter
     * 
     * @param newLine1
     *            the third line of the address
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setLine3(StringField newLine3) throws IllegalArgumentException {
        if (newLine3 == null) {
            throw new IllegalArgumentException("newLine3");
        }
        line3 = newLine3;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the fourth line of the address
     */
    public StringField getLine4() {
        return line4;
    }

    /**
     * Standard java bean setter
     * 
     * @param newLine4
     *            the fourth line of the address
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setLine4(StringField newLine4) throws IllegalArgumentException {
        if (newLine4 == null) {
            throw new IllegalArgumentException("newLine4");
        }
        line4 = newLine4;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the town part of the address
     */
    public StringField getTown() {
        return town;
    }

    /**
     * Standard java bean setter
     * 
     * @param newTown
     *            the town part of the address
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setTown(StringField newTown) throws IllegalArgumentException {
        if (newTown == null) {
            throw new IllegalArgumentException("newTown");
        }
        town = newTown;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the county part of the address
     */
    public StringField getCounty() {
        return county;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCounty
     *            the county part of the address
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCounty(StringField newCounty) throws IllegalArgumentException {
        if (newCounty == null) {
            throw new IllegalArgumentException("newCounty");
        }
        county = newCounty;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the postcode part of the address
     */
    public PostCodeField getPostcode() {
        return postcode;
    }

    /**
     * Standard java bean setter
     * 
     * @param newPostcode
     *            the postcode part of the address
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setPostcode(PostCodeField newPostcode) throws IllegalArgumentException {
        if (newPostcode == null) {
            throw new IllegalArgumentException("newPostcode");
        }
        postcode = newPostcode;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the country part of the address
     */
    public StringField getCountry() {
        return country;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCountry
     *            the country part of the address
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCountry(StringField newCountry) throws IllegalArgumentException {
        if (newCountry == null) {
            throw new IllegalArgumentException("newCountry");
        }
        country = newCountry;
    }
}
