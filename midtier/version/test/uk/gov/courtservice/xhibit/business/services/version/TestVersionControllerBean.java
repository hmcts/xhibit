//package uk.gov.courtservice.xhibit.business.services.version;
//
//import uk.gov.courtservice.framework.exception.*;
//import uk.gov.courtservice.xhibit.business.services.*;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.vos.services.version.VersionValue;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2004</p>
// * <p>Company: </p>
// * @author Rakesh Lakhani
// * @version $Id: TestVersionControllerBean.java,v 1.2 2006/07/11 14:17:01 xzfdtb Exp $
// */
//
//public class TestVersionControllerBean extends TransactionTestCase
//{
//    public TestVersionControllerBean(String s)
//            throws Exception
//    {
//        super(s, false);
//    }
//
//    public void testGetVersions() throws Exception
//    {
//        VersionControllerBean delegate = new VersionControllerBean();
//        VersionValue[] c = delegate.getVersions();
//        for (int i = 0; i < c.length; i++) {
//            System.out.println("VERSION :" + c[i].toString());
//        }
//
//        assertEquals(1,c.length);
//    }
//
//    public void testGetVersionsDelegate() throws Exception
//    {
//        VersionControllerBeanBusinessDelegate delegate = VersionControllerBeanBusinessDelegate.DelegateFactory.getInstance();
//        VersionValue[] c = delegate.getVersions();
//        assertEquals(1,c.length);
//    }
//}