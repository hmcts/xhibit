//package uk.gov.courtservice.xhibit.test.business.entities.ccinfo;
//
////jdk
//import java.util.ArrayList;
//import java.util.Collection;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.ccinfo.CcInfo;
//import uk.gov.courtservice.xhibit.business.entities.ccinfo.CcInfoHome;
//
///**
// * <p>Title: TestRefDisposal</p>
// * <p>Description: This class only tests finder methods of RefDisposal</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Faisal Shoukat
// * @version 1.0
// */
//
//public class CCInfoTest extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(CCInfoTest.class);
//    Collection ccInfo = new ArrayList();
//    public  static String ccSQL= "INSERT INTO XHB_CC_INFO ( CC_INFO_ID, CC_INFO_TEXT, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES ( 1, 'ccInfo txt', 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//
//    public CCInfoTest(String s)
//    {
//	super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//
//	log.debug("setUp()");
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_CC_INFO");
//
//	log.debug("Insert CCInfo");
//	TestUtils.execSql(ccSQL);
//
//
//
//    }
//
//    protected void tearDown()throws Exception
//    {
//	log.debug("tearDown()");
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_CC_INFO");
//
//    }
//
//
//    public void testFindByPrimaryKey()
//    {
//	try
//	{
//	    CcInfoHome home = lookupHome();
//	    log.debug("testFindByPrimaryKey() - Got RefDisposalHome");
//	    CcInfo local = home.findByPrimaryKey(new Integer(1));
//	    log.debug("cINfoText : " + local.getCcInfoText());
//	    assertEquals(1, local.getCcInfoId().intValue());
//	}
//	catch(Exception e)
//	{
//	    log.debug("findByPrimaryKey() is failed");
//	    e.printStackTrace();
//	    fail();
//	}
//    }
//
//
//    private CcInfoHome lookupHome() throws Exception
//    {
//	Context ctx = CSServices.getServiceLocator().getInitialContext();
//	Object home = (CcInfoHome) ctx.lookup("CcInfoHome");
//	return (CcInfoHome) PortableRemoteObject.narrow(home, CcInfoHome.class);
//    }
//}