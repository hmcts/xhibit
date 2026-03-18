//package uk.gov.courtservice.xhibit.business.database.listdistribution;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.io.Writer;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import junit.framework.TestCase;
//
//import org.exolab.castor.xml.ClassDescriptorResolver;
//
//import uk.gov.courtservice.framework.castor.xml.ClassDescriptorResolverFactory;
//import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
//import uk.gov.courtservice.xhibit.business.vos.listdistribution.ListSummary;
//import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List;
//import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ListLetter;
//import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Recipient;
//import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.ListProcessor;
//import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.ListProcessorException;
//
///**
// * Test class for the <code>ListDistribution2DatabasePersist</code> class.
// * This class also contains stub implementations of the required classes
// * to prevent the necessary library files to be present, loaded and working
// * to allow testing to be performed on only the specific details.
// *
// * @author tz0d5m
// * @version $Id: TestListDistribution2DatabasePersist.java,v 1.2 2006/07/04 07:23:31 xzfdtb Exp $
// *
// * @see uk.gov.courtservice.xhibit.business.database.listdistribution
// *      .ListDistribution2DatabasePersist
// */
//public class TestListDistribution2DatabasePersist extends TestCase
//{
//    /** Instance of the class under test */
//    private final ListDistribution2DatabasePersist database =
//            new ListDistribution2DatabasePersistTestImpl();
//
//    public TestListDistribution2DatabasePersist(String name) throws Exception
//    {
//        super(name);
//    }
//
//    public void testPersistListLettersNullFirstParameterInput()
//    {
//        try
//        {
//            database.persistListLetters(null, getTestListSummary());
//            fail("An IllegalArgumentException should have been thrown");
//        }
//        catch (final IllegalArgumentException e)
//        {
//            // ignore as this is the expected result...
//        }
//    }
//
//    public void testPersistListLettersNullSecondParameterInput()
//    {
//        try
//        {
//            database.persistListLetters(ClassDescriptorResolverFactory.getClassDescriptorResolver(),
//                                        null);
//            fail("An IllegalArgumentException should have been thrown");
//        }
//        catch (final IllegalArgumentException e)
//        {
//            // ignore as this is the expected result...
//        }
//    }
//
//    public void testPersistListLettersNullBothParametersInput()
//    {
//        try
//        {
//            database.persistListLetters(null, null);
//            fail("An IllegalArgumentException should have been thrown");
//        }
//        catch (final IllegalArgumentException e)
//        {
//            // ignore as this is the expected result...
//        }
//    }
//
//    public void testPersistListLettersValidParameters()
//    {
//        database.persistListLetters(ClassDescriptorResolverFactory.getClassDescriptorResolver(),
//                                    getTestListSummary());
//    }
//
//    /**
//     * Construct a <code>ListSummary</code> object using our local stubs.
//     */
//    private ListSummary getTestListSummary()
//    {
//        return new ListSummary(new Integer(1),
//                               new ListTestStub(),
//                               new ListProcessorTestStub(),
//                               new Integer(3));
//    }
//
//    /**
//     * Stub implementation of the <code>ListProcessor</code> interface to
//     * provide limited consistent results.
//     *
//     * @author tz0d5m
//     */
//    private class ListProcessorTestStub implements ListProcessor
//    {
//        private int counter = 0;
//
//        public List readList(ClassDescriptorResolver classDescriptorResolver,
//                             Reader reader) throws ListProcessorException
//        {
//            throw new ListProcessorException("Method not implemented for testing");
//        }
//
//        public ListLetter[] processList(List list) throws ListProcessorException
//        {
//            return new ListLetterTestStub[] {
//                new ListLetterTestStub(new Integer(11354)),  // Valid, and can be found
//                new ListLetterTestStub(new Integer(1)),      // Valid, but cannot be found
//                new ListLetterTestStub(new Integer(12011)),  // Valid, and can be found
//                new ListLetterTestStub(null),                // No recipient
//                new ListLetterTestStub(null)                 // No recipient
//            };
//        }
//
//        public void writeLetter(ClassDescriptorResolver classDescriptorResolver,
//                                Writer writer,
//                                ListLetter listLetter) throws ListProcessorException
//        {
//            try
//            {
//                writer.write("writeLetter - writing some text - counter=" + counter);
//                writer.flush();
//            }
//            catch (IOException e)
//            {
//                throw new ListProcessorException(e);
//            }
//        }
//
//        public String getLetterType(ListLetter listLetter) throws ListProcessorException
//        {
//            return "DLL";
//        }
//
//        public String getLetterTitle(ListLetter listLetter) throws ListProcessorException
//        {
//            return "Daily List Letter - FROM TESTING - counter=" + ++counter;
//        }
//
//        public boolean hasRecipientId(ListLetter listLetter) throws ListProcessorException
//        {
//            return ((ListLetterTestStub) listLetter).hasRecipientId;
//        }
//
//        public Integer getRecipientId(ListLetter listLetter) throws ListProcessorException
//        {
//            return ((ListLetterTestStub) listLetter).recipientId;
//        }
//
//        public String getRecipientType(ListLetter listLetter) throws ListProcessorException
//        {
//            return "S";
//        }
//    }
//
//    /**
//     * Stub implementation of the <code>List</code> interface to
//     * provide an empty implementation.
//     *
//     * @author tz0d5m
//     */
//    private class ListTestStub implements List
//    {
//    }
//
//    /**
//     * Stub implementation of the <code>ListLetter</code> interface to
//     * provide limited consistent results.
//     *
//     * @author tz0d5m
//     */
//    private class ListLetterTestStub implements ListLetter
//    {
//        public final Integer recipientId;
//        public final boolean hasRecipientId;
//
//        public ListLetterTestStub(Integer recipientId)
//        {
//            this.recipientId = recipientId;
//            this.hasRecipientId = (recipientId != null);
//        }
//
//        public Recipient getRecipient()
//        {
//            return null;
//        }
//    }
//
//    /**
//     * Custom implementation for testing that overrides the getConnection()
//     * method to return a <code>Connection</code> from a
//     * <code>StandAloneDataSource</code> object.
//     */
//    private class ListDistribution2DatabasePersistTestImpl
//            extends ListDistribution2DatabasePersist
//    {
//        /**
//         * Overridden to acquire a custom connection, and set autocommit to
//         * <i>false</i>.
//         */
//        protected Connection getConnection() throws SQLException
//        {
//            final Connection con = (new StandAloneDataSource()).getConnection();
//            con.setAutoCommit(false);
//
//            // this is set up using command-line parameters...
//            // e.g. -Durl_key=jdbc:oracle:thin:@localhost:1521:ora9utf8
//            return con;
//        }
//
//        /**
//         * Overridden to force the transaction to be rolled back.
//         */
//        protected void closeConnection(Connection con)
//        {
//            if (con != null)
//            {
//                try
//                {
//                    con.rollback();
//                }
//                catch (final Throwable t)
//                {
//                    log.debug("Error commiting connection", t);
//                }
//
//                super.closeConnection(con);
//            }
//        }
//    }
//}
//