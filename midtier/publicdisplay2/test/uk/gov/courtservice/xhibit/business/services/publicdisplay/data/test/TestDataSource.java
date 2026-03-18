//package uk.gov.courtservice.xhibit.business.services.publicdisplay.data.test;
//
//
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.test.MidTierTestConstants;
//
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataSource;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.DataSourceFactory;
//import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataContext;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
//import uk.gov.courtservice.xhibit.common.publicdisplay.data.Data;
//
///**
// * <p>
// * Title:
// * </p>
// *
// * <p>
// * Description:
// * </p>
// *
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// *
// * <p>
// * Company: Electronic Data Systems
// * </p>
// *
// * @author Neil Ellis
// * @version $Revision: 1.2 $
// */
//public class TestDataSource  extends TransactionTestCase implements MidTierTestConstants
//{
//    public TestDataSource(String s) throws Exception
//    {
//        super(s, false);
//    }
//
//    public void testGetData() throws Exception {
//        try
//        {
//            DataSource dataSource = DataSourceFactory.getDataSource(new DataContext(START_DATE),new DisplayDocumentURI("publicdisplay://document/1/SummaryByName:1"));
//            dataSource.retrieve();
//            Data dataForUri = dataSource.getData();
//        }
//        catch (CSUnrecoverableException e)
//        {
//            e.printStackTrace();
//            System.out.println(e.getCause());
//        }
//        //assertNotNull(dataForUri);
//        //
//    }
//}
//