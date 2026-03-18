//
//package uk.gov.courtservice.xhibit.test.business.entities.address;
//
//import junit.framework.*;
//import org.apache.log4j.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
//
//public class TestAddressValue extends TestCase
//{
//  private static Logger log =  CSServices.getLogger(TestAddressValue.class);
//
//  public TestAddressValue(String s)
//  {
//    super(s);
//  }
//
//  protected void setUp()
//  {
//  }
//
//  protected void tearDown()
//  {
//  }
//
//  public void testGetAddress1() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String stringRet = addressvalue.getAddress1();
//    log.debug("TestAddressValue.getAddress1() - " +  stringRet );
//  }
//  public void testGetAddress2() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String stringRet = addressvalue.getAddress2();
//    log.debug("TestAddressValue.getAddress2() - " +  stringRet );
//  }
//  public void testGetAddress3() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String stringRet = addressvalue.getAddress3();
//    log.debug("TestAddressValue.getAddress3() - " +  stringRet );
//  }
//  public void testGetAddress4() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String stringRet = addressvalue.getAddress4();
//    log.debug("TestAddressValue.getAddress4() - " +  stringRet );
//  }
//  public void testGetAddressID() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    Integer integerRet = addressvalue.getAddressID();
//    log.debug("TestAddressValue.getAddressID() - " +  integerRet );
//  }
//  public void testGetCountry() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String stringRet = addressvalue.getCountry();
//    log.debug("TestAddressValue.getCountry() - " +  stringRet );
//  }
//  public void testGetCounty() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String stringRet = addressvalue.getCounty();
//    log.debug("TestAddressValue.getCounty() - " +  stringRet );
//  }
//  public void testGetPostcode() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String stringRet = addressvalue.getPostcode();
//    log.debug("TestAddressValue.getPostcode() - " +  stringRet );
//  }
//  public void testGetTown() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String stringRet = addressvalue.getTown();
//    log.debug("TestAddressValue.getTown() - " +  stringRet );
//  }
//  public void testSetAddress1() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String address11=  "Address 1 modified";
//    addressvalue.setAddress1(address11);
//    log.debug("TestAddressValue.setAddress1() - " +  addressvalue.getAddress1() );
//  }
//  public void testSetAddress2() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String address21=  "Address 2 modified";
//    addressvalue.setAddress2(address21);
//    log.debug("TestAddressValue.setAddress2() - " +  addressvalue.getAddress2() );
//  }
//  public void testSetAddress3() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String address31=  "Address 3 modified";
//    addressvalue.setAddress3(address31);
//    log.debug("TestAddressValue.setAddress3() - " +  addressvalue.getAddress3() );
//  }
//  public void testSetAddress4() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String address41=  "Address 4 modified";
//    addressvalue.setAddress4(address41);
//    log.debug("TestAddressValue.setAddress4() - " +  addressvalue.getAddress4() );
//  }
//  public void testSetCountry() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String country1=  "New Country";
//    addressvalue.setCountry(country1);
//    log.debug("TestAddressValue.setCountry() - " +  addressvalue.getCountry() );
//  }
//  public void testSetCounty() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String county1=  "new County";
//    addressvalue.setCounty(county1);
//    log.debug("TestAddressValue.setCounty() - " +  addressvalue.getCounty() );
//  }
//  public void testSetPostcode() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String postcode1=  "New Postcode";
//    addressvalue.setPostcode(postcode1);
//    log.debug("TestAddressValue.setPostcode() - " +  addressvalue.getPostcode() );
//  }
//  public void testSetTown() {
//    String val1=  "address1";
//    String val2=  "address2";
//    String val3=  "address3";
//    String val4=  "address4";
//    String val5=  "town";
//    String val6=  "county";
//    String val7=  "postcode";
//    String val8=  "country";
//    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);
//    String town1=  "New Town";
//    addressvalue.setTown(town1);
//    log.debug("TestAddressValue.setTown() - " +  addressvalue.getTown() );
//  }
//}
//