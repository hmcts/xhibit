//
//package uk.gov.courtservice.xhibit.test.business.entities.refadvocate;
//
//import java.util.Collection;
//
//import javax.naming.InitialContext;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocateHome;
//import weblogic.ejb.Query;
//import weblogic.ejb.QueryLocalHome;
//
//
//
//public class TestDynamicQuery extends TestCase
//{
//
//    public TestDynamicQuery(String s)
//
//    {
//        super(s);
//    }
//
//    protected void setUp()
//
//    {
//
//    }
//
//    protected void tearDown()
//
//    {
//
//    }
//    public void testDynamicQuery()
//    {
//
//        log("starting test");
//        try
//        {
//
//            InitialContext ic=new InitialContext();
//            RefAdvocateHome rah =(RefAdvocateHome)ic.lookup("RefAdvocateHome");
//            QueryLocalHome qh=(QueryLocalHome)rah;
//            String ejbql="SELECT OBJECT(o)FROM RefAdvocate o";
//            Query query=qh.createQuery();
//            query.setMaxElements(10);
//            Collection results=query.find(ejbql);
//            log("collection size=" + results.size());
//            log(results.toString());
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//            fail();
//        }
//
//    }
//    private void log (String msg)
//    {
//        System.out.println(msg);
//    }
//}