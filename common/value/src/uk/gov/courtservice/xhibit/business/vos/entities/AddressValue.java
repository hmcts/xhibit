package uk.gov.courtservice.xhibit.business.vos.entities;

import org.apache.axis.utils.StringUtils;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;

/**
 * <p>
 * Title: AddressValue
 * </p>
 * <p>
 * Description: This object value is used to store an address, as specified in
 * the Address table.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Laurent Bossard
 * @version 1.0
 */

public class AddressValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private Integer addressID;

    private String address1;

    private String address2;

    private String address3;

    private String address4;

    private String town;

    private String county;

    private String postcode;

    private String country;    

    public AddressValue() {
        super();
    }

    /**
     * @param address1
     * @param address2
     * @param address3
     * @param address4
     * @param town
     * @param county
     * @param postcode
     * @param country
     * @roseuid 3DB8093700ED
     */
    public AddressValue(String address1, String address2, String address3, String address4, String town, String county,
            String postcode, String country) {
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
        this.country = country;
    }
    
    /**
     * @param address Basic Value
     * 
     */
    public AddressValue( XhbAddressBasicValue addBasVal) {
        this.address1 = addBasVal.getAddress1();
        this.address2 = addBasVal.getAddress2();
        this.address3 = addBasVal.getAddress3();
        this.address4 = addBasVal.getAddress4();
        this.town = addBasVal.getTown();
        this.county = addBasVal.getCounty();
        this.postcode = addBasVal.getPostcode();
        this.country = addBasVal.getCountry();
    }
    
    
    public AddressValue(AddressValue addressValue) {
        if (addressValue != null) {
            this.address1 = addressValue.getAddress1();
            this.address2 = addressValue.getAddress2();
            this.address3 = addressValue.getAddress3();
            this.address4 = addressValue.getAddress4();
            this.town = addressValue.getTown();
            this.county = addressValue.getCounty();
            this.postcode = addressValue.getPostcode();
            this.country = addressValue.getCountry();
        
            this.addressID = addressValue.getAddressID();
            setDirty(addressValue.isDirty());
            this.setVersion(addressValue.getVersion());
            this.setId(addressValue.getId());
        }
    }

    public AddressValue(Integer addressID, String address1, String address2, String address3, String address4,
            String town, String county, String postcode, String country) {
        this(address1, address2, address3, address4, town, county, postcode, country);
        this.addressID = addressID;
    }

    /**
     * @return java.lang.Integer
     */
    public Integer getAddressID() {
        return addressID;
    }

    /**
     * @return java.lang.String
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @return java.lang.String
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @return java.lang.String
     */
    public String getAddress3() {
        return address3;
    }

    /**
     * @return java.lang.String
     */
    public String getAddress4() {
        return address4;
    }

    /**
     * @return java.lang.String
     */
    public String getTown() {
        return town;
    }

    /**
     * @return java.lang.String
     */
    public String getCounty() {
        return county;
    }

    /**
     * @return java.lang.String
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @return java.lang.String
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param String
     *            address1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @param String
     *            address2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @param String
     *            address3
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     * @param String
     *            address4
     */
    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    /**
     * @param String
     *            town
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * @param String
     *            county
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * @param String
     *            postcode
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @param String
     *            country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    
    public String getHumanReadableAddressString()
    {
    	String lineSeparator = System.getProperty("line.separator");
    	StringBuilder humanReadableAddress = new StringBuilder();
    	if(!StringUtils.isEmpty(this.address1)) 	{
    		humanReadableAddress.append(this.address1);
    		humanReadableAddress.append(lineSeparator);
    	}
    	if(!StringUtils.isEmpty(this.address2)) 	{
    		humanReadableAddress.append(this.address2);
    		humanReadableAddress.append(lineSeparator);
    	}
    	if(!StringUtils.isEmpty(this.address3)) 	{
    		humanReadableAddress.append(this.address3);
    		humanReadableAddress.append(lineSeparator);
    	}
    	if(!StringUtils.isEmpty(this.address4)) 	{
    		humanReadableAddress.append(this.address4);
    		humanReadableAddress.append(lineSeparator);
    	}
    	if(!StringUtils.isEmpty(this.town)) 	{
    		humanReadableAddress.append(this.town);
    		humanReadableAddress.append(lineSeparator);
    	}
    	if(!StringUtils.isEmpty(this.county)) 	{
    		humanReadableAddress.append(this.county);
    		humanReadableAddress.append(lineSeparator);
    	}
    	if(!StringUtils.isEmpty(this.postcode)) 	{
    		humanReadableAddress.append(this.postcode);
    		humanReadableAddress.append(lineSeparator);
    	}
    	return humanReadableAddress.toString();
    }
    
    @Override
    public void setId(Integer addressID) {
        super.setId(addressID);
        this.addressID = addressID; 
    }
}
