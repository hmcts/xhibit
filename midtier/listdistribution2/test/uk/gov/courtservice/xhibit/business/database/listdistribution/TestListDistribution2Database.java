//package uk.gov.courtservice.xhibit.business.database.listdistribution;
//
//import javax.sql.DataSource;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.castor.xml.ClassDescriptorResolverFactory;
//import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
//
///**
// * Test class for the <code>ListDistribution2Database</code> class.
// *
// * @author tz0d5m
// * @version $Id: TestListDistribution2Database.java,v 1.2 2006/07/13 12:58:01 xzfdtb Exp $
// *
// * @see uk.gov.courtservice.xhibit.business.database.listdistribution
// *      .ListDistribution2Database
// */
//public class TestListDistribution2Database extends TestCase
//{
//    /** Instance of the class under test */
//    private final ListDistribution2Database database =
//            new ListDistribution2DatabaseTestImpl();
//
//    public TestListDistribution2Database(String name) throws Exception
//    {
//        super(name);
//    }
//
//    public void testGetNextListId()
//    {
//        Integer listId = database.getNextListId();
//    }
//
//    public void testUpdateStatus()
//    {
//        database.updateStatus(new Integer(-3), true);
//    }
//
//    public void testGetListSummary()
//    {
//        try
//        {
//            database.getListSummary(ClassDescriptorResolverFactory.getClassDescriptorResolver(),
//                                    new Integer(-3));
//        }
//        catch (IllegalStateException e)
//        {
//            // ignore as we are only testing that the stored procs are
//            // formatted correctly...
//        }
//    }
//
//    /**
//     * Custom implementation for testing that overrides the getConnection()
//     * method to return a <code>Connection</code> from a
//     * <code>StandAloneDataSource</code> object.
//     */
//    private class ListDistribution2DatabaseTestImpl
//            extends ListDistribution2Database
//    {
//        /**
//         * Overridden to acquire a custom connection, and set autocommit to
//         * <i>false</i>.
//         */
//        protected DataSource getDataSource()
//        {
//            return new StandAloneDataSource();
//        }
//    }
//}
//