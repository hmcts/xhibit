//package uk.gov.courtservice.xhibit.test.business.entities.refhearingtype;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.RandomValues;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingType;
//import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingTypeHome;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.TreeSet;
//
///**
// * This class only tests finder methods of RefHearingType.
// *
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @author Jem Marsh
// * @version 1.3
// */
//public class TestRefHearingType extends TestCase {
//
//	private Logger log =  CSServices.getLogger(TestRefHearingType.class);
//	private Collection hearColls = new ArrayList();
//	public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//	public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//
//
//	public TestRefHearingType(String aString) {
//		super(aString);
//	}
//
//	protected void setUp() throws Exception {
//
//		log.debug("setUp()");
//		log.debug("deleting data");
//		TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//		TestUtils.execSql("delete from xhb_court");
//		TestUtils.execSql("Delete from XHB_ADDRESS");
//
//		log.debug("Insert Address and Court Record");
//		TestUtils.execSql(addrSQL);
//		TestUtils.execSql(courtSQL);
//
//		RandomValues random = new RandomValues();
//		log.debug("creating hearingType value");
//		RefHearingTypeBasicValue values; /** @todo remove if not used */
//		RefHearingTypeBasicValue eachValue = null;
//		Integer eachKey = null;
//		Integer eachVersion = new Integer(1);
//		String eachCategory = "category";
//		Integer eachCourtId = new Integer(101);
//		String eachCode = null;
//		String eachDescription = "description";
//		Integer eachListSequence = null;
//		String eachObsoleteIndicator = "N";
//		Integer eachSequenceNo = null;
//		for(int i = 0; i<10; i++) {
//			eachKey = new Integer(i);
//			eachCode = random.randomAlphaNumericString(5);
//			eachListSequence = new Integer(i);
//			eachSequenceNo = new Integer(i);
//			eachValue = new RefHearingTypeBasicValue(eachKey, eachVersion, eachCategory, eachCourtId, eachCode, eachDescription, eachListSequence, eachObsoleteIndicator, eachSequenceNo);
//			hearColls.add(eachValue);
//		}
//
//
//		log.debug("creating sql statemnet to insert into refHearingType");
//		Iterator itr = hearColls.iterator();
//		for(int i = 0; i< 10; i++) {
//			values =(RefHearingTypeBasicValue) itr.next();
//			String sqlHearing = "INSERT into XHB_REF_HEARING_TYPE (REF_HEARING_TYPE_ID, HEARING_TYPE_CODE, HEARING_TYPE_DESC, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, XHB_VERSION, LIST_SEQUENCE, SEQ_NO, CATEGORY, COURT_ID, OBS_IND) vos (\n"
//					 +i +", '" + values.getHearingTypeCode()+ "' , '"+ values.getHearingTypeDesc()+"', '1-dec-02', '1-dec-02', 'Bush','Bush Senior',"+i+", '"+values.getListSequence()+","+values.getSeqNo()+",'"+values.getCategory()+"', 1, 'N')";
//
//			log.debug("sql=" + sqlHearing);
//			log.debug("inserted sqlHearing into refHearingType table");
//			TestUtils.execSql(sqlHearing);
//		}
//
//		log.debug("inserted sqlHearing into refHearingType table");
//	}
//
//	protected void tearDown() throws Exception {
//
//		log.debug("tearDown()");
//		log.debug("deleting data");
//		TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//		TestUtils.execSql("delete from xhb_court");
//		TestUtils.execSql("Delete from XHB_ADDRESS");
//	}
///*
//	public void testCreate() {
//		try {
//			RefHearingTypeHome home = lookupHome();
//			log.debug("testCreate() - Got RefHearingTypeHome");
//			RefHearingType local = home.create(new Integer(4), new Integer(4), new Integer(4));
//			log.debug("refHearingTypeId : " + local.getRefHearingTypeId());
//		} catch(Exception e) {
//			log.debug("create() is failed");
//			e.printStackTrace();
//			fail();
//		}
//	}
//*/
//	public void testFindRefHearingTypesByCourtId()    {
//		log.debug("***testFindRefHearingTypesByCourtId() start");
//		RefHearingTypeBasicValue val = null;
//		Collection hearingCollection = null;
//		Collection hearingValues = new ArrayList();
//		RefHearingType hearing = null;
//
//		try {
//
//			RefHearingTypeHome home = (RefHearingTypeHome)lookupHome();
//			log.debug("testFindRefHearingTypesByCourtId() - Got RefHearingTypeHome");
//
//			hearingCollection = home.findByCourtId(new Integer("1"));
//			log.debug("No. of rows returned : " + hearingCollection.size());
//			assertEquals(hearColls.size(), hearingCollection.size());
//
//			Iterator itr = hearingCollection.iterator();
//			do { //for(int i = 0; i<10; i++)
//				RefHearingTypeBasicValue hearingValue = new RefHearingTypeBasicValue(hearing.getRefHearingTypeId(), hearing.getVersion());
//				hearing =(RefHearingType)itr.next();
//
//				hearingValue.setCategory(hearing.getCategory());
//				hearingValue.setListSequence(hearing.getListSequence());
//				hearingValue.setSeqNo(hearing.getSeqNo());
//				hearingValue.setHearingTypeCode(hearing.getHearingTypeCode());
//				hearingValue.setHearingTypeDesc(hearing.getHearingTypeDesc());
//				hearingValue.setObsInd(hearing.getObsInd());
//				hearingValue.setCourtId(hearing.getCourtId());
//
//				//CSServices.getEJBServices().copyAttributes(hearing,hearingValue);
//				System.out.println("hearing desc = " + hearingValue.getHearingTypeDesc());
//				hearingValues.add(hearingValue);
//				log.debug("size = " + hearingValues.size());
//			}
//			while(itr.hasNext());
//
//	 /*for(int i = 0; i<hearingValues.size(); i++)
//	 {
//  val = (RefHearingTypeValue)hearingValues.get(i);
//  log.debug("HearingDesc = " + val.getHearingTypeDesc());
//  log.debug("int i = " + i);
//	 }*/
//			log.debug("here");
//			TreeSet actual = new TreeSet();
//			TreeSet expected = new TreeSet();
//			log.debug("created tree set");
//			Iterator itrE = hearingValues.iterator();
//			log.debug("got iterator");
//
//			while(itrE.hasNext()) {
//				RefHearingTypeBasicValue firmVal = new RefHearingTypeBasicValue();
//				firmVal = (RefHearingTypeBasicValue)itrE.next();
//				String firm = firmVal.toString();
//				log.debug("firmactual = " + firm);
//				expected.add(firm);
//
//			}
//
//			Iterator itrA = hearColls.iterator();
//			while(itrA.hasNext()) {
//				RefHearingTypeBasicValue firmVal = new RefHearingTypeBasicValue();
//				firmVal = (RefHearingTypeBasicValue)itrA.next();
//				String firm = firmVal.toString();
//				log.debug("firmexpected = " + firm);
//				actual.add(firm);
//
//			}
//
//			log.debug("expected = " + expected.toString());
//			log.debug("actual = " + actual.toString());
//
//			Iterator itrT = actual.iterator();
//			Iterator itrT1 = expected.iterator();
//			while(itrT.hasNext()) {
//				assertEquals(itrT.next(), itrT1.next());
//				log.debug("passed");
//			}
//
//
//		} catch(Exception e) {
//			log.debug("findAllRefHearingTypes() has failed");
//			e.printStackTrace();
//			fail();
//		}
//	}
//
//
//
//	public void testFindByPrimaryKey() {
//		try {
//			RefHearingTypeHome home = lookupHome();
//			log.debug("testFindByPrimaryKey() - Got RefHearingTypeHome");
//			RefHearingType local = home.findByPrimaryKey(new Integer(1));
//			log.debug("refHearingTypeId : " + local.getRefHearingTypeId());
//			assertEquals(1, local.getRefHearingTypeId().intValue());
//		} catch(Exception e) {
//			log.debug("findByPrimaryKey() has failed");
//			e.printStackTrace();
//			fail();
//		}
//	}
//
//
//	private RefHearingTypeHome lookupHome() throws Exception {
//
//		Context ctx = CSServices.getServiceLocator().getInitialContext();
//		Object home = (RefHearingTypeHome) ctx.lookup("RefHearingTypeHome");
//		return (RefHearingTypeHome) PortableRemoteObject.narrow(home, RefHearingTypeHome.class);
//	}
//}